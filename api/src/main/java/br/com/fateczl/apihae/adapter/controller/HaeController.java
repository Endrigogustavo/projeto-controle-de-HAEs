package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.HaeStatusUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.useCase.service.HaeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

import br.com.fateczl.apihae.domain.entity.HaeQtd;

@RestController
@RequestMapping("/hae")
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Hae", description = "Endpoints para manipular HAEs (Horas de Atividades Específicas)")
public class HaeController {

    private final HaeService haeService;

    private final HaeQtd haeQtd;

    private final HaeCacheTransientService haeCacheTransientService;

    public HaeController(HaeService haeService, HaeQtd haeQtd, HaeCacheTransientService haeCacheTransientService) {
        this.haeService = haeService;
        this.haeQtd = haeQtd;
        this.haeCacheTransientService = haeCacheTransientService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createHae(@Valid @RequestBody HaeRequest request) {
        Hae createdHae = haeService.createHae(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHae);
    }

    @GetMapping("/getHaeById/{id}")
    public ResponseEntity<Object> getHaeById(@PathVariable String id) {
        Hae hae = haeService.getHaeById(id);
        return ResponseEntity.ok(hae);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHae(@PathVariable String id) {
        haeService.deleteHae(id);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "HAE deletado com sucesso."));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeId(@PathVariable String id) {
        List<Hae> haes = haeService.getHaesByEmployeeId(id);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByEmployeeIdWithLimit/{employeeId}")
    public ResponseEntity<List<Hae>> getHaesByEmployeeIdWithLimit(@PathVariable String employeeId) {
        List<Hae> haes = haeService.getHaesByEmployeeIdWithLimit(employeeId);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Hae>> getAllHaes() {
        List<Hae> haes = haeService.getAllHaes();
        return ResponseEntity.ok(haes);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<Object> changeHaeStatus(@PathVariable String id,
            @Valid @RequestBody HaeStatusUpdateRequest request) {
        Hae updatedHae = haeService.changeHaeStatus(id, request.getNewStatus(), request.getCoordenadorId());
        return ResponseEntity.ok(updatedHae);
    }

    @GetMapping("/getHaesByProfessor/{professorId}")
    public ResponseEntity<?> getHaesByProfessor(@PathVariable String professorId) {
        List<Hae> haes = haeService.getHaesByProfessorId(professorId);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByCourse/{course}")
    public ResponseEntity<?> getHaesByCourse(@PathVariable String course) {
        List<Hae> haes = haeService.getHaesByCourse(course);
        return ResponseEntity.ok(haes);
    }

    @GetMapping("/getHaesByType/{haeType}")
    public ResponseEntity<?> getHaesByType(@PathVariable HaeType haeType) {
        List<Hae> haes = haeService.getHaesByType(haeType);
        return ResponseEntity.ok(haes);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hae> updateHae(@PathVariable String id, @RequestBody HaeRequest request) {
        Hae createdHae = haeService.updateHae(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(createdHae);
    }

    @PostMapping("/sendEmailToCoordinatorAboutHAECreated/{coordinatorId}/{haeId}")
    public ResponseEntity<?> sendEmailToCoordinatorAboutHAECreated(@PathVariable String coordinatorId,
            @PathVariable String haeId) {
        haeService.sendEmailToCoordinatorAboutHAECreated(coordinatorId, haeId);
        
        return ResponseEntity.ok("Email enviado para o coordenador sobre a criação da HAE com ID: " + haeId);
    }

    @PostMapping("/createHaeAsCoordinator/{coordinatorId}/{employeeId}")
    public ResponseEntity<?> createHaeAsCoordinator(@PathVariable String coordinatorId, @PathVariable String employeeId,
            @Valid @RequestBody HaeRequest request) {
        Hae hae = haeService.createHaeAsCoordinator(coordinatorId, employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hae);
    }

    @GetMapping("/getAvailableHaesCount")
    public ResponseEntity<Integer> getAvailableHaesCount() {
        int count = haeQtd.getQuantidade();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/setAvailableHaesCount")
    public ResponseEntity<?> setAvailableHaesCount(@RequestParam int count, @RequestParam String usuarioId) {
        haeQtd.setQuantidade(count, usuarioId);
        return ResponseEntity.ok("Quantidade de HAEs disponíveis atualizada para: " + count);
    }

    @PostMapping("/{id}/visualizeHae")
    public String visualizeHae(@PathVariable String id) {
        haeCacheTransientService.markAsViewed(id);
        return "Documento " + id + " marcado como visualizado";
    }

    @GetMapping("/{id}/statusViewed")
    public boolean statusHae(@PathVariable String id) {
        return haeCacheTransientService.wasViewed(id);
    }

    @GetMapping("/getAllHaeWithoutViewed")
    public List<Hae> getAllHaeWithoutViewed() {
        return haeCacheTransientService.getAllUnviewed();
    }
}