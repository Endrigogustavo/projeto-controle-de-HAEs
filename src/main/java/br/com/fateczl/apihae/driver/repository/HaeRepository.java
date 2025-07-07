package br.com.fateczl.apihae.driver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fateczl.apihae.domain.entity.Hae;

public interface HaeRepository extends JpaRepository<Hae, String> {
    List<Hae> findByEmployeeId(String employeeId);

    List<Hae> findByCourse(String course);

}
