package com.company;

/**
 * Created by Dasha on 09.10.2016.
 */

public class Identifier {
    private int code;
    private String name;
    private String type;
    private Object value;
    private static int count = 1;

    public Identifier(String name, String type) {
        code = count;
        this.name = name;
        this.type = type;
        this.value = null;
        count++;
    }

    public static void subtractCount() {
        count--;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() { return name; }

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

    @Override
    public boolean equals(Object o) {
        Identifier that = (Identifier) o;
        if (that.name.equals(this.name)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static void reset(){
        count = 1;
    }
}
