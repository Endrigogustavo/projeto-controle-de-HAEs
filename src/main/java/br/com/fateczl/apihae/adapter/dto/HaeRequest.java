package br.com.fateczl.apihae.adapter.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private HaeType projectType;

    @NotNull(message = "A modalidade não pode ser nula.")
    private Modality modality;

    @NotNull(message = "A data de início não pode ser nula.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate startDate;

    @NotNull(message = "A data de término não pode ser nula.")
    @Future(message = "A data de término deve ser no futuro.")
    private LocalDate endDate;

    @NotBlank(message = "A observação não pode estar em branco.")
    private String observation;

    @NotNull(message = "O(s) dia(s) da semana não pode(m) ser nulo(s).")
    @Size(min = 1, message = "Pelo menos um dia da semana deve ser informado.")
    private List<String> dayOfWeek;

    @NotBlank(message = "O intervalo de tempo não pode estar em branco.")
    private String timeRange;

    @NotBlank(message = "A descrição do projeto não pode estar em branco.")
    private String projectDescription;

    @NotNull(message = "O cronograma semanal não pode ser nulo.")
    @Size(min = 1, message = "Pelo menos um dia do cronograma deve ser informado.")
    private Map<String, WeeklyScheduleEntry> weeklySchedule;

    @Size(min = 1, message = "Deve haver pelo menos um estudante na HAE.")
    private List<String> studentRAs;
}