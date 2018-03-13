package com.company;

/**
 * Created by Dasha on 23.04.2017.
 */
public class PolizLabel {
    private String name;
    private int position;

    public PolizLabel(String name, int position)
    {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PolizLabel{" +
                "name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
