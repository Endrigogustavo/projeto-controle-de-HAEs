package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.EmployeeUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.useCase.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/employee")
@SecurityRequirement(name = "cookieAuth") 
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/get-professor/{id}")
    public ResponseEntity<Object> getProfessorById(@PathVariable("id") String id) {
        Employee employee = employeeService.getEmployeeById(id);
        System.out.println(employee);
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
}
