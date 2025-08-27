package br.com.fateczl.apihae.adapter.controller;

import br.com.fateczl.apihae.adapter.dto.request.ChangeRoleRequest;
import br.com.fateczl.apihae.domain.entity.Employee;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fateczl.apihae.useCase.service.Employee.ManageEmployee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "cookieAuth") 
@Tag(name = "Admin", description = "Endpoints para administração do sistema")
public class AdminController {

    private final ManageEmployee manageEmployee;



    /**
     * PUT /admin/change-role/{id}
     * Altera a função (Role) de um Employee (professor/coordenador).
     * Esta rota deve ser restrita apenas a usuários com privilégios de
     * administrador.
     * 
     * @param id      ID do Employee cuja função será alterada.
     * @param request DTO contendo a nova função.
     * @return ResponseEntity com status 200 OK e o Employee atualizado, ou 400 Bad
     *         Request / 404 Not Found.
     */
    @PutMapping("/change-role/{id}")
    public ResponseEntity<Object> changeEmployeeRole(@PathVariable String id,
            @Valid @RequestBody ChangeRoleRequest request) {
        Employee updatedEmployee = manageEmployee.changeEmployeeRole(id, request.getNewRole());
        return ResponseEntity.ok(updatedEmployee);
    }

    
}
