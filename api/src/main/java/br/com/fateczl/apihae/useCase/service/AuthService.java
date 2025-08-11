package br.com.fateczl.apihae.useCase.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.PasswordResetToken;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmailVerificationRepository;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final BasicTextEncryptor textEncryptor;

    @Transactional(readOnly = true)
    public Employee login(String email, String plainPassword) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        try {
            String decryptedStoredPassword = textEncryptor.decrypt(employee.getPassword());
            if (!plainPassword.equals(decryptedStoredPassword)) {
                throw new IllegalArgumentException("Credenciais inválidas.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        return employee;
    }

    @Transactional
    public void sendVerificationCode(String name, String email, String course, String plainPassword) {
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já registrado.");
        }

        String encryptedPassword = textEncryptor.encrypt(plainPassword);

        String verificationToken = UUID.randomUUID().toString();

        emailVerificationRepository.findByEmail(email).ifPresent(emailVerificationRepository::delete);

        EmailVerification newVerification = new EmailVerification();
        newVerification.setEmail(email);
        newVerification.setName(name);
        newVerification.setCourse(course);
        newVerification.setPassword(encryptedPassword);
        newVerification.setCode(verificationToken); // Salva o token no campo 'code'
        newVerification.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        emailVerificationRepository.save(newVerification);

        emailService.sendAccountActivationEmail(email, verificationToken);
    }

    @Transactional
    public Employee verifyEmailCode(String token) {
        EmailVerification verification = emailVerificationRepository.findByCode(token)
                .filter(v -> v.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Token de ativação inválido ou expirado."));

        String email = verification.getEmail();

        if (employeeRepository.findByEmail(email).isPresent()) {
            emailVerificationRepository.delete(verification);
            throw new IllegalArgumentException("Email já associado com uma conta registrada.");
        }

        Employee newEmployee = new Employee();
        newEmployee.setName(verification.getName());
        newEmployee.setEmail(email);
        newEmployee.setCourse(verification.getCourse());
        newEmployee.setPassword(verification.getPassword());

        if (email.toLowerCase().endsWith("@cps.sp.gov.br")) {
            newEmployee.setRole(Role.COORDENADOR);
        } else {
            newEmployee.setRole(Role.PROFESSOR);
        }

        Employee savedEmployee = employeeRepository.save(newEmployee);
        emailVerificationRepository.delete(verification);

        return savedEmployee;
    }

    @Transactional
    public void sendPasswordResetToken(String email) {
        employeeRepository.findByEmail(email).ifPresent(employee -> {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, employee);
            passwordResetTokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(employee.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido ou expirado."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        Employee employee = resetToken.getEmployee();
        String encryptedPassword = textEncryptor.encrypt(newPassword);
        employee.setPassword(encryptedPassword);
        employeeRepository.save(employee);

        passwordResetTokenRepository.delete(resetToken);
    }

}