package br.com.fateczl.apihae.adapter.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.fateczl.apihae.useCase.Interface.IHaeRepository;
import br.com.fateczl.apihae.domain.entity.Hae;

public interface HaeRepository extends IHaeRepository, JpaRepository<Hae, String>, JpaSpecificationExecutor<Hae> {
    @Query("""
                SELECT h FROM Hae h
                WHERE YEAR(h.createdAt) = :year
                  AND MONTH(h.createdAt) BETWEEN :monthStart AND :monthEnd
            """)
    List<Hae> findBySemestre(@Param("year") int year,
            @Param("monthStart") int monthStart,
            @Param("monthEnd") int monthEnd);

            
}