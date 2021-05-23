package com.pancini.herbert.entities;

import java.io.Serializable;

public class Classificacao implements Serializable {
    private int classificacao;
    private int usuarioId;
    private int rodadaId;
    private String nome;
    private int apostas;
    private int pontos;

    public int getUsuarioId() {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getRodadaId() {
        return this.rodadaId;
    }

    public void setRodadaId(int rodadaId) {
        this.rodadaId = rodadaId;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getApostas() {
        return this.apostas;
    }

    public void setApostas(int apostas) {
        this.apostas = apostas;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getClassificacao() {
        return this.classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }
}