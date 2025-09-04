package br.com.fateczl.apihae.adapter.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.adapter.dto.request.EmployeeUpdateRequest;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeSummaryDTO;
import br.com.fateczl.apihae.adapter.facade.EmployeeFacade;
import br.com.fateczl.apihae.domain.enums.Role;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Employee", description = "Endpoints para manipular os funcionários do sistema")
public class EmployeeController {
    private final EmployeeFacade employeeFacade;

    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeFacade.getAllEmployees());
    }

    @GetMapping("/getAllByRole/{role}")
    public ResponseEntity<List<EmployeeSummaryDTO>> getAllProfessoresByRole(@PathVariable("role") Role role) {
        return ResponseEntity.ok(employeeFacade.getEmployeeSummaries(role));
    }

    @GetMapping("/get-professor/{id}")
    public ResponseEntity<EmployeeResponseDTO> getProfessorById(@PathVariable("id") String id) {
        return ResponseEntity.ok(employeeFacade.getEmployeeById(id));
    }

    @GetMapping("/get-professor")
    public ResponseEntity<EmployeeResponseDTO> getProfessorByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(employeeFacade.getEmployeeByEmail(email));
    }

    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String id) {
        employeeFacade.deleteEmployeeById(id);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Conta deletada com sucesso."));
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable String id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        return ResponseEntity.ok(employeeFacade.updateEmployee(id, request.getName(), request.getEmail()));
    }

    @GetMapping("/get-my-user")
    public ResponseEntity<EmployeeResponseDTO> getMyUser(@RequestParam("email") String email) {
        return ResponseEntity.ok(employeeFacade.getMyUser(email));
    }

    @PostMapping("/createEmployeeByDiretorOrAdmin")
    public ResponseEntity<?> createEmployeeByDiretorOrAdmin(
            @Valid @RequestBody EmployeeCreateByDiretorOrAdmRequest request) {
        return ResponseEntity.ok(employeeFacade.createEmployeeByDiretorOrAdm(request));
    }
}