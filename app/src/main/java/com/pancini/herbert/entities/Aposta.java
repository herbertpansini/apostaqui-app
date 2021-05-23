package com.pancini.herbert.entities;

import java.io.Serializable;

public class Aposta implements Serializable {
    private int id;
    private int rodadaId;
    private int usuarioId;
    private int pontos;
    private int valida;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getValida() {
        return this.valida;
    }

    public void setValida(int valida) {
        this.valida = valida;
    }

    public int getRodadaId() {
        return this.rodadaId;
    }

    public void setRodadaId(int rodadaId) {
        this.rodadaId = rodadaId;
    }
}
