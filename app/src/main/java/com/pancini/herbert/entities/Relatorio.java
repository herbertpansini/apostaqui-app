package com.pancini.herbert.entities;

import java.io.Serializable;

public class Relatorio implements Serializable {
    private String nome;
    private String siglaEquipeMandante;
    private String mandante;
    private String empate;
    private String visitante;
    private String siglaEquipeVisitante;

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaEquipeMandante() {
        return this.siglaEquipeMandante;
    }

    public void setSiglaEquipeMandante(String siglaEquipeMandante) {
        this.siglaEquipeMandante = siglaEquipeMandante;
    }

    public String getMandante() {
        return this.mandante;
    }

    public void setMandante(String mandante) {
        this.mandante = mandante;
    }

    public String getEmpate() {
        return this.empate;
    }

    public void setEmpate(String empate) {
        this.empate = empate;
    }

    public String getVisitante() {
        return this.visitante;
    }

    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public String getSiglaEquipeVisitante() {
        return this.siglaEquipeVisitante;
    }

    public void setSiglaEquipeVisitante(String siglaEquipeVisitante) {
        this.siglaEquipeVisitante = siglaEquipeVisitante;
    }
}