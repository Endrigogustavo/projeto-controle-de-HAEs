package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.request.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.request.HaeStatusUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.request.HaeClosureRequest;
import br.com.fateczl.apihae.adapter.dto.response.HaeClosureRecordDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeDetailDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeHoursResponseDTO;
import br.com.fateczl.apihae.adapter.dto.response.HaeResponseDTO;
import br.com.fateczl.apihae.adapter.facade.HaeFacade;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hae")
@SecurityRequirement(name = "cookieAuth")
@RequiredArgsConstructor
@Tag(name = "Hae", description = "Endpoints para manipular HAEs (Horas de Atividades Específicas)")
public class HaeController {
    private final HaeFacade haeFacade;

    @PostMapping("/create")
    public ResponseEntity<Object> createHae(@Valid @RequestBody HaeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(haeFacade.createHae(request));
    }

    @GetMapping("/getHaeById/{id}")
    public ResponseEntity<HaeDetailDTO> getHaeById(@PathVariable String id) {
        return ResponseEntity.ok(haeFacade.getHaeById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHae(@PathVariable String id) {
        haeFacade.deleteHae(id);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "HAE deletado com sucesso."));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeId(@PathVariable String id) {
        return ResponseEntity.ok(haeFacade.getHaesByEmployeeId(id));
    }

    @GetMapping("/getHaesByEmployeeIdWithLimit/{employeeId}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeIdWithLimit(@PathVariable String employeeId) {
        return ResponseEntity.ok(haeFacade.getHaesByEmployeeIdWithLimit(employeeId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<HaeResponseDTO>> getAllHaes() {
        return ResponseEntity.ok(haeFacade.getAllHaes());
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Object> changeHaeStatus(@PathVariable String id,
            @Valid @RequestBody HaeStatusUpdateRequest request) {
        return ResponseEntity.ok(haeFacade.updateHaeStatus(id, request.getNewStatus(), request.getCoordenadorId()));
    }

    @GetMapping("/getHaesByProfessor/{professorId}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByProfessor(@PathVariable String professorId) {
        return ResponseEntity.ok(haeFacade.getHaesByProfessorId(professorId));
    }

    @GetMapping("/getHaesByCourse/{course}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByCourse(@PathVariable String course) {
        return ResponseEntity.ok(haeFacade.getHaesByCourse(course));
    }

    @GetMapping("/getHaesByType/{haeType}")
    public ResponseEntity<?> getHaesByType(@PathVariable HaeType haeType) {
        return ResponseEntity.ok(haeFacade.getHaesByType(haeType));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hae> updateHae(@PathVariable String id, @RequestBody HaeRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(haeFacade.updateHae(id, request));
    }

    @PostMapping("/createHaeAsCoordinator/{coordinatorId}/{employeeId}")
    public ResponseEntity<?> createHaeAsCoordinator(@PathVariable String coordinatorId, @PathVariable String employeeId,
            @Valid @RequestBody HaeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(haeFacade.createHaeAsCoordinator(coordinatorId, employeeId, request));
    }

    @Operation(summary = "Alternar status de visualização", description = "Alterna o status 'viewed' de uma HAE (de true para false e vice-versa).")
    @PutMapping("/viewed/toggle/{haeId}")
    public ResponseEntity<Hae> toggleViewed(@PathVariable String haeId) {
        try {
            return ResponseEntity.ok(haeFacade.toggleViewedStatus(haeId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Listar HAEs visualizadas", description = "Retorna todas as HAEs que já foram visualizadas")
    @GetMapping("/viewed")
    public ResponseEntity<List<Hae>> getViewed() {
        return ResponseEntity.ok(haeFacade.getViewed());
    }

    @Operation(summary = "Listar HAEs não visualizadas", description = "Retorna todas as HAEs que ainda não foram visualizadas")
    @GetMapping("/not-viewed")
    public ResponseEntity<List<Hae>> getNotViewed() {
        return ResponseEntity.ok(haeFacade.getNotViewed());
    }

    @Operation(summary = "Lista todas as HAEs de uma instituição", description = "Retorna uma lista de HAEs baseada no ID da instituição fornecido.")
    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByInstitution(@PathVariable String institutionId) {
        return ResponseEntity.ok(haeFacade.getHaesByInstitutionId(institutionId));
    }

    @Operation(summary = "Lista todas as HAEs baseado nos status", description = "Retorna uma lista de HAEs baseado no status fornecido.")
    @GetMapping("/getHaeByStatus/{status}")
    public ResponseEntity<List<HaeResponseDTO>> getHaesByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(haeFacade.getHaeByStatus(status));
    }

    @Operation(summary = "Busca avançada de HAEs", description = "Filtra HAEs por instituição, curso, tipo, status e visualização. Todos os filtros são opcionais.")
    @GetMapping("/search")
    public ResponseEntity<List<HaeResponseDTO>> searchHaes(
            @RequestParam(required = false) String institutionId,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) HaeType haeType,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Boolean viewed) {

        return ResponseEntity.ok(haeFacade.advancedHaeSearch(institutionId, course, haeType, status, viewed));
    }

    @GetMapping("/getAllWeeklyHours")
    public ResponseEntity<?> getAllWeeklyHours() {
        return ResponseEntity.ok(haeFacade.getAllWeeklyHours());
    }

    @GetMapping("/getWeeklyHoursByHae/{haeId}")
    public ResponseEntity<HaeHoursResponseDTO> getWeeklyHoursByHae(@PathVariable String haeId) {
        return ResponseEntity.ok(haeFacade.getWeeklyHoursByHae(haeId));
    }

    @Operation(summary = "Solicitar fechamento de HAE", description = "Envia as informações de fechamento de uma HAE baseado no tipo")
    @PostMapping("/request-closure/{haeId}")
    public ResponseEntity<Object> requestClosure(@PathVariable String haeId, @Valid @RequestBody HaeClosureRequest request) {
        return ResponseEntity.ok(haeFacade.requestClosure(haeId, request));
    }

    @Operation(summary = "Buscar histórico de fechamento de HAE", description = "Retorna todos os registros de fechamento aprovado de uma HAE")
    @GetMapping("/closure-records/{haeId}")
    public ResponseEntity<List<HaeClosureRecordDTO>> getClosureRecords(@PathVariable String haeId) {
        return ResponseEntity.ok(haeFacade.getClosureRecordsByHaeId(haeId));
    }

}