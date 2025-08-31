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
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.useCase.service.Institution.ManageInstitution;
import br.com.fateczl.apihae.useCase.service.Institution.ShowInstitution;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/institution")
public class InstitutionController {
    private final ManageInstitution manageInstitution;
    private final ShowInstitution showInstitution;

    @PostMapping("/create")
    public ResponseEntity<?> createInstitution(@RequestBody InstitutionCreateRequest request) {
        manageInstitution.createInstitution(request);
        return ResponseEntity.ok("Instituição criada com sucesso!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Institution> updateInstitution(@PathVariable Integer id,
            @Valid @RequestBody InstitutionUpdateRequest request) {
        Institution updatedInstitution = manageInstitution.updateInstitution(id, request);
        return ResponseEntity.ok(updatedInstitution);
    }

    @GetMapping("/getAvailableHaesCount")
    public ResponseEntity<Integer> getAvailableHaesCount(@RequestParam String institutionId) {
        int count = showInstitution.getHaeQtd(institutionId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/setAvailableHaesCount")
    public ResponseEntity<?> setAvailableHaesCount(@RequestParam int count, @RequestParam String userId,
            @RequestParam String institutionId) {
        manageInstitution.setHaeQtd(count, userId, institutionId);
        return ResponseEntity.ok("Quantidade de HAEs disponíveis atualizada para: " + count);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<InstitutionResponseDTO>> getAllInstitutions() {
        return ResponseEntity.ok(showInstitution.listAllInstitutions());
    }

    @GetMapping("/getInstitutionById")
    public ResponseEntity<?> getInstitutionById(@RequestParam String institutionId) {
        return ResponseEntity.ok(showInstitution.getInstitutionById(institutionId));
    }

    @GetMapping("/getEmployeesByInstitutionId")
    public ResponseEntity<?> getEmployeesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(showInstitution.getEmployeesByInstitutionId(institutionId));
    }

    @GetMapping("/getHaesByInstitutionId")
    public ResponseEntity<?> getHaesByInstitutionId(@RequestParam String institutionId) {
        return ResponseEntity.ok(showInstitution.getHaesByInstitutionId(institutionId));
    }
}
