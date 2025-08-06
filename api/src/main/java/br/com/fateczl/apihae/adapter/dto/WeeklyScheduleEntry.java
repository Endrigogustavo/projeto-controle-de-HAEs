package br.com.fateczl.apihae.adapter.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class WeeklyScheduleEntry {
    @NotBlank(message = "O intervalo de tempo não pode estar em branco.")
    private String timeRange;
}