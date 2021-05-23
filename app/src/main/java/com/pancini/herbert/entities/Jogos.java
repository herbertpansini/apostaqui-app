package com.pancini.herbert.entities;

import java.io.Serializable;

public class Jogos implements Serializable {
    private int apostaId;
    private int numeroRodada;
    private String descricaoRodada;
    private String nomeEquipeMandante;
    private String siglaEquipeMandante;
    private int opcao;
    private String siglaEquipeVisitante;
    private String nomeEquipeVisitante;
    private int jogou;
    private int acertou;
    private int pontos;
    private int valida;
    private boolean isHeader;
    private boolean isTitle;

    public int getApostaId() {
        return this.apostaId;
    }

    public void setApostaId(int apostaId) {
        this.apostaId = apostaId;
    }

    public int getNumeroRodada() {
        return this.numeroRodada;
    }

    public void setNumeroRodada(int numeroRodada) {
        this.numeroRodada = numeroRodada;
    }

    public String getDescricaoRodada() {
        return this.descricaoRodada;
    }

    public void setDescricaoRodada(String descricaoRodada) {
        this.descricaoRodada = descricaoRodada;
    }

    public String getNomeEquipeMandante() {
        return this.nomeEquipeMandante;
    }

    public void setNomeEquipeMandante(String nomeEquipeMandante) {
        this.nomeEquipeMandante = nomeEquipeMandante;
    }

    public int getOpcao() {
        return this.opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public String getNomeEquipeVisitante() {
        return this.nomeEquipeVisitante;
    }

    public void setNomeEquipeVisitante(String nomeEquipeVisitante) {
        this.nomeEquipeVisitante = nomeEquipeVisitante;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public String getSiglaEquipeMandante() {
        return this.siglaEquipeMandante;
    }

    public void setSiglaEquipeMandante(String siglaEquipeMandante) {
        this.siglaEquipeMandante = siglaEquipeMandante;
    }

    public String getSiglaEquipeVisitante() {
        return this.siglaEquipeVisitante;
    }

    public void setSiglaEquipeVisitante(String siglaEquipeVisitante) {
        this.siglaEquipeVisitante = siglaEquipeVisitante;
    }

    public int getAcertou() {
        return this.acertou;
    }

    public void setAcertou(int acertou) {
        this.acertou = acertou;
    }

    public int getEscudoEquipeMandante() {
        return  new Escudo().getEscudo(this.siglaEquipeMandante);
    }

    public int getEscudoEquipeVisitante() {
        return new Escudo().getEscudo(this.siglaEquipeVisitante);
    }

    public int getJogou() {
        return this.jogou;
    }

    public void setJogou(int jogou) {
        this.jogou = jogou;
    }

    public int getValida() {
        return this.valida;
    }

    public void setValida(int valida) {
        this.valida = valida;
    }

    public boolean isHeader() {
        return this.isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isTitle() {
        return this.isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}