package br.com.fateczl.apihae.driver.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;

public interface HaeRepository extends JpaRepository<Hae, String> {
    List<Hae> findByEmployeeId(String employeeId);
    List<Hae> findByCourse(String course);
    List<Hae> findTop5ByEmployeeIdOrderByCreatedAtDesc(String employeeId);
    List<Hae> findByProjectType(HaeType projectType);
    int countByEmployeeId(String employeeId);
    List<Hae> findByEndDateBefore(LocalDate data);
    List<Hae> findByInstitutionId(String institutionId);

    @Query("""
                SELECT h FROM Hae h
                WHERE YEAR(h.createdAt) = :year
                  AND MONTH(h.createdAt) BETWEEN :monthStart AND :monthEnd
            """)
    List<Hae> findBySemestre(@Param("year") int year,
            @Param("monthStart") int monthStart,
            @Param("monthEnd") int monthEnd);
}