package br.com.fateczl.apihae.adapter.dto.request;

import br.com.fateczl.apihae.domain.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionRequestDTO {
    private String id;
    private String name;
    private Integer institutionCode;

    public InstitutionRequestDTO(Institution institution) {
        this.id = institution.getId();
        this.name = institution.getName();
    }
}