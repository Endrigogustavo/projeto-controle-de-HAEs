package br.com.fateczl.apihae.useCase.service.Auth;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.entity.PasswordResetToken;
import br.com.fateczl.apihae.driver.repository.EmailVerificationRepository;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.PasswordResetTokenRepository;
import br.com.fateczl.apihae.useCase.service.EmailService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import br.com.fateczl.apihae.domain.factory.EmailVerificationFactory;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;

@Service
@RequiredArgsConstructor
public class SenderEmailAuth {
    
    private final EmployeeRepository employeeRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final BasicTextEncryptor textEncryptor;
    private final InstitutionRepository institutionRepository;

    @Transactional
    public void sendVerificationCode(String name, String email, String course, String plainPassword,
            String institutionName) {
        String encryptedPassword = textEncryptor.encrypt(plainPassword);

        String verificationToken = UUID.randomUUID().toString();

        Optional<EmailVerification> existing = emailVerificationRepository.findByEmail(email);
        existing.ifPresent(verification -> {
            emailVerificationRepository.delete(verification);
            emailVerificationRepository.flush();
        });
      
        EmailVerification newVerification = EmailVerificationFactory.create(name, email, course, encryptedPassword,
                institutionName, verificationToken);

        Institution institution = institutionRepository.findByName(institutionName)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        newVerification.setInstitution(institution);

        emailVerificationRepository.save(newVerification);

        emailService.sendAccountActivationEmail(email, verificationToken, institution.getInstitutionCode());
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
}