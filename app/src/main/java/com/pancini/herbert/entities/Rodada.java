package com.pancini.herbert.entities;

import java.io.Serializable;

public class Rodada implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int ano;
    private int numero;
    private int campeonatoId;
    private int ativa;
    private int finalizada;

    public Rodada() {}

    public Rodada(int ano, int numero, int campeonatoId) {
        this.ano = ano;
        this.numero = numero;
        this.setCampeonatoId(campeonatoId);
    }

    public Rodada(int id, int ano, int numero, int campeonatoId) {
        this.id = id;
        this.ano = ano;
        this.numero = numero;
        this.setCampeonatoId(campeonatoId);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return this.ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCampeonatoId() {
        return this.campeonatoId;
    }

    public void setCampeonatoId(int campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    @Override
    public String toString() {
        return String.format("%02d", this.numero) + "ª Rodada";
    }

    public int getAtiva() {
        return this.ativa;
    }

    public void setAtiva(int ativa) {
        this.ativa = ativa;
    }

    public boolean isAtiva() {
        return this.ativa > 0;
    }

    public int getFinalizada() {
        return this.finalizada;
    }

    public void setFinalizada(int finalizada) {
        this.finalizada = finalizada;
    }

    public boolean isFinalizada() {
        return this.finalizada > 0;
    }
}