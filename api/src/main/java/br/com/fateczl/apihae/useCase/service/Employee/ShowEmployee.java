package br.com.fateczl.apihae.useCase.service.Employee;

import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeSummaryDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShowEmployee {

    private final EmployeeRepository employeeRepository;
    private final HaeRepository haeRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        
        return employees.stream()
            .map(EmployeeResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryDTO> getEmployeeSummaries(Role role) {
        List<Employee> professors = employeeRepository.findAllByRole(role);

        return professors.stream().map(professor -> {
            int haeCount = haeRepository.countByEmployeeId(professor.getId());

            return new EmployeeSummaryDTO(
                    professor.getId(),
                    professor.getName(),
                    professor.getEmail(),
                    professor.getCourse(),
                    haeCount);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com email " + email));
        return ConvertDTOEmployee.convertToDto(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
        return ConvertDTOEmployee.convertToDto(employee);
    }

    public List<Employee> getEmployeesByInstitutionId(String institutionId) {
        return employeeRepository.findByInstitutionId(institutionId);
    }
}
