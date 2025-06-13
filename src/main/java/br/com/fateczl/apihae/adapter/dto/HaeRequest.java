package br.com.fateczl.apihae.adapter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HaeRequest {
    @NotBlank(message = "O ID do funcionário não pode estar em branco.")
    private String employeeId;

    @NotBlank(message = "O título do projeto não pode estar em branco.")
    private String projectTitle;

    @NotNull(message = "As horas semanais não podem ser nulas.")
    @Min(value = 1, message = "As horas semanais devem ser no mínimo 1.")
    @Max(value = 40, message = "As horas semanais devem ser no máximo 40.")
    private Integer weeklyHours;

    @NotNull(message = "A data de início não pode ser nula.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula.")
    @Future(message = "A data de término deve ser no futuro.")
    private LocalDate endDate;

    private String observation;
}