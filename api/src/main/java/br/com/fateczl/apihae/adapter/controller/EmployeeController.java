package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.EmployeeSummaryDTO;
import br.com.fateczl.apihae.adapter.dto.EmployeeUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.useCase.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import br.com.fateczl.apihae.adapter.dto.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.adapter.dto.EmployeeResponseDTO;

@RestController
@RequestMapping("/employee")
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Employee", description = "Endpoints para manipular os funcionários do sistema")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getAllByRole/{role}")
    public ResponseEntity<List<EmployeeSummaryDTO>> getAllProfessoresByRole(@PathVariable("role") Role role) {
        List<EmployeeSummaryDTO> summaries = employeeService.getEmployeeSummaries(role);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/get-professor/{id}")
    public ResponseEntity<EmployeeResponseDTO> getProfessorById(@PathVariable("id") String id) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/get-professor")
    public ResponseEntity<EmployeeResponseDTO> getProfessorByEmail(@RequestParam("email") String email) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String id) {
        employeeService.deleteEmployeeAccount(id);
        return ResponseEntity.ok(Collections.singletonMap("mensagem", "Conta deletada com sucesso."));
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable String id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        Employee updatedEmployee = employeeService.updateEmployeeAccount(id, request.getName(), request.getEmail());
        return ResponseEntity.ok(updatedEmployee);
    }

    @GetMapping("/get-my-user")
    public ResponseEntity<EmployeeResponseDTO> getMyUser(@RequestParam("email") String email) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/createEmployeeByDiretorOrAdmin")
    public ResponseEntity<?> createEmployeeByDiretorOrAdmin(
            @Valid @RequestBody EmployeeCreateByDiretorOrAdmRequest request) {
        Employee newEmployee = employeeService.createEmployeeByDiretorOrAdmin(request);
        return ResponseEntity.ok(newEmployee);
    }
}