package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 28.10.2016.
 */

public class SyntacticalAnalyzer {
    private List<Lexeme> lexemes;

    private Integer numLex = 0;

    private ArrayList<Error> errors;

    public SyntacticalAnalyzer(LexicalAnalyzer analyzer) {
        lexemes = analyzer.getOutputLexemesTable();
        errors = new ArrayList<>();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public ArrayList<Error> getError() {
        return errors;
    }

    private boolean arrayRange() {
        return numLex < lexemes.size();
    }

    private int getLexemeCode() {
        return lexemes.get(numLex).getLexemeCode();
    }

    private int getLexemeLine() {
        return lexemes.get(numLex).getLineNumber();
    }

    private boolean program() {
        if (arrayRange() && getLexemeCode() == 1) {// program
            numLex++;
            if (arrayRange() && getLexemeCode() == 38) {// IDN
                numLex++;
                if (arrayRange() && getLexemeCode() == 31)// ¶
                    numLex++;
                if (SpOg()) {
                    if (arrayRange() && getLexemeCode() == 5) {// begin
                        numLex++;
                        if (arrayRange() && getLexemeCode() == 31)// ¶
                            numLex++;
                        if (SpOp()) {
                            if (arrayRange() && getLexemeCode() == 13) {// end.
                                numLex++;
                                System.out.println("Ошибок нет");
                                return true;
                            } else {
                                System.out.println("Строка " + getLexemeLine() + ": отсутствует закрывающая операторная скобка end. program()");
                                errors.add(new Error(getLexemeLine(), "отсутствует закрывающая операторная скобка end. program()"));
                                return false;
                            }
                        } else {
                            System.out.println("Строка" + getLexemeLine() + ": неправильный список операторов program()");
                            errors.add(new Error(getLexemeLine(), "неправильный список операторов program()"));
                            return false;
                        }
                    } else {
                        System.out.println("Строка " + getLexemeLine() + ": отсутствует открывающая операторная скобка begin program()");
                        errors.add(new Error(getLexemeLine(), "отсутствует открывающая операторная скобка begin program()"));
                        return false;
                    }
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": неправильный список объявления program()");
                    errors.add(new Error(getLexemeLine(), "неправильный список объявления program()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует название программы program()");
                errors.add(new Error(getLexemeLine(), "отсутствует название программы program()"));
                return false;
            }
        } else {
            System.out.println("Строка " + getLexemeLine() + ": программа должна начинаться со слова program program()");
            errors.add(new Error(getLexemeLine(), "программа должна начинаться со слова program program()"));
            return false;
        }
    }

    private boolean SpOg() {
        if (type()) {
            if (SpIDN()) {
                while (arrayRange() && getLexemeCode() == 31) {// ¶
                    numLex++;
                    if (getLexemeCode() == 5) {
                        return true;
                    }
                    if (type()) {
                        if (!SpIDN()) {
                            System.out.println("Строка " + getLexemeLine() + ": объявить список идентификаторов SpOg()");
                            errors.add(new Error(getLexemeLine(), "объявить список идентификаторов SpOg()"));
                            return false;
                        }
                    } else {
                        System.out.println("Строка " + getLexemeLine() + ": отсутствует тип SpOg()");
                        errors.add(new Error(getLexemeLine(), "отсутствует тип SpOg()"));
                        return false;
                    }
                }
                return true;
            } else {
                System.out.println("Строка " + getLexemeLine() + ": объявить список идентификаторов SpOg()");
                errors.add(new Error(getLexemeLine(), "объявить список идентификаторов SpOg()"));
                return false;
            }
        } else {
            System.out.println("Строка " + getLexemeLine() + ": отсутствует тип SpOg()");
            errors.add(new Error(getLexemeLine(), "отсутствует тип SpOg()"));
            return false;
        }
    }

    private boolean type() {
        if (arrayRange() && (getLexemeCode() == 2 || getLexemeCode() == 3 || getLexemeCode() == 4)) {
            numLex++;
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": тип может быть int,real или name type()");
            errors.add(new Error(getLexemeLine(), "тип может быть int,real или name type()"));
            return false;
        }
    }

    private boolean SpIDN() {
        if (arrayRange() && getLexemeCode() == 38) {// IDN
            numLex++;
            while (arrayRange() && getLexemeCode() == 20) {// ,
                numLex++;
                if (arrayRange() && getLexemeCode() == 38) {// IDN
                    numLex++;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": не является идентификатором SpIDN()");
                    errors.add(new Error(getLexemeLine(), "не является идентификатором SpIDN()"));
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": должен быть список идентификаторов SpIDN()");
            errors.add(new Error(getLexemeLine(), "должен быть список идентификаторов SpIDN()"));
            return false;
        }
    }

    private boolean SpOp() {
        if (arrayRange() && getLexemeCode() == 13) {// end.
            System.out.println("Строка " + getLexemeLine() + ": пустой список операторов SpOp()");
            errors.add(new Error(getLexemeLine(), "пустой список операторов SpOp()"));
            return false;
        } else if (operator()) {
            if (arrayRange() && getLexemeCode() == 31) {// ¶
                numLex++;
                /*if(arrayRange() && getLexemeCode() == 12){// end
                    return true;
                }*/
                while (arrayRange() && getLexemeCode() != 13 && getLexemeCode() != 12) {// end.
                    if (operator()) {
                        if (arrayRange() && getLexemeCode() == 31) {// ¶
                            numLex++;
                           /* if (arrayRange() && getLexemeCode() == 12) {// end
                                return true;
                            }*/
                        } else {
                            System.out.println("Строка " + getLexemeLine() + ": отсутствует переход на новую строку SpOp()");
                            errors.add(new Error(getLexemeLine(), "отсутствует переход на новую строку SpOp()"));
                            return false;
                        }
                    } else {
                        System.out.println("Строка " + getLexemeLine() + ": неправильный оператор SpOp()");
                        errors.add(new Error(getLexemeLine(), "неправильный оператор SpOp()"));
                        return false;
                    }
                }
                return true;
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует переход на новую строку SpOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует переход на новую строку SpOp()"));
                return false;
            }
        } else {
            System.out.println("Строка " + getLexemeLine() + ": неправильный оператор SpOp()");
            errors.add(new Error(getLexemeLine(), "неправильный оператор SpOp()"));
            return false;
        }
    }

    private boolean operator() {
        //Оператор присваивания и объявления метки
        if (arrayRange() && getLexemeCode() == 38) {// IDN
            numLex++;
            if (arrayRange() && getLexemeCode() == 27) {// :
                numLex++;
                if (mainOp()) {
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": неверное использование метки operator()");
                    errors.add(new Error(getLexemeLine(), "неверное использование метки operator()"));
                    return false;
                }
            } else if (arrayRange() && getLexemeCode() == 30) {// =
                numLex++;
                if (expression()) {
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение mainOp()");
                    errors.add(new Error(getLexemeLine(), "отсутствует выражение mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует : в определении значения метки operator()");
                errors.add(new Error(getLexemeLine(), "отсутствует : в определении значения метки operator()"));
                return false;
            }
        } else if (mainOp()) {
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": неправильный оператор operator()");
            errors.add(new Error(getLexemeLine(), "неправильный оператор operator()"));
            return false;
        }
    }

    private boolean mainOp() {
        // Оператор вывода
        if (arrayRange() && getLexemeCode() == 6) {// cout
            numLex++;
            if (arrayRange() && getLexemeCode() == 32) {// <<
                numLex++;
                if (arrayRange() && (getLexemeCode() == 38 || getLexemeCode() == 39)) {// IDN or CON
                    numLex++;
                    while (arrayRange() && getLexemeCode() == 32) {// <<
                        numLex++;
                        if (arrayRange() && (getLexemeCode() == 38 || getLexemeCode() == 39)) {// IDN or CON
                            numLex++;
                        } else {
                            System.out.println("Строка " + getLexemeLine() + ": отсутствует IDN|CON mainOp()");
                            errors.add(new Error(getLexemeLine(), "отсутствует IDN|CON mainOp()"));
                            return false;
                        }
                    }
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": неправильный оператор вывода mainOp()");
                    errors.add(new Error(getLexemeLine(), "неправильный оператор вывода mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует << mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует << mainOp()"));
                return false;
            }
            // Оператор ввода
        } else if (arrayRange() && getLexemeCode() == 7) {// cin
            numLex++;
            if (arrayRange() && getLexemeCode() == 33) {// >>
                numLex++;
                if (arrayRange() && getLexemeCode() == 38) {// IDN
                    numLex++;
                    while (arrayRange() && getLexemeCode() == 33) {// >>
                        numLex++;
                        if (arrayRange() && getLexemeCode() == 38) {// IDN
                            numLex++;
                        } else {
                            System.out.println("Строка " + getLexemeLine() + ": отсутствует IDN mainOp()");
                            errors.add(new Error(getLexemeLine(), "отсутствует IDN mainOp()"));
                            return false;
                        }
                    }
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": неправильный оператор ввода mainOp()");
                    errors.add(new Error(getLexemeLine(), "неправильный оператор ввода mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует >> mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует >> mainOp()"));
                return false;
            }
            // Оператор присваивания
        } else if (arrayRange() && getLexemeCode() == 38) {// IDN
            numLex++;
            if (arrayRange() && getLexemeCode() == 30) {// =
                numLex++;
                if (expression()) {
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение mainOp()");
                    errors.add(new Error(getLexemeLine(), "отсутствует выражение mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует знак присваивания (=) mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует знак присваивания (=) mainOp()"));
                return false;
            }
            // Цикл
        } else if (arrayRange() && getLexemeCode() == 8) {// for
            numLex++;
            if (arrayRange() && getLexemeCode() == 38) {// IDN
                numLex++;
                if (arrayRange() && getLexemeCode() == 30) {// =
                    numLex++;
                    if (expression()) {
                        if (arrayRange() && getLexemeCode() == 9) {// to
                            numLex++;
                            if (expression()) {
                                if (arrayRange() && getLexemeCode() == 10) {// by
                                    numLex++;
                                    if (expression()) {
                                        if (arrayRange() && getLexemeCode() == 11) {// while
                                            numLex++;
                                            if (arrayRange() && getLexemeCode() == 25) {// (
                                                numLex++;
                                                if (logicalExpression()) {
                                                    if (arrayRange() && getLexemeCode() == 26) {// )
                                                        numLex++;
                                                        if (SpOp()) {
                                                            if (arrayRange() && getLexemeCode() == 12) {// end
                                                                numLex++;
                                                                return true;
                                                            } else {
                                                                System.out.println("Строка " + getLexemeLine() + ": отсутствует закрывающая операторная скобка end для цикла mainOp()");
                                                                errors.add(new Error(getLexemeLine(), "отсутствует закрывающая операторная скобка end для цикла mainOp()"));
                                                                return false;
                                                            }
                                                        } else {
                                                            System.out.println("Строка " + getLexemeLine() + ": отсутствует оператор цикла mainOp()");
                                                            errors.add(new Error(getLexemeLine(), "отсутствует оператор цикла mainOp()"));
                                                            return false;
                                                        }
                                                    } else {
                                                        System.out.println("Строка " + getLexemeLine() + ": отсутствует закрывающая скобка после логического выражения для цикла while mainOp()");
                                                        errors.add(new Error(getLexemeLine(), "отсутствует закрывающая скобка после логического выражения для цикла while mainOp()"));
                                                        return false;
                                                    }
                                                } else {
                                                    System.out.println("Строка " + getLexemeLine() + ": отсутствует логическое выражение для цикла while mainOp()");
                                                    errors.add(new Error(getLexemeLine(), "отсутствует логическое выражение для цикла while mainOp()"));
                                                    return false;
                                                }
                                            } else {
                                                System.out.println("Строка " + getLexemeLine() + ": отсутствует открывающая скобка перед логическим выражением для цикла while mainOp()");
                                                errors.add(new Error(getLexemeLine(), "отсутствует открывающая скобка перед логическим выражением для цикла while mainOp()"));
                                                return false;
                                            }
                                        } else {
                                            System.out.println("Строка " + getLexemeLine() + ": отсутствует while mainOp()");
                                            errors.add(new Error(getLexemeLine(), "отсутствует while mainOp()"));
                                            return false;
                                        }
                                    } else {
                                        System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение после by mainOp()");
                                        errors.add(new Error(getLexemeLine(), "отсутствует выражение после by mainOp()"));
                                        return false;
                                    }
                                } else {
                                    System.out.println("Строка " + getLexemeLine() + ": отсутствует by mainOp()");
                                    errors.add(new Error(getLexemeLine(), "отсутствует by mainOp()"));
                                    return false;
                                }
                            } else {
                                System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение после to mainOp()");
                                errors.add(new Error(getLexemeLine(), "отсутствует выражение после to mainOp()"));
                                return false;
                            }
                        } else {
                            System.out.println("Строка " + getLexemeLine() + ": отсутствует to mainOp()");
                            errors.add(new Error(getLexemeLine(), "отсутствует to mainOp()"));
                            return false;
                        }
                    } else {
                        System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение после = в цикле for mainOp()");
                        errors.add(new Error(getLexemeLine(), "отсутствует выражение после = mainOp()"));
                        return false;
                    }
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует = в цикле for mainOp()");
                    errors.add(new Error(getLexemeLine(), "отсутствует отсутствует = в цикле for mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует IDN в цикле for mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует IDN в цикле for mainOp()"));
                return false;
            }
            // Условный переход
        } else if (arrayRange() && getLexemeCode() == 17) {// if
            numLex++;
            if (relation()) {
                if (arrayRange() && getLexemeCode() == 18) {// then
                    numLex++;
                    if (operator()) {
                        return true;
                    } else {
                        System.out.println("Строка " + getLexemeLine() + ": отсутствует оператор в условном переходе mainOp()");
                        errors.add(new Error(getLexemeLine(), "отсутствует оператор в условном переходе mainOp()"));
                        return false;
                    }
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует then в условном переходе mainOp()");
                    errors.add(new Error(getLexemeLine(), "отсутствует отсутствует then в условном переходе mainOp()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение в условном переходе mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует выражение в условном переходе mainOp()"));
                return false;
            }
            //Безусловный переход
        } else if (arrayRange() && getLexemeCode() == 19) {// goto
            numLex++;
            if (arrayRange() && getLexemeCode() == 38) {// IDN
                numLex++;
                return true;
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует IDN в безусловном переходе mainOp()");
                errors.add(new Error(getLexemeLine(), "отсутствует IDN в безусловном переходе mainOp()"));
                return false;
            }
        } else {
            System.out.println("Строка " + getLexemeLine() + ": неизвестный оператор mainOp()");
            errors.add(new Error(getLexemeLine(), "неизвестный оператор mainOp()"));
            return false;
        }
    }

    private boolean expression() {// <E>
        if (terminal()) {
            while (arrayRange() && (getLexemeCode() == 21 || getLexemeCode() == 22)) { // + -
                numLex++;
                terminal();
            }
            return true;
        } else {
            System.out.println("Строка " + arrayRange() + ": отсутствует терминал expression()");
            errors.add(new Error(getLexemeLine(), "отсутствует терминал expression()"));
            return false;
        }
    }

    private boolean terminal() {// <T>
        if (multiplier()) {
            while (arrayRange() && (getLexemeCode() == 23 || getLexemeCode() == 24)) { // * /
                numLex++;
                multiplier();
            }
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": отсутствует множитель terminal()");
            errors.add(new Error(getLexemeLine(), "отсутствует множитель terminal()"));
            return false;
        }
    }

    private boolean multiplier() {// <o>
        if (arrayRange() && getLexemeCode() == 25) { // (
            numLex++;
            expression();
            if (arrayRange() && getLexemeCode() == 26) { // )
                numLex++;
                return true;
            } else {
                System.out.println("Строка " + getLexemeLine() + ": осутствует закрывающая скобка multiplier()");
                errors.add(new Error(getLexemeLine(), "осутствует закрывающая скобка multiplier()"));
                return false;
            }
        } else if (arrayRange() && (getLexemeCode() == 38 || getLexemeCode() == 39)) { // IDN || CON
            numLex++;
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": должен быть IDN или CON или открывающая скобка для выражения multiplier()");
            errors.add(new Error(getLexemeLine(), "должен быть IDN или CON или открывающая скобка для выражения multiplier()"));
            return false;
        }
    }

    private boolean logicalExpression() {
        if (logicalTerminal()) {
            while (arrayRange() && getLexemeCode() == 15) { // or
                numLex++;
                logicalTerminal();
            }
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": должно быть логическое выражение logicalExpression()");
            errors.add(new Error(getLexemeLine(), "должно быть логическое выражение logicalExpression()"));
            return false;
        }
    }

    private boolean logicalTerminal() {
        if (logicalMultiplier()) {
            while (arrayRange() && getLexemeCode() == 14) { // and
                numLex++;
                logicalTerminal();
            }
            return true;
        } else {
            System.out.println("Строка " + getLexemeLine() + ": должен быть логический терм logicalTerminal()");
            errors.add(new Error(getLexemeLine(), "должен быть логический терм logicalTerminal()"));
            return false;
        }
    }

    private boolean logicalMultiplier() {
        if (arrayRange() && getLexemeCode() == 25) { // (
            numLex++;
            if (logicalExpression()) {
                if (arrayRange() && getLexemeCode() == 26) { // )
                    numLex++;
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует закрывающая скобка logicalMultiplier()");
                    errors.add(new Error(getLexemeLine(), "отсутствует закрывающая скобка logicalMultiplier()"));
                    return false;
                }
            }
        } else if (arrayRange() && getLexemeCode() == 16) { // not
            numLex++;
            if (logicalMultiplier()) {
                return true;
            } else {
                System.out.println("Строка " + getLexemeLine() + ": должен быть логический множитель после not logicalMultiplier()");
                errors.add(new Error(getLexemeLine(), "должен быть логический множитель после not logicalMultiplier()"));
                return false;
            }
        } else if (relation()) {
            return true;
        }
        System.out.println("Строка " + getLexemeLine() + ": должен быть логический множитель logicalMultiplier()");
        errors.add(new Error(getLexemeLine(), "должен быть логический множитель logicalMultiplier()"));
        return false;
    }

    private boolean relation() { // отношение
        if (expression()) {
            if (tokenRelation()) {
                if (expression()) {
                    return true;
                } else {
                    System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение после знака отношения relation()");
                    errors.add(new Error(getLexemeLine(), "отсутствует выражение после знака отношения relation()"));
                    return false;
                }
            } else {
                System.out.println("Строка " + getLexemeLine() + ": отсутствует знак отношения relation()");
                errors.add(new Error(getLexemeLine(), "отсутствует знак отношения relation()"));
                return false;
            }
        } else {
            System.out.println("Строка " + getLexemeLine() + ": отсутствует выражение relation()");
            errors.add(new Error(getLexemeLine(), "отсутствует выражение relation()"));
            return false;
        }
    }

    private boolean tokenRelation() { //знак отношения
        if (arrayRange() && (getLexemeCode() == 28 || getLexemeCode() == 29 || getLexemeCode() == 36
                || getLexemeCode() == 34 || getLexemeCode() == 35 || getLexemeCode() == 37)) {
            numLex++;
            return true;
        } else {
            System.out.println("Рядок " + getLexemeLine() + ": має бути знак відношення tokenRelation()");
            errors.add(new Error(getLexemeLine(), "має бути знак відношення tokenRelation()"));
            return false;
        }
    }

    public void analyze() {
        program();
    }
}