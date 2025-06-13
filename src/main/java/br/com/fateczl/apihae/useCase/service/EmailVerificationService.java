package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.driver.repository.EmailVerificationRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private String generateNumericCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public String generateAndSaveVerificationCode(String email, String name, String hashedPassword) {
        emailVerificationRepository.findByEmail(email).ifPresent(emailVerificationRepository::delete);

        String verificationCode = generateNumericCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        EmailVerification newVerification = new EmailVerification();
        newVerification.setEmail(email);
        newVerification.setName(name);
        newVerification.setPassword(hashedPassword);
        newVerification.setCode(verificationCode);
        newVerification.setExpiresAt(expiresAt);

        emailVerificationRepository.save(newVerification);

        sendVerificationEmail(email, verificationCode);

        return verificationCode;
    }

    private void sendVerificationEmail(String toEmail, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("Confirmação de E-mail - FATEC");

            String emailContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #ffffff;">
                  <div style="text-align: center; margin-bottom: 20px;">
                    <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo da FATEC" style="max-width: 200px;" />
                  </div>
                  <h2 style="color: #a6192e; text-align: center;">Confirmação de E-mail</h2>
                  <p style="color: #3d3d3d;">Olá!</p>
                  <p style="color: #3d3d3d;">
                    Você está recebendo este e-mail porque solicitou a criação de uma conta no sistema de Horas de Atividades Extracurriculares da
                    <strong style="color: #a6192e;">FATEC (Faculdade de Tecnologia do Estado de São Paulo)</strong>.
                  </p>
                  <p style="font-size: 18px; color: #3d3d3d;"><strong>Seu código de confirmação é:</strong></p>
                  <div style="text-align: center; margin: 20px 0;">
                    <span style="font-size: 28px; font-weight: bold; background-color: #a6192e; color: white; padding: 12px 24px; display: inline-block; border-radius: 6px;">
                      %s
                    </span>
                  </div>
                  <p style="color: #3d3d3d;">Este código é pessoal, intransferível e expira em alguns minutos por motivos de segurança.</p>
                  <p style="color: #3d3d3d;">Caso você não tenha solicitado este cadastro, por favor ignore esta mensagem.</p>
                  <br />
                  <p style="color: #3d3d3d;">Atenciosamente,</p>
                  <p style="color: #3d3d3d;">
                    <strong>Equipe Técnica da FATEC</strong><br />
                    <a href="https://www.fatec.sp.gov.br" target="_blank" style="color: #a6192e; text-decoration: none;">www.fatec.sp.gov.br</a>
                  </p>
                </div>
                """.formatted(code);

            helper.setText(emailContent, true);
            javaMailSender.send(message);
            System.out.println("E-mail de verificação enviado para: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail de verificação para " + toEmail + ": " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<EmailVerification> findValidVerification(String email, String code) {
        Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByEmailAndCode(email, code);

        if (verificationOpt.isPresent()) {
            EmailVerification verification = verificationOpt.get();
            if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
                emailVerificationRepository.delete(verification);
                return Optional.empty();
            }
            return Optional.of(verification);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteVerification(EmailVerification verification) {
        emailVerificationRepository.delete(verification);
    }
}
