package br.com.fateczl.apihae.useCase.service.Hae;

import br.com.fateczl.apihae.adapter.dto.response.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShowHae {

    private final HaeRepository haeRepository;
    private final EmployeeRepository employeeRepository;

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