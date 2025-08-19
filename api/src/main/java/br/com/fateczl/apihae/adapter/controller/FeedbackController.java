package br.com.fateczl.apihae.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.request.FeedbackRequest;
import br.com.fateczl.apihae.useCase.service.EmailService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final EmailService emailService;

    public FeedbackController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> receberFeedback(@RequestBody FeedbackRequest feedback) throws MessagingException {
        String corpoEmail = emailService.buildFeedbackEmailTemplate(feedback);
        emailService.sendEmailFeedback("fateczlhae@gmail.com", "Novo Feedback sobre o Sistema de HAE", corpoEmail);
        return ResponseEntity.ok("Feedback enviado com sucesso!");
    }
}
