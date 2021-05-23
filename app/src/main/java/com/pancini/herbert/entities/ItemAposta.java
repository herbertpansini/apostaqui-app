package com.pancini.herbert.entities;

import java.io.Serializable;

public class ItemAposta implements Serializable {
    private int id;
    private int apostaId;
    private int confrontoId;
    private Confronto confronto;
    private boolean mandanteSelected;
    private boolean empateSelected;
    private boolean visitanteSelected;
    private int current = NONE; // hold the answer picked by the user, initial is NONE(see below)
    public static final int NONE = -1; // No answer selected
    public static final int ANSWER_ONE_SELECTED = 0; // first answer selected
    public static final int ANSWER_TWO_SELECTED = 1; // second answer selected
    public static final int ANSWER_THREE_SELECTED = 2; // third answer selected

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApostaId() {
        return this.apostaId;
    }

    public void setApostaId(int apostaId) {
        this.apostaId = apostaId;
    }

    public int getConfrontoId() {
        return this.confrontoId;
    }

    public void setConfrontoId(int confrontoId) {
        this.confrontoId = confrontoId;
    }

    public Confronto getConfronto() {
        return confronto;
    }

    public void setConfronto(Confronto confronto) {
        this.confronto = confronto;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isMandanteSelected() {
        return this.mandanteSelected;
    }

    public void setMandanteSelected(boolean mandanteSelected) {
        this.mandanteSelected = mandanteSelected;
    }

    public boolean isEmpateSelected() {
        return this.empateSelected;
    }

    public void setEmpateSelected(boolean empateSelected) {
        this.empateSelected = empateSelected;
    }

    public boolean isVisitanteSelected() {
        return this.visitanteSelected;
    }

    public void setVisitanteSelected(boolean visitanteSelected) {
        this.visitanteSelected = visitanteSelected;
    }
}