package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.response.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.domain.factory.HaeFactory;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static br.com.fateczl.apihae.useCase.util.DataUtils.getSemestre;

@RequiredArgsConstructor
@Service
public class HaeService {

    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final InstitutionService institutionService;
    private final InstitutionRepository institutionRepository;

    @Transactional
    public Hae createHae(HaeRequest request) {
        int qtdHaeFatec = institutionService.getHaeQtd(request.getInstitutionId());
        List<Hae> haesDoSemestre = findByCurrentSemester();

        if (haesDoSemestre.size() >= qtdHaeFatec) {
            throw new IllegalArgumentException("Limite de HAEs atingido no semestre. Não é possível criar mais HAEs.");
        }

        Institution institution = institutionRepository.findById(request.getInstitutionId())
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada com o ID fornecido."));

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

        employeeRepository.findCoordinatorByCourse(savedHae.getCourse())
                .ifPresentOrElse(
                        coordinator -> emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), savedHae),
                        () -> System.err.println("Aviso: Nenhum coordenador encontrado para o curso '"
                                + savedHae.getCourse() + "'. E-mail de notificação não enviado."));

        return savedHae;
    }

    @Transactional(readOnly = true)
    public HaeDetailDTO getHaeById(String id) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        String coordenatorName = "Sem coordenador definido";

        if (hae.getCoordenatorId() != null && !hae.getCoordenatorId().equals("Sem coordenador definido")) {
            coordenatorName = employeeRepository.findById(hae.getCoordenatorId())
                    .map(Employee::getName)
                    .orElse("Coordenador não encontrado");
        }

        return new HaeDetailDTO(hae, coordenatorName);
    }

    @Transactional
    public void deleteHae(String id) {
        if (!haeRepository.existsById(id)) {
            throw new IllegalArgumentException("HAE não encontrado com ID: " + id);
        }
        haeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByEmployeeId(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Empregado com ID " + employeeId + " não encontrado.");
        }
        return haeRepository.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByEmployeeIdWithLimit(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Empregado com ID " + employeeId + " não encontrado.");
        }
        return haeRepository.findTop5ByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    @Transactional(readOnly = true)
    public List<HaeResponseDTO> getHaesByProfessorId(String professorId) {
        if (!employeeRepository.existsById(professorId)) {
            throw new IllegalArgumentException("Professor com ID " + professorId + " não encontrado.");
        }
        List<Hae> haes = haeRepository.findByEmployeeId(professorId);

        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HaeResponseDTO> getAllHaes() {
        List<Hae> haes = haeRepository.findAll();
        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HaeResponseDTO> getHaesByCourse(String course) {
        List<Hae> haes = haeRepository.findByCourse(course);
        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByType(HaeType haeType) {
        return haeRepository.findByProjectType(haeType);
    }

    @Transactional
    public Hae updateHae(String id, HaeRequest request) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + id));

        if (hae.getStatus() == Status.COMPLETO) {
            throw new IllegalStateException("Não é possível editar uma HAE com status COMPLETO.");
        }

        hae.setProjectTitle(request.getProjectTitle());
        hae.setCourse(request.getCourse());
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setProjectDescription(request.getProjectDescription());
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setDayOfWeek(request.getDayOfWeek());
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

        employeeRepository.findCoordinatorByCourse(hae.getCourse())
                .ifPresent(coordinator -> emailService.sendHaeUpdatedNotificationEmail(coordinator.getEmail(),
                        updatedHae));

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

        Institution institution = institutionRepository.findById(request.getInstitutionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Instituição não encontrada com ID: " + request.getInstitutionId()));

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        Hae hae = HaeFactory.createByCoordinator(employee, request, institution, coordinator, weeklyScheduleFlattened);

        return haeRepository.save(hae);
    }

    public List<Hae> findByCurrentSemester() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int monthStart = (today.getMonthValue() <= 6) ? 1 : 7;
        int monthEnd = (today.getMonthValue() <= 6) ? 6 : 12;
        return haeRepository.findBySemestre(year, monthStart, monthEnd);
    }

    public List<HaeResponseDTO> getHaesByInstitutionId(String institutionId) {
        List<Hae> haes = haeRepository.findByInstitutionId(institutionId);

        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }

    public int getHaeCountByCurrentSemester() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int monthStart = (today.getMonthValue() <= 6) ? 1 : 7;
        int monthEnd = (today.getMonthValue() <= 6) ? 6 : 12;
        List<Hae> countSemesterHae = haeRepository.findBySemestre(year, monthStart, monthEnd);
        return countSemesterHae.size();
    }

    @Transactional(readOnly = true)
    public List<HaeResponseDTO> getHaeByStatus(Status status) {
        List<Hae> haes = haeRepository.findByStatus(status);

        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HaeResponseDTO> searchHaes(String institutionId, String course, HaeType haeType, Status status,
            Boolean viewed) {
        Specification<Hae> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("institution").get("id"), institutionId));

            if (course != null && !course.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("course"), course));
            }
            if (haeType != null) {
                predicates.add(criteriaBuilder.equal(root.get("projectType"), haeType));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (viewed != null) {
                predicates.add(criteriaBuilder.equal(root.get("viewed"), viewed));
            }
            if (institutionId != null && !institutionId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("institution").get("id"), institutionId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Hae> haes = haeRepository.findAll(spec);

        return haes.stream()
                .map(HaeResponseDTO::new)
                .collect(Collectors.toList());
    }
}