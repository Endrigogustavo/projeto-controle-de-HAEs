package br.com.fateczl.apihae.useCase.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import br.com.fateczl.apihae.adapter.dto.FeedbackRequest;
import br.com.fateczl.apihae.domain.entity.EmailVerification;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.driver.repository.EmailVerificationRepository;

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
  public void sendAccountActivationEmail(String toEmail, String token, String institutionId) {
    String subject = "Ativação de Conta - Sistema de HAEs FATEC";
    String activationLink = String.format("%s?token=%s&institutionId=%s", frontendActivationUrl, token, institutionId);
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

  public void sendAlertCoordenadorEmail(String toEmail, Hae hae) {
    String subject = "Alerta - Sistema de HAEs FATEC";
    String emailContent = buildAlertCoordenadorEmailTemplate(hae);
    try {
      sendEmail(toEmail, subject, emailContent);
      System.out.println("E-mail de alerta enviado para: " + toEmail);
    } catch (MessagingException e) {
      System.err.println("Erro ao enviar e-mail de alerta para " + toEmail + ": " + e.getMessage());
    }
  }

  /**
   * Envia um e-mail de alerta para o professor com uma mensagem específica.
   * 
   * @param toEmail      O e-mail do professor destinatário.
   * @param alertMessage A mensagem de alerta a ser enviada.
   */

  public void sendAlertProfessorHaeStatusEmail(String toEmail, Hae hae) {
    String subject = "Alerta - Sistema de HAEs FATEC";
    String emailContent = buildAlertProfessorHaeStatusEmailTemplate(hae);
    try {
      sendEmail(toEmail, subject, emailContent);
      System.out.println("E-mail de alerta enviado para: " + toEmail);
    } catch (MessagingException e) {
      System.err.println("Erro ao enviar e-mail de alerta para " + toEmail + ": " + e.getMessage());
    }
  }

  public void sendEmailFeedback(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    javaMailSender.send(message);
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

  public String buildFeedbackEmailTemplate(FeedbackRequest feedback) {
    return """
        <div style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;">
          <div style="max-width: 650px; margin: auto; background-color: #ffffff; padding: 25px 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">

            <div style="text-align: center; margin-bottom: 20px;">
              <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png"
                   alt="Logo FATEC" style="max-width: 180px;" />
            </div>

            <h2 style="color: #a6192e; text-align: center; margin-bottom: 10px;">📝 Novo Feedback Recebido</h2>
            <p style="text-align: center; font-size: 15px; color: #555; margin-bottom: 30px;">
              Um usuário enviou um novo feedback através do sistema de HAEs.
            </p>

            <div style="font-size: 15px; color: #333;">
              <p><strong>👤 Nome:</strong> %s</p>
              <p><strong>📧 E-mail:</strong> <a href="mailto:%s" style="color: #a6192e;">%s</a></p>
              <p><strong>💬 Mensagem:</strong></p>
              <div style="background-color: #fafafa; padding: 15px; border-left: 4px solid #a6192e; border-radius: 5px; white-space: pre-line;">
                %s
              </div>
            </div>

            <p style="text-align: center; font-size: 13px; color: #999; margin-top: 40px;">
              Este e-mail foi gerado automaticamente pelo sistema de HAEs FATEC.
            </p>
          </div>
        </div>
        """
        .formatted(
            feedback.getName(),
            feedback.getEmail(),
            feedback.getEmail(),
            feedback.getDescription());
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

  private String buildAlertCoordenadorEmailTemplate(Hae hae) {
    return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
          <div style="text-align: center; margin-bottom: 20px;">
            <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo FATEC" style="max-width: 200px;" />
          </div>
          <h2 style="color: #a6192e; text-align: center;">Nova HAE Criada</h2>
          <p><strong>Informações:</strong></p>
          <ul>
            <li><strong>Professor:</strong> %s</li>
            <li><strong>Curso:</strong> %s</li>
            <li><strong>Título do Projeto:</strong> %s</li>
            <li><strong>Tipo de Projeto:</strong> %s</li>
            <li><strong>Modalidade:</strong> %s</li>
            <li><strong>Período:</strong> %s até %s</li>
            <li><strong>Carga Horária Semanal:</strong> %d horas</li>
            <li><strong>Horários:</strong><br/>%s</li>
            <li><strong>Descrição:</strong> %s</li>
          </ul>
        </div>
        """
        .formatted(
            hae.getNameEmployee(),
            hae.getCourse(),
            hae.getProjectTitle(),
            hae.getProjectType(),
            hae.getModality(),
            hae.getStartDate(),
            hae.getEndDate(),
            hae.getWeeklyHours(),
            formatWeeklySchedule(hae.getWeeklySchedule()),
            hae.getProjectDescription());
  }

  private String buildAlertProfessorHaeStatusEmailTemplate(Hae hae) {
    return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;">
          <div style="text-align: center; margin-bottom: 20px;">
            <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo FATEC" style="max-width: 200px;" />
          </div>
          <h2 style="color: #a6192e; text-align: center;">Sua HAE foi %s</h2>
          <p><strong>Informações:</strong></p>
          <ul>
            <li><strong>Título do Projeto:</strong> %s</li>
            <li><strong>Tipo:</strong> %s</li>
            <li><strong>Modalidade:</strong> %s</li>
            <li><strong>Período:</strong> %s até %s</li>
            <li><strong>Carga Horária Semanal:</strong> %d horas</li>
            <li><strong>Horários:</strong><br/>%s</li>
          </ul>
          <p>Obrigado por utilizar o sistema de HAEs da FATEC.</p>
        </div>
        """
        .formatted(
            hae.getStatus().name(),
            hae.getProjectTitle(),
            hae.getProjectType(),
            hae.getModality(),
            hae.getStartDate(),
            hae.getEndDate(),
            hae.getWeeklyHours(),
            formatWeeklySchedule(hae.getWeeklySchedule()));
  }

  private String formatWeeklySchedule(Map<String, String> schedule) {
    if (schedule == null || schedule.isEmpty())
      return "Não informado";
    StringBuilder sb = new StringBuilder();
    schedule.forEach((dia, horario) -> sb.append("<li>").append(dia).append(": ").append(horario).append("</li>"));
    return "<ul>" + sb.toString() + "</ul>";
  }

}