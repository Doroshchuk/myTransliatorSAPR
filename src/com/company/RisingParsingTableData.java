package com.company;

import java.util.ArrayList;

/**
 * Created by Dasha on 26.02.2017.
 */

public class RisingParsingTableData {
    private Integer step;
    private String stack;
    private String sign;
    private String input;

    public RisingParsingTableData(Integer step, String stack, String sign, String input) {
        this.step = step;
        this.stack = stack;
        this.sign = sign;
        this.input = input;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
