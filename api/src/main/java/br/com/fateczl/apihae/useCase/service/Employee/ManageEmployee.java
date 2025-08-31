package br.com.fateczl.apihae.useCase.service.Employee;

import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.factory.EmployeeFactory;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.jasypt.util.text.TextEncryptor;

import br.com.fateczl.apihae.driver.repository.PasswordResetTokenRepository;

@RequiredArgsConstructor
@Service
public class ManageEmployee {

    private final EmployeeRepository employeeRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TextEncryptor textEncryptor;

    @Transactional
    public void deleteEmployeeAccount(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
        passwordResetTokenRepository.deleteByEmployee(employee);
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
            existingEmployeeWithEmail.ifPresent(accountValidation -> {
                if (!accountValidation.getId().equals(id)) {
                    throw new IllegalArgumentException("Email em uso por outra conta.");
                }
            });
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

    @Transactional
    public Employee createEmployeeByDiretorOrAdmin(EmployeeCreateByDiretorOrAdmRequest request) {
        Employee employee = EmployeeFactory.createEmployee(request);
        String plainPassword = request.getProvisoryPassword();
        String encryptedPassword = textEncryptor.encrypt(plainPassword);
        employee.setPassword(encryptedPassword);

        Institution institution = institutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        employee.setInstitution(institution);
        return employeeRepository.save(employee);
    }
}
