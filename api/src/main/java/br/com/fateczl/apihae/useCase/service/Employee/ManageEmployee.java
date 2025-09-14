package br.com.fateczl.apihae.useCase.service.Employee;

import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.factory.EmployeeFactory;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IInstitutionRepository;
import br.com.fateczl.apihae.useCase.Interface.IPasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.jasypt.util.text.TextEncryptor;

@RequiredArgsConstructor
@Service
public class ManageEmployee {

    private final IEmployeeRepository iEmployeeRepository;
    private final IInstitutionRepository iInstitutionRepository;
    private final IPasswordResetTokenRepository iPasswordResetTokenRepository;
    private final TextEncryptor textEncryptor;

    @Transactional
    public void deleteEmployeeAccount(String id) {
        Employee employee = iEmployeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
        iPasswordResetTokenRepository.deleteByEmployee(employee);
        iEmployeeRepository.deleteById(id);
    }

    @Transactional
    public Employee updateEmployeeAccount(String id, String newName, String newEmail) {
        Employee employee = iEmployeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        if (newName != null && !newName.trim().isEmpty()) {
            employee.setName(newName.trim());
        }

        if (newEmail != null && !newEmail.trim().isEmpty()) {
            Optional<Employee> existingEmployeeWithEmail = iEmployeeRepository.findByEmail(newEmail.trim());
            existingEmployeeWithEmail.ifPresent(accountValidation -> {
                if (!accountValidation.getId().equals(id)) {
                    throw new IllegalArgumentException("Email em uso por outra conta.");
                }
            });
            employee.setEmail(newEmail.trim());
        }

        return iEmployeeRepository.save(employee);
    }

    @Transactional
    public Employee changeEmployeeRole(String id, Role newRole) {
        Employee employee = iEmployeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        employee.setRole(newRole);
        return iEmployeeRepository.save(employee);
    }

    @Transactional
    public Employee createEmployeeByDiretorOrAdmin(EmployeeCreateByDiretorOrAdmRequest request) {
        Employee employee = EmployeeFactory.createEmployee(request);
        String plainPassword = request.getProvisoryPassword();
        String encryptedPassword = textEncryptor.encrypt(plainPassword);
        employee.setPassword(encryptedPassword);

        Institution institution = iInstitutionRepository.findByInstitutionCode(request.getInstitutionCode())
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        employee.setInstitution(institution);
        return iEmployeeRepository.save(employee);
    }
}
