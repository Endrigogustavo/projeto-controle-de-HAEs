package br.com.fateczl.apihae.adapter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailCodeRequest {
    @NotBlank(message = "E-mail não pode estar em branco")
    @Email(message = "E-mail dever ser válido")
    private String email;

    @NotBlank(message = "Código não pode estar em branco")
    @Size(min = 6, max = 6, message = "Código deve ter exatamente 6 caracteres")
    private String code;
}