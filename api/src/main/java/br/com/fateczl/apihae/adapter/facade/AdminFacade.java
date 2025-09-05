package br.com.fateczl.apihae.adapter.facade;


import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.useCase.service.Employee.ManageEmployee;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminFacade {
    private final ManageEmployee manageEmployee;

    public Employee changeEmployeeRole(String id, Role newRole) {
        return manageEmployee.changeEmployeeRole(id, newRole);
    }
}
