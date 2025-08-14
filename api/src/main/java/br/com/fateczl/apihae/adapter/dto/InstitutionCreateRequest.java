package br.com.fateczl.apihae.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionCreateRequest {
    private String name;
    private Integer institutionCode;
    private int haeQtd;
    private String address;
}
