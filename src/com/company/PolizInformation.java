package com.company;

/**
 * Created by Dasha on 17.05.2017.
 */
public class PolizInformation {
    private String input;
    private String stack;
    private String poliz;

    public PolizInformation(String input, String stack, String poliz){
        this.input = input;
        this.stack = stack;
        this.poliz = poliz;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getPoliz() {
        return poliz;
    }

    public void setPoliz(String poliz) {
        this.poliz = poliz;
    }
}
