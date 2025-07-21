package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.HaeRequest;
import br.com.fateczl.apihae.adapter.dto.HaeStatusUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.Student;
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

@RestController
@RequestMapping("/hae")
@SecurityRequirement(name = "cookieAuth") 
@Tag(name = "Hae", description = "Endpoints para manipular HAEs (Horas de Atividades Específicas)")
public class HaeController {

    private final HaeService haeService;

    public HaeController(HaeService haeService) {
        this.haeService = haeService;
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

//    @GetMapping("/getStudentsByHae/{haeId}")
//    public ResponseEntity<?> getStudentsByHae(@PathVariable String haeId) {
//        List<Student> students = haeService.getStudentsByHaeId(haeId);
//        return ResponseEntity.ok(students);
//    }

    @PostMapping("/sendEmailToCoordinatorAboutHAECreated/{coordinatorId}/{haeId}")
    public ResponseEntity<?> sendEmailToCoordinatorAboutHAECreated(@PathVariable String coordinatorId, @PathVariable String haeId) {
        haeService.sendEmailToCoordinatorAboutHAECreated(coordinatorId, haeId);
        return ResponseEntity.ok("Email enviado para o coordenador sobre a criação da HAE com ID: " + haeId);
    }

    @PostMapping("/createHaeAsCoordinator/{coordinatorId}/{employeeId}")
    public ResponseEntity<?> createHaeAsCoordinator(@PathVariable String coordinatorId, @PathVariable String employeeId, @Valid @RequestBody HaeRequest request) {
        Hae hae = haeService.createHaeAsCoordinator(coordinatorId, employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hae);
    }
}