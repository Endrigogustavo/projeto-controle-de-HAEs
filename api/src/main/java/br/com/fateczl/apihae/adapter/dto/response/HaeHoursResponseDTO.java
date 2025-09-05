package br.com.fateczl.apihae.adapter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HaeHoursResponseDTO {
    private String haeId;
    private int weeklyHours;
}
