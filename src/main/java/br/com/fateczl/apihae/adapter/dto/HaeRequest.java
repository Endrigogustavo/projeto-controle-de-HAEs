package br.com.fateczl.apihae.adapter.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.fateczl.apihae.domain.enums.HaeType;
import br.com.fateczl.apihae.domain.enums.Modality;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "O curso não pode estar em branco.")
    private String course;

    @NotNull(message = "O tipo de projeto não pode ser nulo.")
    private HaeType haeType;

    @NotNull(message = "A modalidade não pode ser nula.")
    private Modality modality;

    @NotNull(message = "A data de início não pode ser nula.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula.")
    @Future(message = "A data de término deve ser no futuro.")
    private LocalDate endDate;

    @NotBlank(message = "A descrição não pode estar em branco.")
    private String description;

    @NotNull(message = "A observação não pode ser nula.")
    @NotBlank(message = "A observação não pode estar em branco.")

    @NotBlank(message = "A observação não pode estar em branco.")
    private String observation;

    @NotBlank(message = "O dia da semana não pode estar em branco.")
    @NotNull(message = "O dia da semana não pode ser nulo.")
    private String dayOfWeek;

    @NotBlank(message = "O intervalo de tempo não pode estar em branco.")
    @NotNull(message = "O intervalo de tempo não pode ser nulo.")
    private String timeRange;

    @NotBlank(message = "O resultado alcançado não pode estar em branco.")
    @NotNull(message = "O resultado alcançado não pode ser nulo.")
    private String resultAchieved;

    @NotNull(message = "O cronograma não pode ser nulo.")
    @NotBlank(message = "O cronograma não pode estar em branco.")
    private String cronograma;

    @NotBlank(message = "A descrição do projeto não pode estar em branco.")
    @NotNull(message = "A descrição do projeto não pode ser nula.")
    private String projectDescription;

    @NotBlank(message = "O tipo de projeto não pode estar em branco.")
    @NotNull(message = "O tipo de projeto não pode ser nulo.")
    private String projectType;

    @NotNull(message = "A lista de RAs dos estudantes não pode ser nula.")
    @Size(min = 1, message = "Deve haver pelo menos um estudante na HAE.")
    private List<@NotBlank(message = "RA do estudante não pode estar em branco.") String> studentRas;

}