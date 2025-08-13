package br.com.fateczl.apihae.useCase.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataUtils {

    /**
     * Função auxiliar para determinar o semestre de uma data.
     *
     * @param date Data no formato LocalDate ou LocalDateTime.
     * @return Uma string no formato "AAAA/S" (ex: "2025/1").
     */
    public static String getSemestre(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int semestre = (month <= 6) ? 1 : 2;
        return year + "/" + semestre;
    }

    public static String getSemestre(LocalDateTime dateTime) {
        return getSemestre(dateTime.toLocalDate());
    }
}
