package br.com.fateczl.apihae.adapter.dto.response;

import br.com.fateczl.apihae.domain.entity.Institution;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstitutionResponseDTO {
    private String id;
    private String name;

    public InstitutionResponseDTO(Institution institution) {
        this.id = institution.getId();
        this.name = institution.getName();
    }
}
