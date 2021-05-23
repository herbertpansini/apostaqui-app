package com.pancini.herbert.entities;

import java.io.Serializable;

public class Equipe implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String sigla;

    public Equipe() {}

    public Equipe(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Equipe(int id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
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

    public String getSigla() {
        return this.sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    // to display object as a string in spinner
    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equipe) {
            Equipe equipe = (Equipe)obj;
            if(equipe.getNome().equals(this.nome) && equipe.getId() == this.id )
                return true;
        }
        return false;
    }
}