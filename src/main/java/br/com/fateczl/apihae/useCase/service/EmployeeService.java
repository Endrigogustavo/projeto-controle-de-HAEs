package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    

    @Transactional(readOnly = true)
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
    }

    @Transactional
    public void deleteEmployeeAccount(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Empregado não encontrado com ID: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Transactional
    public Employee updateEmployeeAccount(String id, String newName, String newEmail) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        if (newName != null && !newName.trim().isEmpty()) {
            employee.setName(newName.trim());
        }

        if (newEmail != null && !newEmail.trim().isEmpty()) {
            Optional<Employee> existingEmployeeWithEmail = employeeRepository.findByEmail(newEmail.trim());
            if (existingEmployeeWithEmail.isPresent() && !existingEmployeeWithEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email em uso por outra conta.");
            }
            employee.setEmail(newEmail.trim());
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee changeEmployeeRole(String id, Role newRole) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        employee.setRole(newRole);
        return employeeRepository.save(employee);
    }
}
