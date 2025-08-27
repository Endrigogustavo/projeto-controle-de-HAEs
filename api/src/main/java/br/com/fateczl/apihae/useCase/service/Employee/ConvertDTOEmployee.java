package br.com.fateczl.apihae.useCase.service.Employee;

import br.com.fateczl.apihae.adapter.dto.request.InstitutionRequestDTO;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.domain.entity.Employee;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConvertDTOEmployee {

    public static EmployeeResponseDTO convertToDto(Employee employee) {
        InstitutionRequestDTO institutionDto = null;
        if (employee.getInstitution() != null) {
            institutionDto = new InstitutionRequestDTO(
                    employee.getInstitution().getId(),
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
