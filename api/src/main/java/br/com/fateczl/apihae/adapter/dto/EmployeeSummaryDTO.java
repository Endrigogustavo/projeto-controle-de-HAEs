package br.com.fateczl.apihae.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSummaryDTO {
    private String id;
    private String name;
    private String email;
    private String course;
    private int haeCount;
}