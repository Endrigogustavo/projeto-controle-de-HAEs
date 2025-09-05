package br.com.fateczl.apihae.useCase.service.Hae;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.domain.factory.HaeFactory;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import br.com.fateczl.apihae.useCase.service.EmailService;
import br.com.fateczl.apihae.useCase.service.Institution.ShowInstitution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static br.com.fateczl.apihae.useCase.util.DataUtils.getSemestre;

@RequiredArgsConstructor
@Service
public class ManageHae {

    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final ShowInstitution showInstitution;
    private final InstitutionRepository institutionRepository;
    private final ShowHae showHae;

    @Transactional
    public Hae createHae(HaeRequest request) {
        Institution institution = institutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com o código fornecido."));

        validarLimiteDeHAEs(institution.getId(), request.getWeeklyHours());

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível criar HAE."));

        LocalDate newHaeStartDate = request.getStartDate();
        String newHaeSemestre = getSemestre(newHaeStartDate);

        List<Hae> existingHaes = haeRepository.findByEmployeeId(request.getEmployeeId());

        boolean hasUnfinishedPastHae = existingHaes.stream().anyMatch(hae -> {
            String existingHaeSemestre = getSemestre(hae.getStartDate());
            return existingHaeSemestre.compareTo(newHaeSemestre) < 0 && hae.getStatus() != Status.COMPLETO;
        });

        if (hasUnfinishedPastHae) {
            throw new IllegalArgumentException(
                    "Você possui HAEs de semestres anteriores que não foram concluídas. Finalize-as para poder criar novas.");
        }

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        Hae newHae = HaeFactory.createHae(request, employee, institution, weeklyScheduleFlattened);
        Hae savedHae = haeRepository.save(newHae);

        emailService.sendAlertProfessorHaeStatusEmail(employee.getEmail(), savedHae);

        employeeRepository.findByCourseAndRole(savedHae.getCourse(), Role.COORDENADOR)
                .ifPresentOrElse(
                        coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), savedHae),
                        () -> System.err.println("Aviso: Nenhum coordenador encontrado para o curso '"
                                + savedHae.getCourse() + "'. E-mail de notificação não enviado."));

        return savedHae;
    }

    @Transactional
    public void deleteHae(String id) {
        if (!haeRepository.existsById(id)) {
            throw new IllegalArgumentException("HAE não encontrado com ID: " + id);
        }
        haeRepository.deleteById(id);
    }

    @Transactional
    public Hae updateHae(String id, HaeRequest request) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + id));

        if (hae.getStatus() == Status.COMPLETO) {
            throw new IllegalStateException("Não é possível editar uma HAE com status COMPLETO.");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível atualizar HAE."));

        if (!hae.getEmployee().equals(employee)) {
            throw new IllegalArgumentException("Funcionário não é o responsável pela HAE.");
        }

        validarLimiteDeHAEs(hae.getInstitution().getId(), request.getWeeklyHours());

        hae.setProjectTitle(request.getProjectTitle());
        hae.setCourse(request.getCourse());
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setProjectDescription(request.getProjectDescription());
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setDimensao(request.getDimensao());
        hae.setObservations(request.getObservations());
        hae.setStudents(request.getStudentRAs());

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));
        hae.setWeeklySchedule(weeklyScheduleFlattened);

        double totalMinutes = 0;
        for (String timeRange : weeklyScheduleFlattened.values()) {
            try {
                String[] times = timeRange.split(" - ");
                if (times.length == 2) {
                    LocalTime startTime = LocalTime.parse(times[0]);
                    LocalTime endTime = LocalTime.parse(times[1]);
                    if (endTime.isAfter(startTime)) {
                        totalMinutes += ChronoUnit.MINUTES.between(startTime, endTime);
                    }
                }
            } catch (Exception e) {
                System.err.println("Formato de hora inválido no cronograma durante a atualização: " + timeRange);
            }
        }
        hae.setWeeklyHours((int) Math.round(totalMinutes / 60.0));

        hae.setStatus(Status.PENDENTE);
        hae.setCoordenatorId("Sem coordenador definido");
        hae.setViewed(false);
        hae.setUpdatedAt(LocalDateTime.now());

        Hae updatedHae = haeRepository.save(hae);

        employeeRepository.findByCourseAndRole(hae.getCourse(), Role.COORDENADOR)
                .ifPresentOrElse(
                        coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), hae),
                        () -> System.err.println("Aviso: Nenhum coordenador encontrado para o curso '"
                                + hae.getCourse() + "'. E-mail de notificação não enviado."));

        return updatedHae;
    }

    @Transactional
    public Hae createHaeAsCoordinator(String coordinatorId, String employeeId, HaeRequest request) {
        Employee coordinator = employeeRepository.findById(coordinatorId)
                .filter(emp -> emp.getRole() == Role.COORDENADOR)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O empregado com ID " + coordinatorId + " não é um coordenador."));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com ID: " + employeeId));

        Institution institution = institutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Instituição não encontrada com código: " + request.getInstitutionCode()));

        validarLimiteDeHAEs(institution.getId(), request.getWeeklyHours());

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        Hae hae = HaeFactory.createByCoordinator(employee, request, institution, coordinator, weeklyScheduleFlattened);

        return haeRepository.save(hae);
    }

    @Transactional
    public void validarLimiteDeHAEs(String institutionId, int horasSolicitadas) {
        int qtdHaeFatec = showInstitution.getHaeQtdHours(institutionId);
        int weeklyHours = showHae.getWeeklyHoursAllHaesInstitutionByCurrentSemester(institutionId);

        int totalComNovasHoras = weeklyHours + horasSolicitadas;

        if (totalComNovasHoras > qtdHaeFatec) {
            throw new IllegalArgumentException(
                    "A adição dessa HAE ultrapassa o limite de horas permitidas (" + qtdHaeFatec +
                            "h). Atualmente já existem " + weeklyHours +
                            "h cadastradas neste semestre.");
        }
    }

}