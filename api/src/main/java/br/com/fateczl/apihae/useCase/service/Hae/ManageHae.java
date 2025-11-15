package br.com.fateczl.apihae.useCase.service.Hae;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.request.HaeClosureRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.domain.factory.HaeFactory;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IHaeRepository;
import br.com.fateczl.apihae.useCase.Interface.IInstitutionRepository;
import br.com.fateczl.apihae.useCase.service.EmailService;
import br.com.fateczl.apihae.useCase.service.Institution.ShowInstitution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static br.com.fateczl.apihae.useCase.util.DataUtils.getSemestre;

@RequiredArgsConstructor
@Service
public class ManageHae {

    private final IHaeRepository iHaeRepository;
    private final IEmployeeRepository iEmployeeRepository;
    private final EmailService emailService;
    private final ShowInstitution showInstitution;
    private final IInstitutionRepository institutionRepository;
    private final ShowHae showHae;

    @Transactional
    public Hae createHae(HaeRequest request) {
        Institution institution = institutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com o código fornecido."));

        limitHaeValidation(institution.getId(), request.getWeeklyHours(), null);

        Employee employee = iEmployeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível criar HAE."));

        LocalDate newHaeStartDate = request.getStartDate();
        String newHaeSemestre = getSemestre(newHaeStartDate);

        List<Hae> existingHaes = iHaeRepository.findByEmployeeId(request.getEmployeeId());

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
        Hae savedHae = iHaeRepository.save(newHae);

        emailService.sendAlertProfessorHaeStatusEmail(employee.getEmail(), savedHae);

        iEmployeeRepository.findByCourseAndRole(savedHae.getCourse(), Role.COORDENADOR)
                .ifPresentOrElse(
                        coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), savedHae),
                        () -> System.err.println("Aviso: Nenhum coordenador encontrado para o curso '"
                                + savedHae.getCourse() + "'. E-mail de notificação não enviado."));

        return savedHae;
    }

    @Transactional
    public void deleteHae(String id) {
        if (!iHaeRepository.existsById(id)) {
            throw new IllegalArgumentException("HAE não encontrado com ID: " + id);
        }
        iHaeRepository.deleteById(id);
    }

    @Transactional
    public Hae updateHae(String id, HaeRequest request) {
        Hae hae = iHaeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + id));

        if (hae.getStatus() == Status.COMPLETO) {
            throw new IllegalStateException("Não é possível editar uma HAE com status COMPLETO.");
        }

        Employee employee = iEmployeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível atualizar HAE."));

        if (!hae.getEmployee().equals(employee)) {
            throw new IllegalArgumentException("Funcionário não é o responsável pela HAE.");
        }

        limitHaeValidation(hae.getInstitution().getId(), request.getWeeklyHours(), hae.getWeeklyHours());

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
        hae.setWeeklyHours(request.getWeeklyHours());

        hae.setStatus(Status.PENDENTE);
        hae.setCoordenatorId("Sem coordenador definido");
        hae.setViewed(false);
        hae.setUpdatedAt(LocalDateTime.now());

        Hae updatedHae = iHaeRepository.save(hae);

        iEmployeeRepository.findByCourseAndRole(hae.getCourse(), Role.COORDENADOR)
                .ifPresentOrElse(
                        coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), hae),
                        () -> System.err.println("Aviso: Nenhum coordenador encontrado para o curso '"
                                + hae.getCourse() + "'. E-mail de notificação não enviado."));

        return updatedHae;
    }

    @Transactional
    public Hae createHaeAsCoordinator(String coordinatorId, String employeeId, HaeRequest request) {
        Employee coordinator = iEmployeeRepository.findById(coordinatorId)
                .filter(emp -> emp.getRole() == Role.COORDENADOR)
                .orElseThrow(() -> new IllegalArgumentException(
                        "O empregado com ID " + coordinatorId + " não é um coordenador."));

        Employee employee = iEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com ID: " + employeeId));

        Institution institution = institutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Instituição não encontrada com código: " + request.getInstitutionCode()));

        limitHaeValidation(institution.getId(), request.getWeeklyHours(), null);

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        Hae hae = HaeFactory.createByCoordinator(employee, request, institution, coordinator, weeklyScheduleFlattened);

        return iHaeRepository.save(hae);
    }

    @Transactional
    public void limitHaeValidation(String institutionId, int hoursRequested, Integer currentHours) {
        int qtdHaeFatec = showInstitution.getHaeQtdHours(institutionId);
        int weeklyHours = showHae.getWeeklyHoursAllHaesInstitutionByCurrentSemester(institutionId);

        if (currentHours != null && hoursRequested <= currentHours) {
            return;
        }

        int overtime = (currentHours != null) ? hoursRequested - currentHours : hoursRequested;
        int totalWithNewHours = weeklyHours + overtime;

        if (totalWithNewHours > qtdHaeFatec) {
            throw new IllegalArgumentException(
                    "O lançamento dessa HAE ultrapassa o limite de horas permitidas (" + qtdHaeFatec +
                            "h). Atualmente já existem " + weeklyHours + "h cadastradas neste semestre.");
        }
    }

    @Transactional
    public Hae requestClosure(String haeId, HaeClosureRequest request) {
        Hae hae = iHaeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + haeId));

        if (hae.getStatus() != Status.APROVADO) {
            throw new IllegalStateException("Apenas HAEs com status APROVADO podem solicitar fechamento.");
        }

        // Populate closure fields based on HAE type
        switch (hae.getProjectType()) {
            case TCC:
                hae.setTccRole(request.getTccRole());
                hae.setTccStudentCount(request.getTccStudentCount());
                hae.setTccStudentNames(request.getTccStudentNames());
                hae.setTccApprovedStudents(request.getTccApprovedStudents());
                hae.setTccProjectInfo(request.getTccProjectInfo());
                break;

            case Estagio:
                hae.setEstagioStudentInfo(request.getEstagioStudentInfo());
                hae.setEstagioApprovedStudents(request.getEstagioApprovedStudents());
                break;

            case ApoioDirecao:
                hae.setApoioType(request.getApoioType());
                hae.setApoioGeralDescription(request.getApoioGeralDescription());
                hae.setApoioApprovedStudents(request.getApoioApprovedStudents());
                hae.setApoioCertificateStudents(request.getApoioCertificateStudents());
                break;

            default:
                throw new IllegalArgumentException("Tipo de HAE não reconhecido: " + hae.getProjectType());
        }

        hae.setStatus(Status.FECHAMENTO_SOLICITADO);
        hae.setUpdatedAt(LocalDateTime.now());

        Hae updatedHae = iHaeRepository.save(hae);

        // Notify coordinator about closure request
        iEmployeeRepository.findByCourseAndRole(hae.getCourse(), Role.COORDENADOR)
                .ifPresent(coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), hae));

        return updatedHae;
    }

}