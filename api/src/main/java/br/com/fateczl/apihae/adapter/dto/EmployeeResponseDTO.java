package br.com.fateczl.apihae.adapter.dto;

import br.com.fateczl.apihae.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private String id;
    private String name;
    private String email;
    private String course;
    private Role role;
    private InstitutionDTO institution;
}