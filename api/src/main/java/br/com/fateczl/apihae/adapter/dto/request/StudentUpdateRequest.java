package br.com.fateczl.apihae.adapter.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@fatec\\.sp\\.gov\\.br$", message = "O email deve ser do domínio da fatec (fatec.sp.gov.br)")
    private String email;

    @NotBlank(message = "O curso é obrigatório")
    private String course;

    @NotBlank(message = "O período é obrigatório")
    private String period;
}