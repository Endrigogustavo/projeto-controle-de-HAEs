package br.com.fateczl.apihae.adapter.controller;

import org.springframework.web.bind.annotation.*;

import br.com.fateczl.apihae.useCase.service.GeminiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/analyze")
    public String analyze(@RequestParam String prompt) {
        return geminiService.analyzeText(prompt);
    }
}
