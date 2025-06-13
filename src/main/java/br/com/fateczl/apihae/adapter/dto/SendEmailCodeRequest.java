package br.com.fateczl.apihae.adapter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailCodeRequest {
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotBlank(message = "E-mail não pode estar em branco")
    @Email(message = "E-mail deve ser válido")
    private String email;

    @NotBlank(message = "Senha não pode estar em branco")
    @Size(min = 6, message = "Senha não pode ter menos de 6 caracteres")
    private String password;
}