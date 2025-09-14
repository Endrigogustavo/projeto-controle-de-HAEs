package br.com.fateczl.apihae.adapter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.InstitutionResponseDTO;
import br.com.fateczl.apihae.adapter.facade.InstitutionFacade;
import br.com.fateczl.apihae.domain.entity.Institution;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/institution")
public class InstitutionController {
    private final InstitutionFacade institutionFacade;

    @PostMapping("/create")
    public ResponseEntity<?> createInstitution(@RequestBody InstitutionCreateRequest request) {
        institutionFacade.createInstitution(request);
        return ResponseEntity.ok("Instituição criada com sucesso!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Institution> updateInstitution(@PathVariable Integer id,
            @Valid @RequestBody InstitutionUpdateRequest request) {
        return ResponseEntity.ok(institutionFacade.updateInstitution(id, request));
    }

    @GetMapping("/getAvailableHaesCount")
    public ResponseEntity<Integer> getAvailableHaesCount(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionFacade.getAvailableHaesCount(institutionId));
    }

    @PostMapping("/setAvailableHaesCount")
    public ResponseEntity<?> setAvailableHaesCount(@RequestParam int count, @RequestParam String userId,
            @RequestParam String institutionId) {
        institutionFacade.setAvailableHaesCount(count, userId, institutionId);
        return ResponseEntity.ok("Quantidade de HAEs disponíveis atualizada para: " + count);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<InstitutionResponseDTO>> getAllInstitutions() {
        return ResponseEntity.ok(institutionFacade.getAllInstitutions());
    }

    @GetMapping("/getInstitutionById")
    public ResponseEntity<?> getInstitutionById(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionFacade.getInstitutionById(institutionId));
    }

    @GetMapping("/getInstitutionByInstitutionCode")
    public ResponseEntity<?> getInstitutionByInstitutionCode(@RequestParam Integer institutionCode) {
        return ResponseEntity.ok(institutionFacade.getInstitutionByInstitutionCode(institutionCode));
    }

    @GetMapping("/getEmployeesByInstitutionId")
    public ResponseEntity<?> getEmployeesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionFacade.getEmployeesByInstitutionId(institutionId));
    }

    @GetMapping("/getHaesByInstitutionId")
    public ResponseEntity<?> getHaesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(institutionFacade.getHaesByInstitutionId(institutionId));
    }

    @GetMapping("/getRemainingHours")
    public ResponseEntity<Integer> getRemainingHours(@RequestParam String institutionId) {
        int remainingHours = institutionFacade.getRemainingHours(institutionId);
        return ResponseEntity.ok(remainingHours);
    }
}
