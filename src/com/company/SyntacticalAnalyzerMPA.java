package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dasha on 08.11.2016.
 */

public class SyntacticalAnalyzerMPA {
    private List<Lexeme> lexemes;
    private List<Constant> consList;
    private List<Identifier> idnsList;
    private int count;
    private LinkedList<Integer> stack;
    private ArrayList<LexemeMPA> lexemesMPA;
    private ArrayList<ChekingMPA> checkingMPA;
    private List<String> inputLexemes;

    public SyntacticalAnalyzerMPA(LexicalAnalyzer analyzer){
        this.lexemes = analyzer.getOutputLexemesTable();
        this.consList = analyzer.getConstants();
        this.idnsList = analyzer.getIdentifiers();
        this.count = 0;
        this.stack = new LinkedList<>();
        this.lexemesMPA = new ArrayList<>();
        this.checkingMPA = new ArrayList<>();
        this.inputLexemes = analyzer.getInputLexemes();
        initRules(checkingMPA);
        analyze();
    }

    public void analyze() {
        int state = 1;
        for (count = 0; count < lexemes.size(); count++) {
            state = getState(state, stack);
        }
    }

    private String getLexeme() {
        if (lexemes.get(count).getLexemeCode() == 38) {
            return idnsList.get(lexemes.get(count).getIdnCode() - 1).getName();
        }
        if (lexemes.get(count).getLexemeCode() == 39) {
            return "" + consList.get(lexemes.get(count).getIdnCode() - 1).getValue();
        }
        return inputLexemes.get(lexemes.get(count).getLexemeCode());
    }

    private String getStack() {
        String string = "";
        for (Integer el : stack) {
            string += " " + el;
        }
        return string;
    }

    private int getState(int state, LinkedList<Integer> stack) {
        for (ChekingMPA checking_MPA : checkingMPA){
            if (Objects.equals(checking_MPA.getLexemeCode(), lexemes.get(count).getLexemeCode())) {
                if (checking_MPA.getAlpha() == state) {

                    if (checking_MPA.getBeta() != null) {
                        if (checking_MPA.getStack() == null) {
                            lexemesMPA.add(new LexemeMPA(count + 1, getLexeme(), state, getStack()));
                            return checking_MPA.getBeta();
                        } else {
                            stack.push(checking_MPA.getStack());
                            lexemesMPA.add(new LexemeMPA(count + 1, getLexeme(), state, getStack()));
                            return checking_MPA.getBeta();
                        }
                    } else {
                        lexemesMPA.add(new LexemeMPA(count + 1, getLexeme(), state, getStack()));
                        if (state == 10) {
                            stack.push(checking_MPA.getMpaStack());
                            return checking_MPA.getMpaBeta();
                        } else return stack.pop();
                    }
                }
            }
        }
        for (ChekingMPA checking_MPA : checkingMPA) {
            if (checking_MPA.getAlpha() == state) {
                if (checking_MPA.getMpaBeta() != null) {
                    if (checking_MPA.getMpaBeta() == -1) {
                        lexemesMPA.add(new LexemeMPA(count + 1, getLexeme(), state, getStack()));
                        return getState(stack.pop(), stack);
                    }
                    stack.push(checking_MPA.getMpaStack());
                    lexemesMPA.add(new LexemeMPA(count + 1, getLexeme(), state, getStack()));
                    return getState(checking_MPA.getMpaBeta(), stack);
                } else {
                    throw new IllegalArgumentException("Ошибка: " + checking_MPA.getSemant()
                            + " в строке № " + lexemes.get(count).getLineNumber());
                }
            }
        }
        return 1;
    }

