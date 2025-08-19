package br.com.fateczl.apihae.domain.factory;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.domain.entity.EmailVerification;

public class EmployeeFactory {
    public static Employee fromEmailVerification(EmailVerification verification, String email) {
        Employee employee = new Employee();
        employee.setName(verification.getName());
        employee.setEmail(email);
        employee.setCourse(verification.getCourse());
        employee.setPassword(verification.getPassword());
        return employee;
    }

    public static Employee createEmployee(EmployeeCreateByDiretorOrAdmRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setCourse(request.getCourse());
        employee.setPassword(request.getProvisoryPassword());
        employee.setRole(Role.PROFESSOR);
        return employee;
    }
}