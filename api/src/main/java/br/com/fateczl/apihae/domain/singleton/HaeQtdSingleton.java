package br.com.fateczl.apihae.domain.singleton;

import org.springframework.stereotype.Component;

@Component
public class HaeQtdSingleton {
    private static HaeQtdSingleton instance;
    private int quantidade;

    private HaeQtdSingleton() {
        quantidade = 0;
    }

    public static HaeQtdSingleton getInstance() {
        if (instance == null) {
            instance = new HaeQtdSingleton();
        }
        return instance;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
