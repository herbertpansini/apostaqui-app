package com.pancini.herbert.entities;

import java.io.Serializable;

public class Model implements Serializable {
    private int id;
    private String description;
    private int counter;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
