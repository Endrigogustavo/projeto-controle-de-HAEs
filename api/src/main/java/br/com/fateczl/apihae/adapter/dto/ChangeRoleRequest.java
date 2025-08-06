package br.com.fateczl.apihae.adapter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import br.com.fateczl.apihae.domain.enums.Role;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {
    @NotNull(message = "A nova função não pode ser nula.")
    private Role newRole;
}