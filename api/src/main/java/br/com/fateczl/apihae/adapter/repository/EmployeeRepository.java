package br.com.fateczl.apihae.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;

public interface EmployeeRepository extends IEmployeeRepository,JpaRepository<Employee, String> {
}
