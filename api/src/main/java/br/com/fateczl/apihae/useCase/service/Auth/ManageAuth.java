package br.com.fateczl.apihae.useCase.service.Auth;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.entity.PasswordResetToken;
import br.com.fateczl.apihae.domain.enums.Role;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import br.com.fateczl.apihae.domain.factory.EmployeeFactory;
import br.com.fateczl.apihae.useCase.Interface.IEmailVerificationRepository;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IInstitutionRepository;
import br.com.fateczl.apihae.useCase.Interface.IPasswordResetTokenRepository;

@Service
@RequiredArgsConstructor
public class ManageAuth {
    
    private final IEmployeeRepository iEmployeeRepository;
    private final IEmailVerificationRepository iEmailVerificationRepository;
    private final IPasswordResetTokenRepository iPasswordResetTokenRepository;
    private final BasicTextEncryptor textEncryptor;
    private final IInstitutionRepository iInstitutionRepository;

    @Transactional(readOnly = true)
    public Employee login(String email, String plainPassword) {
        Employee employee = iEmployeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        String decryptedStoredPassword = textEncryptor.decrypt(employee.getPassword());
        if (!plainPassword.equals(decryptedStoredPassword)) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }
        return employee;
    }

    @Transactional
    public Employee verifyEmailCode(String token, Integer institutionCode) {
        EmailVerification verification = iEmailVerificationRepository.findByCode(token)
                .filter(verificationFilter -> verificationFilter.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Token de ativação inválido ou expirado."));

        String email = verification.getEmail();

        iEmployeeRepository.findByEmail(email).ifPresent(existingEmployee -> {
            iEmailVerificationRepository.delete(verification);
            throw new IllegalArgumentException("Email já associado com uma conta registrada.");
        });

        Employee newEmployee = EmployeeFactory.fromEmailVerification(verification, email);

        Institution institution = iInstitutionRepository.findByInstitutionCode(institutionCode)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        newEmployee.setInstitution(institution);

        if (email.toLowerCase().endsWith("@cps.sp.gov.br")) {
            newEmployee.setRole(Role.COORDENADOR);
        } else {
            newEmployee.setRole(Role.PROFESSOR);
        }

        Employee savedEmployee = iEmployeeRepository.save(newEmployee);
        iEmailVerificationRepository.delete(verification);

        return savedEmployee;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = iPasswordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            iPasswordResetTokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        Employee employee = resetToken.getEmployee();
        String encryptedPassword = textEncryptor.encrypt(newPassword);
        employee.setPassword(encryptedPassword);
        iEmployeeRepository.save(employee);

        iPasswordResetTokenRepository.delete(resetToken);
    }

}