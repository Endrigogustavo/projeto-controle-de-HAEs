package br.com.fateczl.apihae.adapter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {
    private String id;
    private String name;
    private Integer institutionCode;
}