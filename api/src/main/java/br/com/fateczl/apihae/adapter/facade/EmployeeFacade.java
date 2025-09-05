package br.com.fateczl.apihae.adapter.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeSummaryDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.useCase.service.Employee.ManageEmployee;
import br.com.fateczl.apihae.useCase.service.Employee.ShowEmployee;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmployeeFacade {
    private final ShowEmployee showEmployee;
    private final ManageEmployee manageEmployee;
    
    public List<EmployeeResponseDTO> getAllEmployees() {
        return showEmployee.getAllEmployees();
    }
    
    public List<EmployeeSummaryDTO> getEmployeeSummaries(Role role) {
        return showEmployee.getEmployeeSummaries(role);
    }

    public EmployeeResponseDTO getEmployeeById(String id) {
        return showEmployee.getEmployeeById(id);
    }

    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        return showEmployee.getEmployeeByEmail(email);
    }

    public void deleteEmployeeById(String id) {
        manageEmployee.deleteEmployeeAccount(id);
    }

    public Employee updateEmployee(String id, String name, String email) {
        return manageEmployee.updateEmployeeAccount(id, name, email);
    }

    public EmployeeResponseDTO getMyUser(String email) {
        return showEmployee.getEmployeeByEmail(email);
    }

    public Employee createEmployeeByDiretorOrAdm(EmployeeCreateByDiretorOrAdmRequest request) {
        return manageEmployee.createEmployeeByDiretorOrAdmin(request);
    }
}
