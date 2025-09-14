package br.com.fateczl.apihae.useCase.Interface;

import java.util.List;
import java.util.Optional;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;

public interface IEmployeeRepository {
    Optional<Employee> findByEmail(String email);

    List<Employee> findAllByRole(Role role);

    List<Employee> findByInstitutionId(String institutionId);

    Optional<Employee> findByCourseAndRole(String course, Role role);

    Employee save(Employee employee);

    Optional<Employee> findById(String id);

    void delete(Employee employee);

    void deleteById(String id);

    List<Employee> findAll();

    boolean existsById(String id);
}
