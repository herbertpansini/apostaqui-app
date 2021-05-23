package com.pancini.herbert.entities;

import java.io.Serializable;
import java.util.Date;

public class Jogo implements Serializable {
    private int id;
    private int rodada;
    private int numeroRodada;
    private int equipeMandante;
    private String nomeEquipeMandante;
    private String siglaEquipeMandante;
    private Integer placarEquipeMandante;
    private Integer placarEquipeVisitante;
    private int equipeVisitante;
    private String nomeEquipeVisitante;
    private String siglaEquipeVisitante;
    private Date horario;

    public Jogo() {}

    public Jogo(int rodada, int equipeMandante, Integer placarEquipeMandante, Integer placarEquipeVisitante, int equipeVisitante, Date horario) {
        this.setRodada(rodada);
        this.setEquipeMandante(equipeMandante);
        this.setPlacarEquipeMandante(placarEquipeMandante);
        this.setPlacarEquipeVisitante(placarEquipeVisitante);
        this.setEquipeVisitante(equipeVisitante);
        this.setHorario(horario);
    }

    public Jogo(int id, int rodada, int equipeMandante, Integer placarEquipeMandante, Integer placarEquipeVisitante, int equipeVisitante, Date horario) {
        this.setId(id);
        this.setRodada(rodada);
        this.setEquipeMandante(equipeMandante);
        this.setPlacarEquipeMandante(placarEquipeMandante);
        this.setPlacarEquipeVisitante(placarEquipeVisitante);
        this.setEquipeVisitante(equipeVisitante);
        this.setHorario(horario);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRodada() {
        return this.rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    public int getEquipeMandante() {
        return this.equipeMandante;
    }

    public void setEquipeMandante(int equipeMandante) {
        this.equipeMandante = equipeMandante;
    }

    public String getNomeEquipeMandante() {
        return this.nomeEquipeMandante;
    }

    public void setNomeEquipeMandante(String nomeEquipeMandante) {
        this.nomeEquipeMandante = nomeEquipeMandante;
    }

    public String getSiglaEquipeMandante() {
        return this.siglaEquipeMandante;
    }

    public void setSiglaEquipeMandante(String siglaEquipeMandante) {
        this.siglaEquipeMandante = siglaEquipeMandante;
    }

    public int getEscudoEquipeMandante() {
        return  new Escudo().getEscudo(this.siglaEquipeMandante);
    }

    public Integer getPlacarEquipeMandante() {
        return this.placarEquipeMandante;
    }

    public void setPlacarEquipeMandante(Integer placarEquipeMandante) {
        this.placarEquipeMandante = placarEquipeMandante;
    }

    public Integer getPlacarEquipeVisitante() {
        return this.placarEquipeVisitante;
    }

    public void setPlacarEquipeVisitante(Integer placarEquipeVisitante) {
        this.placarEquipeVisitante = placarEquipeVisitante;
    }

    public int getEquipeVisitante() {
        return this.equipeVisitante;
    }

    public void setEquipeVisitante(int equipeVisitante) {
        this.equipeVisitante = equipeVisitante;
    }

    public String getNomeEquipeVisitante() {
        return this.nomeEquipeVisitante;
    }

    public void setNomeEquipeVisitante(String nomeEquipeVisitante) {
        this.nomeEquipeVisitante = nomeEquipeVisitante;
    }

    public String getSiglaEquipeVisitante() {
        return this.siglaEquipeVisitante;
    }

    public void setSiglaEquipeVisitante(String siglaEquipeVisitante) {
        this.siglaEquipeVisitante = siglaEquipeVisitante;
    }

    public int getEscudoEquipeVisitante() {
        return new Escudo().getEscudo(this.siglaEquipeVisitante);
    }

    public Date getHorario() {
        return this.horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public int getNumeroRodada() {
        return this.numeroRodada;
    }

    public void setNumeroRodada(int numeroRodada) {
        this.numeroRodada = numeroRodada;
    }
}
