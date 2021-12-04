package com.org.example;

public class People implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    private String name;

    public People() {
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public People(java.lang.String name) {
        this.name = name;
    }

}
