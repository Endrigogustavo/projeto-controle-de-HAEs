package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.WeeklyScheduleEntry;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Student;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.domain.singleton.CalendarioSingleton;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HaeService {

    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final CalendarioSingleton calendarioSingleton;

    public HaeService(HaeRepository haeRepository, EmployeeRepository employeeRepository,
            StudentRepository studentRepository, CalendarioSingleton calendarioSingleton) {
        this.haeRepository = haeRepository;
        this.employeeRepository = employeeRepository;
        this.studentRepository = studentRepository;
        this.calendarioSingleton = calendarioSingleton;
    }

    public Hae createHae(HaeRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com ID " + request.getEmployeeId()
                        + " não encontrado. Não é possível criar HAE."));

        List<Hae> existingHaes = haeRepository.findByEmployeeId(request.getEmployeeId());

        if (existingHaes.stream()
                .anyMatch(hae -> hae.getStatus() == Status.PENDENTE || hae.getStatus() == Status.APROVADO)) {
            throw new IllegalArgumentException("O professor já possui uma HAE pendente.");
        }

        LocalDate inicio = request.getStartDate();
        LocalDate fim = request.getEndDate();
        Set<LocalDate> diasNaoLetivos = calendarioSingleton.getCalendario().getDiasNaoLetivos();

        List<LocalDate> datasInvalidas = new ArrayList<>();

        if (diasNaoLetivos.contains(inicio)) {
            datasInvalidas.add(inicio);
        }
        if (diasNaoLetivos.contains(fim)) {
            datasInvalidas.add(fim);
        }
        if (!datasInvalidas.isEmpty()) {
            throw new IllegalArgumentException("As seguintes datas não são letivas: " + datasInvalidas);
        }

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
        newHae.setObservations(request.getObservation());
        newHae.setStatus(Status.PENDENTE);
        newHae.setCourse(request.getCourse());
        newHae.setProjectType(request.getProjectType());
        newHae.setModality(request.getModality());
        newHae.setCoordenatorId("Sem coordenador definido");
        newHae.setDayOfWeek(request.getDayOfWeek());
        newHae.setTimeRange(request.getTimeRange());
        newHae.setProjectDescription(request.getProjectDescription());

        // aqui, usa o Map<String, String> já formatado
        newHae.setWeeklySchedule(weeklyScheduleFlattened);

        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            newHae.setStudents(request.getStudentRAs());
        } else {
            newHae.setStudents(List.of());
        }

        return haeRepository.save(newHae);
    }

    @Transactional(readOnly = true)
    public Hae getHaeById(String id) {
        return haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));
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
    public List<Hae> getHaesByProfessorId(String professorId) {
        if (!employeeRepository.existsById(professorId)) {
            throw new IllegalArgumentException("Professor com ID " + professorId + " não encontrado.");
        }
        return haeRepository.findByEmployeeId(professorId);
    }

    @Transactional(readOnly = true)
    public List<Hae> getAllHaes() {
        return haeRepository.findAll();
    }

    @Transactional
    public Hae changeHaeStatus(String id, Status newStatus, String coordenadorId) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        Employee coordenador = employeeRepository.findById(coordenadorId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Coordenador com id " + coordenadorId + " não encontrado."));

        if (coordenador.getRole() != Role.COORDENADOR) {
            throw new IllegalArgumentException("Empregado com ID " + coordenadorId
                    + " não é um coordenador. Apenas coordenadores podem mudar o status da HAE.");
        }

        hae.setStatus(newStatus);
        hae.setCoordenatorId(coordenadorId);
        return haeRepository.save(hae);
    }

    @Transactional
    public List<Hae> getHaesByCourse(String course) {
        return haeRepository.findByCourse(course);
    }

    @Transactional(readOnly = true)
    public List<Hae> getHaesByType(HaeType haeType) {
        return haeRepository.findByProjectType(haeType);
    }

    // @Transactional(readOnly = true)
    // public List<Student> getStudentsByHaeId(String haeId) {
    // Hae hae = haeRepository.findById(haeId)
    // .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: "
    // + haeId));
    // return hae.getStudents();
    // }

    // TODO Implementar lógica de envio de email
    @Transactional
    public void sendEmailToCoordinatorAboutHAECreated(String coordinatorId, String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + haeId));
        Employee coordinator = employeeRepository.findById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com ID: " + coordinatorId));
        System.out.println("Email enviado para o coordenador " + coordinator.getName()
                + " sobre a criação da HAE com ID: " + haeId);
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
        Set<LocalDate> diasNaoLetivos = calendarioSingleton.getCalendario().getDiasNaoLetivos();

        List<LocalDate> datasInvalidas = new ArrayList<>();

        if (diasNaoLetivos.contains(inicio)) {
            datasInvalidas.add(inicio);
        }
        if (diasNaoLetivos.contains(fim)) {
            datasInvalidas.add(fim);
        }

        if (!datasInvalidas.isEmpty()) {
            throw new IllegalArgumentException("As seguintes datas não são letivas: " + datasInvalidas);
        }

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
        hae.setObservations(request.getObservation());
        hae.setCourse(request.getCourse());
        hae.setProjectType(request.getProjectType());
        hae.setModality(request.getModality());
        hae.setDayOfWeek(request.getDayOfWeek());
        hae.setTimeRange(request.getTimeRange());
        hae.setProjectDescription(request.getProjectDescription());

        // Verifica se o tipo de HAE é Estágio ou TCC e atribui os estudantes
        // cadastrados no sistema
        // if (request.getHaeType() == HaeType.Estagio || request.getHaeType() ==
        // HaeType.TCC) {
        // List<Student> students = request.getStudentRas().stream()
        // .map(ra -> studentRepository.findById(ra)
        // .orElseThrow(() -> new IllegalArgumentException("Estudante com RA " + ra + "
        // não encontrado.")))
        // .toList();
        // hae.setStudents(students);
        // } else {
        // hae.setStudents(List.of());
        // }

        if (request.getProjectType() == HaeType.Estagio || request.getProjectType() == HaeType.TCC) {
            List<String> studentRas = request.getStudentRAs();
            hae.setStudents(studentRas);
        } else {
            hae.setStudents(List.of());
        }

        return haeRepository.save(hae);
    }
}
