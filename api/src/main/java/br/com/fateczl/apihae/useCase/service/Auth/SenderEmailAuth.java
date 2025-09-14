package br.com.fateczl.apihae.useCase.service.Auth;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.entity.PasswordResetToken;
import br.com.fateczl.apihae.useCase.Interface.IEmailVerificationRepository;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IInstitutionRepository;
import br.com.fateczl.apihae.useCase.Interface.IPasswordResetTokenRepository;
import br.com.fateczl.apihae.useCase.service.EmailService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import br.com.fateczl.apihae.domain.factory.EmailVerificationFactory;

@Service
@RequiredArgsConstructor
public class SenderEmailAuth {
    
    private final IEmployeeRepository iEmployeeRepository;
    private final IEmailVerificationRepository iEmailVerificationRepository;
    private final IPasswordResetTokenRepository iPasswordResetTokenRepository;
    private final EmailService emailService;
    private final BasicTextEncryptor textEncryptor;
    private final IInstitutionRepository iInstitutionRepository;

    @Transactional
    public void sendVerificationCode(String name, String email, String course, String plainPassword,
            String institutionName) {
        String encryptedPassword = textEncryptor.encrypt(plainPassword);

        String verificationToken = UUID.randomUUID().toString();

        Optional<EmailVerification> existing = iEmailVerificationRepository.findByEmail(email);
        existing.ifPresent(verification -> {
            iEmailVerificationRepository.delete(verification);
            iEmailVerificationRepository.flush();
        });
      
        EmailVerification newVerification = EmailVerificationFactory.create(name, email, course, encryptedPassword,
                institutionName, verificationToken);

        Institution institution = iInstitutionRepository.findByName(institutionName)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        newVerification.setInstitution(institution);

        iEmailVerificationRepository.save(newVerification);

        emailService.sendAccountActivationEmail(email, verificationToken, institution.getInstitutionCode());
    }

    @Transactional
    public void sendPasswordResetToken(String email) {
        iEmployeeRepository.findByEmail(email).ifPresent(employee -> {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, employee);
            iPasswordResetTokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(employee.getEmail(), token);
        });
    }
}