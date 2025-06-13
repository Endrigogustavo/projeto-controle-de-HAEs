package br.com.fateczl.apihae.driver.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fateczl.apihae.domain.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
}
