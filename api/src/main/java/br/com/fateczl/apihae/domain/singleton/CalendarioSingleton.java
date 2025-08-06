package br.com.fateczl.apihae.domain.singleton;

import br.com.fateczl.apihae.domain.entity.Calendario;
import org.springframework.stereotype.Component;

@Component
public class CalendarioSingleton {

    private final Calendario calendario;

    public CalendarioSingleton() {
        this.calendario = new Calendario();
    }

    public Calendario getCalendario() {
        return calendario;
    }
}
