package com.company;

/**
 * Created by Dasha on 17.05.2017.
 */
public class PolizExecutionInformation {
    private String input;
    private String stack;

    public PolizExecutionInformation(String input, String stack){
        this.input = input;
        this.stack = stack;
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
}
