package com.company;

/**
 * Created by Dasha on 19.10.2016.
 */

public class LexemChecker {
    public boolean isSign(Lexeme lexeme) {
        if(lexeme == null){
            return false;
        }
        String lexemeName = lexeme.getLexemeName();
        return  lexemeName.equals("-") || lexemeName.equals("+");
    }

    public boolean isValid(Lexeme lexeme) {
        if(lexeme == null){
            return true;
        }

        return !(lexeme.getLexemeName().equals(")") || lexeme.getIdentifier() != null || lexeme.getConstant() != null);
    }

    public boolean isConstant(Lexeme lexeme) {
        if(lexeme == null){
            return false;
        }

        return lexeme.getConstant() != null;
    }
}
