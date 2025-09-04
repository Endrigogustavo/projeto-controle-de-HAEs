package br.com.fateczl.apihae.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.request.FeedbackRequest;
import br.com.fateczl.apihae.adapter.facade.FeedbackFacade;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackFacade feedbackFacade;

    @PostMapping
    public ResponseEntity<String> receberFeedback(@RequestBody FeedbackRequest feedback) throws MessagingException {
        feedbackFacade.sendFeedbackEmail(feedback);
        return ResponseEntity.ok("Feedback enviado com sucesso!");
    }
}
