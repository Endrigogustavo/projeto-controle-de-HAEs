package br.com.fateczl.apihae.adapter.facade;

import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.adapter.dto.request.FeedbackRequest;
import br.com.fateczl.apihae.useCase.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FeedbackFacade {
    private final EmailService emailService;

    public void sendFeedbackEmail(FeedbackRequest feedback) throws MessagingException {
        String corpoEmail = emailService.buildFeedbackEmailTemplate(feedback);
        emailService.sendEmailFeedback("fateczlhae@gmail.com", "Novo Feedback sobre o Sistema de HAE", corpoEmail);
    }
}
