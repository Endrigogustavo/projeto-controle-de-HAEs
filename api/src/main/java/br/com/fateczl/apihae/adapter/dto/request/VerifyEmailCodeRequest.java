package br.com.fateczl.apihae.adapter.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailCodeRequest {
    @NotBlank(message = "E-mail não pode estar em branco")
    @Email(message = "E-mail dever ser válido")
    private String email;

    @NotBlank(message = "Código não pode estar em branco")
    private String code;
}