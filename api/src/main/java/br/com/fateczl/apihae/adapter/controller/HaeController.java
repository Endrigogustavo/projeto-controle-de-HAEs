package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.request.HaeStatusUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.fateczl.apihae.domain.enums.Status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

import br.com.fateczl.apihae.useCase.service.Hae.ManageHae;
import br.com.fateczl.apihae.useCase.service.Hae.ShowHae;
import br.com.fateczl.apihae.useCase.service.Hae.StatusHae;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hae")
@SecurityRequirement(name = "cookieAuth")
@RequiredArgsConstructor
@Tag(name = "Hae", description = "Endpoints para manipular HAEs (Horas de Atividades Específicas)")
public class HaeController {

    private final ShowHae showHae;
    private final ManageHae manageHae;
    private final StatusHae statusHae;

    @PostMapping("/create")
    public ResponseEntity<Object> createHae(@Valid @RequestBody HaeRequest request) {
        Hae createdHae = manageHae.createHae(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHae);
    }

    @GetMapping("/getHaeById/{id}")
    public ResponseEntity<HaeDetailDTO> getHaeById(@PathVariable String id) {
        HaeDetailDTO hae = showHae.getHaeById(id);
        return ResponseEntity.ok(hae);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHae(@PathVariable String id) {
        manageHae.deleteHae(id);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "HAE deletado com sucesso."));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeId(@PathVariable String id) {
        List<Hae> haes = showHae.getHaesByEmployeeId(id);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByEmployeeIdWithLimit/{employeeId}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeIdWithLimit(@PathVariable String employeeId) {
        List<Hae> haes = showHae.getHaesByEmployeeIdWithLimit(employeeId);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<HaeResponseDTO>> getAllHaes() {
        List<HaeResponseDTO> haes = showHae.getAllHaes();
        return ResponseEntity.ok(haes);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Object> changeHaeStatus(@PathVariable String id,
            @Valid @RequestBody HaeStatusUpdateRequest request) {
        Hae updatedHae = statusHae.changeHaeStatus(id, request.getNewStatus(), request.getCoordenadorId());
        return ResponseEntity.ok(updatedHae);
    }

    @GetMapping("/getHaesByProfessor/{professorId}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByProfessor(@PathVariable String professorId) {
        List<HaeResponseDTO> haes = showHae.getHaesByProfessorId(professorId);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByCourse/{course}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByCourse(@PathVariable String course) {
        List<HaeResponseDTO> haes = showHae.getHaesByCourse(course);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByType/{haeType}")
    public ResponseEntity<?> getHaesByType(@PathVariable HaeType haeType) {
        List<Hae> haes = showHae.getHaesByType(haeType);
        return ResponseEntity.ok(haes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hae> updateHae(@PathVariable String id, @RequestBody HaeRequest request) {
        Hae createdHae = manageHae.updateHae(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(createdHae);
    }

    @PostMapping("/createHaeAsCoordinator/{coordinatorId}/{employeeId}")
    public ResponseEntity<?> createHaeAsCoordinator(@PathVariable String coordinatorId, @PathVariable String employeeId,
            @Valid @RequestBody HaeRequest request) {
        Hae hae = manageHae.createHaeAsCoordinator(coordinatorId, employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hae);
    }

    @Operation(summary = "Alternar status de visualização", description = "Alterna o status 'viewed' de uma HAE (de true para false e vice-versa).")
    @PutMapping("/viewed/toggle/{haeId}")
    public ResponseEntity<Hae> toggleViewed(@PathVariable String haeId) {
        try {
            Hae updatedHae = statusHae.toggleViewedStatus(haeId);
            return ResponseEntity.ok(updatedHae);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Listar HAEs visualizadas", description = "Retorna todas as HAEs que já foram visualizadas")
    @GetMapping("/viewed")
    public ResponseEntity<List<Hae>> getViewed() {
        return ResponseEntity.ok(statusHae.getHaeWasViewed());
    }

    @Operation(summary = "Listar HAEs não visualizadas", description = "Retorna todas as HAEs que ainda não foram visualizadas")
    @GetMapping("/not-viewed")
    public ResponseEntity<List<Hae>> getNotViewed() {
        return ResponseEntity.ok(statusHae.getHaeWasNotViewed());
    }

    @Operation(summary = "Lista todas as HAEs de uma instituição", description = "Retorna uma lista de HAEs baseada no ID da instituição fornecido.")
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByInstitution(@PathVariable String institutionId) {
        List<HaeResponseDTO> haes = showHae.getHaesByInstitutionId(institutionId);
        return ResponseEntity.ok(haes);
    }

    @Operation(summary = "Lista todas as HAEs baseado nos status", description = "Retorna uma lista de HAEs baseado no status fornecido.")
    @GetMapping("/getHaeByStatus/{status}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByStatus(@PathVariable Status status) {
        List<HaeResponseDTO> haes = showHae.getHaeByStatus(status);
        return ResponseEntity.ok(haes);
    }

    @Operation(summary = "Busca avançada de HAEs", description = "Filtra HAEs por instituição, curso, tipo, status e visualização. Todos os filtros são opcionais.")
    @GetMapping("/search")
    public ResponseEntity<List<HaeResponseDTO>> searchHaes(
            @RequestParam(required = false) String institutionId,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) HaeType haeType,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Boolean viewed) {

        List<HaeResponseDTO> haes = showHae.searchHaes(institutionId, course, haeType, status, viewed);
        return ResponseEntity.ok(haes);
    }

}