package br.com.fateczl.apihae.domain.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Calendario {
    private Set<LocalDate> diasNaoLetivos = new HashSet<>();

    public Set<LocalDate> getDiasNaoLetivos() {
        return diasNaoLetivos;
    }

    public void adicionarDiaNaoLetivo(LocalDate data) {
        diasNaoLetivos.add(data);
    }

    public void removerDiaNaoLetivo(LocalDate data) {
        diasNaoLetivos.remove(data);
    }

    public boolean isDiaNaoLetivo(LocalDate data) {
        return diasNaoLetivos.contains(data);
    }
}
