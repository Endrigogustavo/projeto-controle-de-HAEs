package br.com.fateczl.apihae.adapter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateByDiretorOrAdmRequest {
    private String name;
    private String email;
    private String course;
    private String provisoryPassword;
    private Integer institutionCode;
}
