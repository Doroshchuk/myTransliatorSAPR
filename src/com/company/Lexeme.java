package com.company;

/**
 * Created by Dasha on 06.10.2016.
 */

public class Lexeme {
    private Integer lexemeNumber;
    private int lineNumber;
    private String lexemeName;
    private int lexemeCode;
    private Constant constant;
    private Identifier identifier;
    private static Integer count = 1;

    public Lexeme(int lineNumber, String lexemeName, int lexemeCode) {

        this.lexemeNumber = 0;
        this.lineNumber = lineNumber;
        this.lexemeName = lexemeName;
        this.lexemeCode = lexemeCode;
    }

    public Lexeme(int lineNumber, String lexemeName, int lexemeCode, Constant constant) {
        this.lexemeNumber = 0;
        this.lineNumber = lineNumber;
        this.lexemeName = lexemeName;
        this.lexemeCode = lexemeCode;
        this.constant = constant;
    }

    public Lexeme(int lineNumber, String lexemeName, int lexemeCode, Identifier identifier) {
        this.lexemeNumber = 0;
        this.lineNumber = lineNumber;
        this.lexemeName = lexemeName;
        this.lexemeCode = lexemeCode;
        this.identifier = identifier;
    }

    public Integer getIdnCode() {
        if (identifier != null) {
            return identifier.getCode();
        } else if (constant != null) {
                return constant.getCode();
            } else {
            return null;
        }
    }

    public void setLexemeNumber(int number) {
        this.lexemeNumber = number;
    }

    public Integer getLexemeNumber() {
        return lexemeNumber;
    }

    public void setLexemeNumber(Integer lexemeNumber) {
        this.lexemeNumber = lexemeNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLexemeName() {
        return lexemeName;
    }

    public void setLexemeName(String lexemeName) {
        this.lexemeName = lexemeName;
    }

    public int getLexemeCode() {
        return lexemeCode;
    }

    public void setLexemeCode(int lexemeCode) {
        this.lexemeCode = lexemeCode;
    }

    public Constant getConstant() {
        return constant;
    }

    public void setConstant(Constant constant) {
        this.constant = constant;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public static void subtractCount() {
        count--;
    }
    @Override
    public String toString() {
        return "Lexeme{" +
                "lineNumber=" + lineNumber +
                ", lexemeName='" + lexemeName + '\'' +
                ", lexemeCode=" + lexemeCode +
                ", constant=" + constant +
                ", identifier=" + identifier +
                '}';
    }

    public static void reset(){
        count = 1;
    }
}
