package com.pancini.herbert.entities;

import java.io.Serializable;

public class UsuarioAposta implements Serializable {
    private int usuarioId;
    private int rodadaId;
    private String nome;
    private int apostas;
    private boolean selected;
    private boolean edited;

    public UsuarioAposta() {}

    public UsuarioAposta(int usuarioId, int rodadaId, String nome, int apostas) {
        this.usuarioId = usuarioId;
        this.rodadaId = rodadaId;
        this.nome = nome;
        this.apostas = apostas;
    }

    public int getUsuarioId() {
        return this.usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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

    public int isValid() {
        if (this.isSelected()) {
            return 1;
        }
        return 0;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEdited() {
        return this.edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public int getRodadaId() {
        return this.rodadaId;
    }

    public void setRodadaId(int rodadaId) {
        this.rodadaId = rodadaId;
    }
}