    private void initRules(ArrayList<ChekingMPA> checkingMPA) {
        checkingMPA.add(new ChekingMPA(1, "program", 2, null, "[!=]error: Отсутствует program", "", 1, null, null));
        checkingMPA.add(new ChekingMPA(2, "IDN", 3, null, "[!=]error: Отсутствует IDN после program", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(3, "¶", 4, null, "[!=]error: Отсутствует переход на новую строку", "", 31, null, null));
        checkingMPA.add(new ChekingMPA(4, "int", 5, null, "[!=]error: Отсутствует int|real|name", "", 2, null, null));
        checkingMPA.add(new ChekingMPA(4, "real", 5, null, "[!=]error: Отсутствует int|real|name", "", 3, null, null));
        checkingMPA.add(new ChekingMPA(4, "name", 5, null, "[!=]error: Отсутствует int|real|name", "", 4, null, null));
        checkingMPA.add(new ChekingMPA(5, "IDN", 6, null, "[!=]error: Отсутствует IDN после типа", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(6, ",", 5, null, "[!=]error: Отсутствует , или переход на новую строку", "", 20, null, null));
        checkingMPA.add(new ChekingMPA(6, "¶", 7, null, "[!=]error: Отсутствует , или переход на новую строку", "", 31, null, null));
        checkingMPA.add(new ChekingMPA(7, "begin", 8, null, "[!=]error: Отсутствует , или переход на новую строку или begin", "", 5, null, null));
        checkingMPA.add(new ChekingMPA(7, "int", 5, null, "[!=]error: Отсутствует int|real|name", "", 2, null, null));
        checkingMPA.add(new ChekingMPA(7, "real", 5, null, "[!=]error: Отсутствует int|real|name", "", 3, null, null));
        checkingMPA.add(new ChekingMPA(7, "name", 5, null, "[!=]error: Отсутствует int|real|name", "", 4, null, null));
        checkingMPA.add(new ChekingMPA(8, "¶", 11, 9, "[!=]error: Отсутствует переход на новую строку", "", 31, null, null));
        checkingMPA.add(new ChekingMPA(9, "¶", 10, null, "[!=]error: Отсутствует переход на новую строку", "", 31, null, null));
        checkingMPA.add(new ChekingMPA(10, "end.", null, null, "[!=] п/а опер 11 ↓9", "[=]выход", 13, 11, 9));

        checkingMPA.add(new ChekingMPA(11, "IDN", 12, null, "[!=] Отсутствует оператор", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(11, "cout", 16, null, "[!=] Отсутствует оператор", "", 6, null, null));
        checkingMPA.add(new ChekingMPA(11, "cin", 19, null, "[!=] Отсутствует оператор", "", 7, null, null));
        checkingMPA.add(new ChekingMPA(11, "for", 22, null, "[!=] Отсутствует оператор", "", 8, null, null));
        checkingMPA.add(new ChekingMPA(11, "if", 35, 31, "[!=] Отсутствует оператор", "", 17, null, null));
        checkingMPA.add(new ChekingMPA(11, "goto", 34, null, "[!=] Отсутствует оператор", "", 19, null, null));
        checkingMPA.add(new ChekingMPA(12, "=", 35, 15, "[!=] Отсутствует = в операторе присваивания", "", 30, null, null));
        checkingMPA.add(new ChekingMPA(12, ":", 13, null, "[!=] Отсутствует : в метке", "", 27, null, null));
        checkingMPA.add(new ChekingMPA(13, "IDN", 14, null, "[!=] Отсутствует оператор для метки", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(13, "cout", 16, null, "[!=] Отсутствует оператор для метки", "", 6, null, null));
        checkingMPA.add(new ChekingMPA(13, "cin", 19, null, "[!=] Отсутствует оператор для метки", "", 7, null, null));
        checkingMPA.add(new ChekingMPA(13, "for", 22, null, "[!=] Отсутствует оператор для метки", "", 8, null, null));
        checkingMPA.add(new ChekingMPA(13, "if", 35, 31, "[!=] Отсутствует оператор для метки", "", 17, null, null));
        checkingMPA.add(new ChekingMPA(13, "goto", 34, null, "[!=] Отсутствует оператор для метки", "", 19, null, null));
        checkingMPA.add(new ChekingMPA(14, "=", 35, 15, "[!=] Отсутствует = в операторе присваивания для метки", "", 30, null, null));
        checkingMPA.add(new ChekingMPA(15, "@", null, null, "выход", "", null, -1, null));
        checkingMPA.add(new ChekingMPA(16, "<<", 17, null, "[!=] Отсутствует << в операторе вывода", "", 32, null, null));
        checkingMPA.add(new ChekingMPA(17, "IDN", 18, null, "[!=] Отсутствует IDN|CON в операторе вывода", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(17, "CON", 18, null, "[!=] Отсутствует IDN|CON в операторе вывода", "", 39, null, null));
        checkingMPA.add(new ChekingMPA(18, "<<", 17, null, "выход", "", 32, -1, null));
        checkingMPA.add(new ChekingMPA(19, ">>", 20, null, "[!=] Отсутствует >> в операторе ввода", "", 33, null, null));
        checkingMPA.add(new ChekingMPA(20, "IDN", 21, null, "[!=] Отсутствует IDN в операторе ввода", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(21, ">>", 20, null, "выход", "", 33, -1, null));
        checkingMPA.add(new ChekingMPA(22, "IDN", 23, null, "[!=] Отсутствует IDN в for", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(23, "=", 35, 24, "[!=] Отсутствует = после IDN в for", "", 30, null, null));
        checkingMPA.add(new ChekingMPA(24, "to", 35, 25, "[!=] Отсутствует to в for", "", 9, null, null));
        checkingMPA.add(new ChekingMPA(25, "by", 35, 26, "[!=] Отсутствует by в for", "", 10, null, null));
        checkingMPA.add(new ChekingMPA(26, "while", 27, null, "[!=] Отсутствует while в for", "", 11, null, null));
        checkingMPA.add(new ChekingMPA(27, "(", 38, 28, "[!=] Отсутствует ( для while", "", 25, null, null));
        checkingMPA.add(new ChekingMPA(28, ")", 11, 29, "[!=] Отсутствует ) после лог. выражения для while", "", 26, null, null));
        checkingMPA.add(new ChekingMPA(29, "¶", 30, null, "[!=] Отсутствует переход на новую строку", "", 31, null, null));
        checkingMPA.add(new ChekingMPA(30, "end", null, null, "[!=]п/а опер 11 ↓29", "выход", 12, 11, 29));
        checkingMPA.add(new ChekingMPA(31, "<", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 28, null, null));
        checkingMPA.add(new ChekingMPA(31, "<=", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 34, null, null));
        checkingMPA.add(new ChekingMPA(31, "==", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 36, null, null));
        checkingMPA.add(new ChekingMPA(31, "!=", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 37, null, null));
        checkingMPA.add(new ChekingMPA(31, ">=", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 35, null, null));
        checkingMPA.add(new ChekingMPA(31, ">", 35, 32, "[!=] Отсутствует знак отношения после выражения в if", "", 29, null, null));
        checkingMPA.add(new ChekingMPA(32, "then", 11, 33, "[!=] Отсутствует then в if", "", 18, null, null));
        checkingMPA.add(new ChekingMPA(33, "@", null, null, "выход", "", null, -1, null));
        checkingMPA.add(new ChekingMPA(34, "IDN", null, null, "[!=] Отсутствует IDN после goto", "выход", 38, null, null));

        checkingMPA.add(new ChekingMPA(35, "IDN", 37, null, "[!=] Отсутствует IDN|CON или ( в арифметическом выражении", "", 38, null, null));
        checkingMPA.add(new ChekingMPA(35, "CON", 37, null, "[!=] Отсутствует IDN|CON или ( в арифметическом выражении", "", 39, null, null));
        checkingMPA.add(new ChekingMPA(35, "(", 35, 36, "[!=] Отсутствует IDN|CON или ( в арифметическом выражении", "", 25, null, null));
        checkingMPA.add(new ChekingMPA(36, ")", 37, null, "[!=] Отсутствует ) в арифметическом выражении", "", 26, null, null));
        checkingMPA.add(new ChekingMPA(37, "+", 35, null, "[!=] Отсутствует знак +-*/ в арифметическом выражении", "", 21, -1, null));
        checkingMPA.add(new ChekingMPA(37, "-", 35, null, "[!=] Отсутствует знак +-*/ в арифметическом выражении", "", 22, -1, null));
        checkingMPA.add(new ChekingMPA(37, "*", 35, null, "[!=] Отсутствует знак +-*/ в арифметическом выражении", "", 23, -1, null));
        checkingMPA.add(new ChekingMPA(37, "/", 35, null, "[!=] Отсутствует знак +-*/ в арифметическом выражении", "", 24, -1, null));

        checkingMPA.add(new ChekingMPA(38, "not", 38, null, "[!=] п/а выражение Е 35 ↓39", "", 16, 35, 39));
        checkingMPA.add(new ChekingMPA(38, "(", 38, 40, "[!=] п/а выражение Е 35 ↓40", "", 25, 35, 39));
        checkingMPA.add(new ChekingMPA(39, "<", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 28, null, null));
        checkingMPA.add(new ChekingMPA(39, "<=", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 34, null, null));
        checkingMPA.add(new ChekingMPA(39, "==", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 36, null, null));
        checkingMPA.add(new ChekingMPA(39, "!=", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 37, null, null));
        checkingMPA.add(new ChekingMPA(39, ">=", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 35, null, null));
        checkingMPA.add(new ChekingMPA(39, ">", 35, 41, "[!=] Отсутствует знак отношения в логическом выражении", "", 29, null, null));
        checkingMPA.add(new ChekingMPA(40, ")", 41, null, "[!=] Отсутствует ) в логическом выражении", "", 26, null, null));
        checkingMPA.add(new ChekingMPA(41, "and", 38, null, "[!=]виход", "", 14, -1, null));
        checkingMPA.add(new ChekingMPA(41, "or", 38, null, "[!=]виход", "", 15, -1, null));
    }

    public List<LexemeMPA> getLexemesMPA(){
        return lexemesMPA;
    }
}
