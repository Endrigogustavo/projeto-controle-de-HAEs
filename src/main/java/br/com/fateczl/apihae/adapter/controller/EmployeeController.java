package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.EmployeeUpdateRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.useCase.service.EmployeeService;
import br.com.fateczl.apihae.useCase.utils.TokenUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/employee")
@SecurityRequirement(name = "cookieAuth") 
@Tag(name = "Employee", description = "Endpoints para manipular os funcionários do sistema")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TokenUtils TokenService;

    public EmployeeController(EmployeeService employeeService, TokenUtils TokenService) {
        this.employeeService = employeeService;
        this.TokenService = TokenService;        
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

    @GetMapping("/get-my-user")
    public ResponseEntity<?> getMyUser(@CookieValue(value = "auth_token", required = false) String authToken) {
        try {
            Employee employee = employeeService.findMyEmployee(authToken);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao buscar funcionário: " + e.getMessage());
        }
    }
}