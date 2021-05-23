package com.pancini.herbert.entities;

import java.io.Serializable;

public class Campeonato implements Serializable {
    private int id;
    private String nome;
    private int esporteId;
    private String nomeEsporte;

    public Campeonato() {}

    public Campeonato(String nome, int esporteId) {
        this.nome = nome;
        this.esporteId = esporteId;
    }

    public Campeonato(int id, String nome, int esporteId, String nomeEsporte) {
        this.id = id;
        this.nome = nome;
        this.esporteId = esporteId;
        this.nomeEsporte = nomeEsporte;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getEsporteId() {
        return this.esporteId;
    }
    public void setEsporteId(int esporteId) {
        this.esporteId = esporteId;
    }
    public String getNomeEsporte() {
        return this.nomeEsporte;
    }
    public void setNomeEsporte(String nomeEsporte) {
        this.nomeEsporte = nomeEsporte;
    }
}