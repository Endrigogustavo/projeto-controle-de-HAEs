package br.com.fateczl.apihae.useCase.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Value("${frontend.reset.url}")
  private String frontendResetUrl;

  @Value("${frontend.activation.url}")
  private String frontendActivationUrl;

  /**
   * 
   * @param toEmail O e-mail do destinatário.
   * @param token   O token de ativação gerado.
   */
  public void sendAccountActivationEmail(String toEmail, String token) {
    String subject = "Ativação de Conta - Sistema de HAEs FATEC";
    String activationLink = frontendActivationUrl + "?token=" + token;
    String emailContent = buildActivationEmailTemplate(activationLink);
    try {
      sendEmail(toEmail, subject, emailContent);
      System.out.println("E-mail de ativação enviado para: " + toEmail);
    } catch (MessagingException e) {
      System.err.println("Erro ao enviar e-mail de ativação para " + toEmail + ": " + e.getMessage());
    }
  }

  /**
   * Envia o e-mail com o link de redefinição de senha.
   * 
   * @param toEmail O e-mail do destinatário.
   * @param token   O token de redefinição gerado.
   */
  public void sendPasswordResetEmail(String toEmail, String token) {
    String subject = "Redefinição de Senha - Sistema de HAEs FATEC";
    String resetLink = frontendResetUrl + "?token=" + token;
    String emailContent = buildPasswordResetEmailTemplate(resetLink);

    try {
      sendEmail(toEmail, subject, emailContent);
      System.out.println("E-mail de redefinição de senha enviado para: " + toEmail);
    } catch (MessagingException e) {
      System.err.println("Erro ao enviar e-mail de redefinição para " + toEmail + ": " + e.getMessage());
    }
  }

  private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(senderEmail);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);
    javaMailSender.send(message);
  }

  private String buildActivationEmailTemplate(String activationLink) {
    return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
          <div style="text-align: center; margin-bottom: 20px;">
            <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo FATEC" style="max-width: 200px;" />
          </div>
          <h2 style="color: #a6192e; text-align: center;">Ativação de Conta</h2>
          <p>Bem-vindo ao sistema de HAEs da FATEC! Para concluir seu cadastro, por favor, clique no botão abaixo.</p>
          <div style="text-align: center; margin: 20px 0;">
            <a href="%s" target="_blank" style="font-size: 16px; background-color: #a6192e; color: white; padding: 12px 24px; text-decoration: none; display: inline-block; border-radius: 6px;">
              Ativar Minha Conta
            </a>
          </div>
          <p>Este link expira em 15 minutos. Se você não solicitou este cadastro, ignore este e-mail.</p>
        </div>
        """
        .formatted(activationLink);
  }

  private String buildPasswordResetEmailTemplate(String resetLink) {
    return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
          <div style="text-align: center; margin-bottom: 20px;">
            <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo FATEC" style="max-width: 200px;" />
          </div>
          <h2 style="color: #a6192e; text-align: center;">Redefinição de Senha</h2>
          <p>Você solicitou a redefinição da sua senha no sistema de HAEs da FATEC.</p>
          <p><strong>Clique no link abaixo para criar uma nova senha:</strong></p>
          <div style="text-align: center; margin: 20px 0;">
            <a href="%s" target="_blank" style="font-size: 16px; background-color: #a6192e; color: white; padding: 12px 24px; text-decoration: none; display: inline-block; border-radius: 6px;">
              Redefinir Minha Senha
            </a>
          </div>
          <p>Este link expira em 1 hora. Se você não solicitou isso, ignore este e-mail.</p>
        </div>
        """
        .formatted(resetLink);
  }
}