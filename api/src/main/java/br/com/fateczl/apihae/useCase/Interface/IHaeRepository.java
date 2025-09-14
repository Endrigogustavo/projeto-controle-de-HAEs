package br.com.fateczl.apihae.useCase.Interface;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Status;

public interface IHaeRepository {
    List<Hae> findByEmployeeId(String employeeId);

    List<Hae> findByCourse(String course);

    List<Hae> findTop5ByEmployeeIdOrderByCreatedAtDesc(String employeeId);

    List<Hae> findByProjectType(HaeType projectType);

    int countByEmployeeId(String employeeId);

    List<Hae> findByEndDateBefore(LocalDate data);

    List<Hae> findByInstitutionId(String institutionId);

    List<Hae> findByViewed(Boolean viewed);

    List<Hae> findByStatus(Status status);

    List<Hae> findBySemestre(int year, int monthStart, int monthEnd);

    List<Hae> findAll();

    Optional<Hae> findById(String id);

    Hae save(Hae hae);

    void delete(Hae hae);

    List<Hae> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    List<Hae> findByEmployeeIdAndCreatedAtBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    List<Hae> findByCourseAndProjectTypeAndStatusAndViewed(String course, HaeType projectType, Status status,
            Boolean viewed);

    List<Hae> findByCourseAndProjectTypeAndStatus(String course, HaeType projectType, Status status);

    List<Hae> findByCourseAndProjectTypeAndViewed(String course, HaeType projectType, Boolean viewed);

    List<Hae> findByCourseAndStatusAndViewed(String course, Status status, Boolean viewed);

    List<Hae> findByProjectTypeAndStatusAndViewed(HaeType projectType, Status status, Boolean viewed);

    List<Hae> findByCourseAndProjectType(String course, HaeType projectType);

    List<Hae> findByCourseAndStatus(String course, Status status);

    boolean existsById(String id);

    List<Hae> findAll(Specification<Hae> spec);

    void deleteById(String id);
}
