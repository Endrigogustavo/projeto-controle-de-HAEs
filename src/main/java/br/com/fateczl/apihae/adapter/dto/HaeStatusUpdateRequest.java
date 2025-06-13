package br.com.fateczl.apihae.adapter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import br.com.fateczl.apihae.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HaeStatusUpdateRequest {
    @NotNull(message = "O novo status não pode ser nulo.")
    private Status newStatus;

    @NotBlank(message = "O ID do coordenador não pode estar em branco.")
    private String coordenadorId;
}