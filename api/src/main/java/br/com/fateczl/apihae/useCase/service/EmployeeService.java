package br.com.fateczl.apihae.useCase.service;

import br.com.fateczl.apihae.adapter.dto.EmployeeSummaryDTO;
import br.com.fateczl.apihae.adapter.dto.InstitutionDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Institution;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.driver.repository.EmployeeRepository;
import br.com.fateczl.apihae.driver.repository.HaeRepository;
import br.com.fateczl.apihae.driver.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.fateczl.apihae.adapter.dto.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.adapter.dto.EmployeeResponseDTO;
import br.com.fateczl.apihae.driver.repository.PasswordResetTokenRepository;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final HaeRepository haeRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
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
        return convertToDto(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
        return convertToDto(employee);
    }

    @Transactional
    public void deleteEmployeeAccount(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));
        passwordResetTokenRepository.deleteByEmployee(employee);
        employeeRepository.deleteById(id);
    }

    @Transactional
    public Employee updateEmployeeAccount(String id, String newName, String newEmail) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        if (newName != null && !newName.trim().isEmpty()) {
            employee.setName(newName.trim());
        }

        if (newEmail != null && !newEmail.trim().isEmpty()) {
            Optional<Employee> existingEmployeeWithEmail = employeeRepository.findByEmail(newEmail.trim());
            if (existingEmployeeWithEmail.isPresent() && !existingEmployeeWithEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email em uso por outra conta.");
            }
            employee.setEmail(newEmail.trim());
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee changeEmployeeRole(String id, Role newRole) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empregado não encontrado com ID: " + id));

        employee.setRole(newRole);
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee createEmployeeByDiretorOrAdmin(EmployeeCreateByDiretorOrAdmRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setCourse(request.getCourse());
        employee.setPassword(request.getProvisoryPassword());
        employee.setRole(Role.PROFESSOR);

        Institution institution = institutionRepository.findById(request.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        employee.setInstitution(institution);
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployeesByInstitutionId(String institutionId) {
        return employeeRepository.findByInstitutionId(institutionId);
    }

    private EmployeeResponseDTO convertToDto(Employee employee) {
        InstitutionDTO institutionDto = null;
        if (employee.getInstitution() != null) {
            institutionDto = new InstitutionDTO(
                    employee.getInstitution().getId().toString(),
                    employee.getInstitution().getName(),
                    employee.getInstitution().getInstitutionCode());
        }

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getCourse(),
                employee.getRole(),
                institutionDto);
    }
}
