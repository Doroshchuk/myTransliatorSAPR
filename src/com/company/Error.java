package com.company;

/**
 * Created by Dasha on 28.10.2016.
 */

public class Error {
    private Integer line;
    private String text;

    public Error(Integer line, String text) {
        this.line = line;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
    public String toString (){
        return text + " on line " + line;
    }
}
