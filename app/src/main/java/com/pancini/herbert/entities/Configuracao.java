package com.pancini.herbert.entities;

import java.io.Serializable;

public class Configuracao implements Serializable {
    private int id;
    private int quantidadeHoras;

    public Configuracao() {

    }

    public Configuracao(int id, int quantidadeHoras) {
        this.id = id;
        this.quantidadeHoras = quantidadeHoras;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidadeHoras() {
        return this.quantidadeHoras;
    }

    public void setQuantidadeHoras(int quantidadeHoras) {
        this.quantidadeHoras = quantidadeHoras;
    }
}
