package br.com.fateczl.apihae.driver.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findAllByRole(Role role);
    List<Employee> findByInstitutionId(String institutionId);
    Optional<Employee> findByCourseAndRole(String course, Role role);
}
