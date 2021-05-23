package com.pancini.herbert.entities;

import java.io.Serializable;

public class ReportHeader implements Serializable {
    private int podeGerar;
    private int diaSemana;
    private int numeroRodada;
    private int apostas;

    public int getPodeGerar() {
        return this.podeGerar;
    }

    public void setPodeGerar(int podeGerar) {
        this.podeGerar = podeGerar;
    }

    public int getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public int getNumeroRodada() {
        return this.numeroRodada;
    }

    public void setNumeroRodada(int numeroRodada) {
        this.numeroRodada = numeroRodada;
    }

    public int getApostas() {
        return this.apostas;
    }

    public void setApostas(int apostas) {
        this.apostas = apostas;
    }
}
