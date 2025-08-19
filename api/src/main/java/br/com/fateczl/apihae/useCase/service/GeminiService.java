package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.gemini.GeminiRequest;
import br.com.fateczl.apihae.adapter.dto.gemini.GeminiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.util.List;

import org.springframework.web.client.RestClientException;

@Service
public class GeminiService {

    @Value("${gemini.api_key}")
    private String API_KEY;

    @Value("${gemini.api_url}")
    private String API_URL;

    private String ENDPOINT; // ✅ Agora não é final

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        this.ENDPOINT = API_URL + "?key=" + API_KEY;
    }

    public String analyzeText(String prompt) {
        try {
            Thread.sleep(5000); // Evita erro 429
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        GeminiRequest.Content.Part part = new GeminiRequest.Content.Part(prompt);
        GeminiRequest.Content content = new GeminiRequest.Content("user", List.of(part));
        GeminiRequest request = new GeminiRequest(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                    ENDPOINT,
                    HttpMethod.POST,
                    entity,
                    GeminiResponse.class
            );

            return response.getBody()
                    .getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();

        } catch (RestClientException e) {
            return "Erro ao chamar a API Gemini: " + e.getMessage();
        }
    }
}
