package com.company;

/**
 * Created by Dasha on 09.10.2016.
 */

public class Constant {
    private int code;
    private String value;
    private static int count = 1;

    public Constant(String value) {
        code = count;
        this.value = value;
        count++;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        Constant constant = (Constant) o;
        if (constant.value.equals(this.value)) {
            return true;
        } else {
            return false;
        }
    }


    public static void subtractCount() {
        count--;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "code=" + code +
                ", value='" + value + '\'' +
                '}';
    }

    public static void reset(){
        count = 1;
    }
}
