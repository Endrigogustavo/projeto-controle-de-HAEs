package br.com.fateczl.apihae.adapter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "O token não pode estar em branco.")
    private String token;

    @NotBlank(message = "A nova senha não pode estar em branco.")
    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
    private String newPassword;

}