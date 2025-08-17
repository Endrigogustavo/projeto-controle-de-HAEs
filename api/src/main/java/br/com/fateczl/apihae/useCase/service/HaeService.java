package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.HaeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.fateczl.apihae.domain.entity.Institution;
import static br.com.fateczl.apihae.useCase.util.DataUtils.getSemestre;
import lombok.RequiredArgsConstructor;

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

        LocalDate inicio = request.getStartDate();
        LocalDate fim = request.getEndDate();

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        Hae newHae = new Hae();
        newHae.setEmployee(employee);
        newHae.setNameEmployee(employee.getName());
        newHae.setProjectTitle(request.getProjectTitle());
        newHae.setWeeklyHours(request.getWeeklyHours());
        newHae.setStartDate(request.getStartDate());
        newHae.setEndDate(request.getEndDate());
        newHae.setObservations(request.getObservations());
        newHae.setStatus(Status.PENDENTE);
        newHae.setCourse(request.getCourse());
        newHae.setProjectType(request.getProjectType());
        newHae.setModality(request.getModality());
        newHae.setCoordenatorId("Sem coordenador definido");
        newHae.setDayOfWeek(request.getDayOfWeek());
        newHae.setTimeRange(request.getTimeRange());
        newHae.setProjectDescription(request.getProjectDescription());
        newHae.setWeeklySchedule(weeklyScheduleFlattened);

        newHae.setInstitution(institution);

        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            newHae.setStudents(request.getStudentRAs());
        } else {
            newHae.setStudents(List.of());
        }

        return haeRepository.save(newHae);
    }

    @Transactional(readOnly = true)
    public HaeDetailDTO getHaeById(String id) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        String coordenatorName = "Sem coordenador definido"; // Valor padrão

        if (hae.getCoordenatorId() != null && !hae.getCoordenatorId().equals("Sem coordenador definido")) {
            Optional<Employee> coordinator = employeeRepository.findById(hae.getCoordenatorId());
            if (coordinator.isPresent()) {
                coordenatorName = coordinator.get().getName();
            } else {
                coordenatorName = "Coordenador não encontrado";
            }
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

    @Transactional
    public List<Hae> getHaesByCourse(String course) {
        return haeRepository.findByCourse(course);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByType(HaeType haeType) {
        return haeRepository.findByProjectType(haeType);
    }

    @Transactional
    public Hae updateHae(String id, HaeRequest request) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + id));

        Map<String, String> weeklyScheduleFlattened = request.getWeeklySchedule()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getTimeRange()));

        hae.setCourse(request.getCourse());
        hae.setProjectTitle(request.getProjectTitle());
        hae.setWeeklyHours(request.getWeeklyHours());
        hae.setProjectType(request.getProjectType());
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setTimeRange(request.getTimeRange());
        hae.setProjectDescription(request.getProjectDescription());
        hae.setObservations(request.getObservations());
        hae.setStatus(Status.PENDENTE);
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setModality(request.getModality());
        hae.setStudents(request.getStudentRAs());
        hae.setWeeklySchedule(weeklyScheduleFlattened);
        hae.setUpdatedAt(LocalDateTime.now());

        return haeRepository.save(hae);
    }

    @Transactional
    public void sendEmailToCoordinatorAboutHAECreated(String coordinatorId, String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + haeId));
        Employee coordinator = employeeRepository.findById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com ID: " + coordinatorId));
        if (coordinator.getRole() != Role.COORDENADOR) {
            throw new IllegalArgumentException("O empregado com ID " + coordinatorId + " não é um coordenador.");
        }
        emailService.sendAlertCoordenadorEmail(coordinator.getEmail(), hae);
    }

    @Transactional
    public void sendEmailToProfessorAboutHaeStatus(String professorId, String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + haeId));
        Employee professor = employeeRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado com ID: " + professorId));
        if (professor.getRole() != Role.PROFESSOR) {
            throw new IllegalArgumentException("O empregado com ID " + professorId + " não é um professor.");
        }

        emailService.sendAlertProfessorHaeStatusEmail(professor.getEmail(), hae);
    }

    @Transactional
    public Hae createHaeAsCoordinator(String coordinatorId, String employeeId, HaeRequest request) {
        Employee coordinator = employeeRepository.findById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com ID: " + coordinatorId));
        if (coordinator.getRole() != Role.COORDENADOR) {
            throw new IllegalArgumentException("O empregado com ID " + coordinatorId + " não é um coordenador.");
        }

        LocalDate inicio = request.getStartDate();
        LocalDate fim = request.getEndDate();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com ID: " + employeeId));

        Hae hae = new Hae();
        hae.setEmployee(employee);
        hae.setNameEmployee(employee.getName());
        hae.setStatus(Status.APROVADO);
        hae.setCoordenatorId(coordinatorId);
        hae.setProjectTitle(request.getProjectTitle());
        hae.setWeeklyHours(request.getWeeklyHours());
        hae.setStartDate(request.getStartDate());
        hae.setEndDate(request.getEndDate());
        hae.setObservations(request.getObservations());
        hae.setCourse(request.getCourse());
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setTimeRange(request.getTimeRange());
        hae.setProjectDescription(request.getProjectDescription());

        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            List<String> studentRas = request.getStudentRAs();
            hae.setStudents(studentRas);
        } else {
            hae.setStudents(List.of());
        }

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
}