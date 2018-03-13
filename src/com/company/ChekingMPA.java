package com.company;

/**
 * Created by Dasha on 08.11.2016.
 */
public class ChekingMPA {
    private Integer alpha;
    private String metka;
    private Integer beta;
    private Integer stack;
    private String semant;
    private String semantEqual;
    private Integer lexemeCode;
    private Integer mpaBeta;
    private Integer mpaStack;

    public ChekingMPA (Integer alpha, String metka, Integer beta, Integer stack,
                    String semant, String semantEqual, Integer lexemeCode, Integer mpaBeta, Integer mpaStack) {
        this.alpha = alpha;
        this.metka = metka;
        this.beta = beta;
        this.stack = stack;
        this.semant = semant;
        this.semantEqual = semantEqual;
        this.lexemeCode = lexemeCode;
        this.mpaBeta = mpaBeta;
        this.mpaStack = mpaStack;
    }

    public Integer getStack() {
        return stack;
    }

    public void setStack(Integer stack) {
        this.stack = stack;
    }

    public String getSemant() {
        return semant;
    }

    public void setSemant(String semant) {
        this.semant = semant;
    }

    public Integer getBeta() {
        return beta;
    }

    public void setBeta(Integer beta) {
        this.beta = beta;
    }

    public String getMetka() {
        return metka;
    }

    public void setMetka(String metka) {
        this.metka = metka;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public String getSemantEqual() {
        return semantEqual;
    }

    public void setSemantEqual(String semantEqual) {
        this.semantEqual = semantEqual;
    }

    public Integer getLexemeCode() {
        return lexemeCode;
    }

    public void setLexemeCode(Integer lexCode) {
        this.lexemeCode = lexemeCode;
    }

    public Integer getMpaBeta() {
        return mpaBeta;
    }

    public void setSemantBeta(Integer mpaBeta) {
        this.mpaBeta = mpaBeta;
    }

    public Integer getMpaStack() {
        return mpaStack;
    }

    public void setSemantStack(Integer mpaStack) {
        this.mpaStack = mpaStack;
    }
}
