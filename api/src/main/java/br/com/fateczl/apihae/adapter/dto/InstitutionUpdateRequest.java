package br.com.fateczl.apihae.adapter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InstitutionUpdateRequest {

    @NotNull(message = "O código da instituição é obrigatório")
    @Positive(message = "O código da instituição deve ser um número positivo")
    private Integer institutionCode;

    @NotBlank(message = "O nome da instituição é obrigatório")
    private String name;

    @NotBlank(message = "O endereço é obrigatório")
    private String address;

    @NotNull(message = "O limite de HAEs é obrigatório")
    @Min(value = 0, message = "O limite não pode ser negativo")
    private Integer haeQtd;
}