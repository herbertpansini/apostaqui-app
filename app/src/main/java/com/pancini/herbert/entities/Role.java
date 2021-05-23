package com.pancini.herbert.entities;

import java.io.Serializable;

public class Role implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            Role role = (Role)obj;
            if(role.getName().equals(this.name) && role.getId() == this.id )
                return true;
        }
        return false;
    }
}