package com.company;

/**
 * Created by Dasha on 15.05.2017.
 */
public class PolizCell {
    private String name;
    private double value;

    public PolizCell (String name){
        this.name = name;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
