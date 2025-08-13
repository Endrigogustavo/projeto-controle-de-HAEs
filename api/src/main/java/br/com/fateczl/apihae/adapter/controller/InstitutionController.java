package br.com.fateczl.apihae.adapter.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.InstitutionCreateRequest;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.useCase.service.InstitutionService;

@RestController
@RequestMapping("/institution")
public class InstitutionController {
    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createInstitution(InstitutionCreateRequest request) {
        institutionService.createInstitution(request);
        return ResponseEntity.ok("Instituição criada com sucesso!");
    }

    @GetMapping("/getAvailableHaesCount")
    public ResponseEntity<Integer> getAvailableHaesCount(@RequestParam String institutionId) {
        int count = institutionService.getHaeQtd(institutionId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/setAvailableHaesCount")
    public ResponseEntity<?> setAvailableHaesCount(@RequestParam int count, @RequestParam String userId,
            @RequestParam String institutionId) {
        institutionService.setHaeQtd(count, userId, institutionId);
        return ResponseEntity.ok("Quantidade de HAEs disponíveis atualizada para: " + count);
    }

    @GetMapping("/getAllInstitutions")
    public ResponseEntity<?> getAllInstitutions() {
        return ResponseEntity.ok(institutionService.listAllInstitutions());
    }

    @GetMapping("/getInstitutionById")
    public ResponseEntity<?> getInstitutionById(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionService.getInstitutionById(institutionId));
    }

    @GetMapping("/getEmployeesByInstitutionId")
    public ResponseEntity<?> getEmployeesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionService.getEmployeesByInstitutionId(institutionId));
    }

    @GetMapping("/getHaesByInstitutionId")
    public ResponseEntity<?> getHaesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionService.getHaesByInstitutionId(institutionId));
    }
}
