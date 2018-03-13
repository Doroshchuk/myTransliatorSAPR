package com.company;

/**
 * Created by Dasha on 15.05.2017.
 */
public class PolizExecutionIdentification {
    public String name;
    public String type;
    public Object value;

    public PolizExecutionIdentification (String name, String type, Object value){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
