package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 06.10.2016.
 */

public class LexicalAnalyzer {
    private final List<String> inputLexemes;
    private List<Lexeme> outputLexemes;
    private List<Identifier> idns;
    private List<Constant> cons;
    private String programSource;

    LexicalAnalyzer(String source) {
        inputLexemes = new ArrayList<>();
        outputLexemes = new ArrayList<>();
        cons = new ArrayList<>();
        idns = new ArrayList<>();
        programSource = source;
        TableOfLexemes();
    }

    public void analyze() {
        String delimiters = "\\n\\s/(/)/+/-/*/{}/</>/=,/:";
        String regularExpression = "(?=[" + delimiters + "])|(?<=[" + delimiters + "])";
        programSource = programSource.replace("-", " - ");
        programSource = programSource.replace("+", " + ");
        String[] result = programSource.split(regularExpression);
        int i = 0;
        for (String elem : result) {
            if (elem.equals("\n")) {
                result[i] = "¶";
            }
            i++;
        }
        parseLexemsToTable(result);
    }

    private boolean isConstant(String lexeme) {
        if (lexeme.matches("^[-+]?[0-9]*\\.?[0-9]+$") || lexeme.matches("^[-+]?[0-9]*\\.?") || lexeme.matches("^[-+]?\\.[0-9]*\\?") || lexeme.matches("^[-+]?[0-9]*\\?")) {
            return true;
        }
        return false;
    }

    private void parseLexemsToTable(String[] initialLexemes) {
        int lineNumber = 1;
        Lexeme lexeme;
        outputLexemes.clear();
        boolean isDeclaration = true;
        String previousLexeme = null;
        String beforePreviousLexeme = null;


        List<String> lexemes = new ArrayList<>();
        for (int i = 0; i < initialLexemes.length; i++) {
            if (!(initialLexemes[i].equals(" "))) {
                lexemes.add(initialLexemes[i]);
            }
        }
        for (int i = 0, number = 1; i < lexemes.size(); i++, number++) {
            if (!lexemes.get(i).equals("\n") && !lexemes.get(i).equals("\t")) {
                if (lexemes.get(i).equals("begin")) {
                    isDeclaration = false;
                }
                String nextLexeme = lexemes.get(i + 1);
                if (nextLexeme != null) {
                    String doubleSymbolDelimiter = lexemes.get(i) + nextLexeme;
                    if (doubleSymbolDelimiter.equals("<=") || doubleSymbolDelimiter.equals(">=") || doubleSymbolDelimiter.equals("==") || doubleSymbolDelimiter.equals("!=") || doubleSymbolDelimiter.equals("<<") || doubleSymbolDelimiter.equals(">>")) {
                        int doubleSymbolDelimiterNumber = inputLexemes.indexOf(doubleSymbolDelimiter);
                        lexeme = new Lexeme(lineNumber, doubleSymbolDelimiter, doubleSymbolDelimiterNumber);
                        i++;
                        lexeme.setLexemeNumber(number);
                        outputLexemes.add(lexeme);
                        System.out.println(doubleSymbolDelimiter);
                        continue;
                    }
                }
                int lexemeNumber = inputLexemes.indexOf(lexemes.get(i));
                if (lexemeNumber != -1) {
                    lexeme = new Lexeme(lineNumber, lexemes.get(i), lexemeNumber);
                } else {
                    if (isConstant(lexemes.get(i))) {
                        Constant constant = new Constant(lexemes.get(i));
                        if (cons.contains(constant)) {
                            constant = cons.get(cons.indexOf(constant));
                            Constant.subtractCount();
                        } else {
                            cons.add(constant);
                        }
                        lexeme = new Lexeme(lineNumber, lexemes.get(i), inputLexemes.indexOf("con"), constant);
                    } else {
                        if (lexemes.get(i).charAt(0) >= '0' && lexemes.get(i).charAt(0) <= '9') {
                            throw new IllegalArgumentException("Помилкова лексема у строці " + lineNumber);
                        } else {
                            String type = null;
                            if (previousLexeme.equals("program")) {
                                type = previousLexeme;
                            }
                            if (previousLexeme.equals("@")) {
                                if (beforePreviousLexeme.equals("int") || beforePreviousLexeme.equals("real") || beforePreviousLexeme.equals("label")) {
                                    type = beforePreviousLexeme;
                                }
                                if (beforePreviousLexeme.equals(",")) {
                                    type = idns.get(idns.size() - 1).getType();
                                }
                            }
                            Identifier identifier = new Identifier(lexemes.get(i), type);
                            if (idns.contains(identifier)) {
                                if (isDeclaration) {
                                    throw new IllegalArgumentException("Дублювання лексеми у строці " + lineNumber);
                                }
                                identifier = idns.get(idns.indexOf(identifier));
                                Identifier.subtractCount();
                            } else {
                                if (!isDeclaration) {
                                    throw new IllegalArgumentException("Неоголошена лексема у строці " + lineNumber);
                                }
                                idns.add(identifier);
                            }
                            lexeme = new Lexeme(lineNumber, lexemes.get(i), inputLexemes.indexOf("idn"), identifier);
                        }
                    }
                }
                lexeme.setLexemeNumber(number);
                outputLexemes.add(lexeme);
                System.out.println(lexemes.get(i));
                previousLexeme = lexemes.get(i);
                if (i != 0)  {
                    beforePreviousLexeme = lexemes.get(i-1);
                }
            }
            if (lexemes.get(i).equals("¶")) {
                lineNumber++;
            }
            collapseLexemes();
        }
    }

    private void collapseLexemes(){
        Lexeme beforePrevious = null;
        Lexeme previous = null;

        List<Integer> indexesToDelete = new ArrayList<>();
        LexemChecker checker = new LexemChecker();

        for(Lexeme currentLexeme: outputLexemes){
            if(checker.isConstant(currentLexeme) &&
                checker.isSign(previous) &&
                checker.isValid(beforePrevious)) {

                indexesToDelete.add(outputLexemes.lastIndexOf(previous));
                currentLexeme.setLexemeName(previous.getLexemeName() + currentLexeme.getLexemeName());
                Constant.subtractCount();

                Constant constant = new Constant(currentLexeme.getLexemeName());
                if (cons.contains(constant)) {
                    Constant.subtractCount();
                } else {
                    cons.add(constant);
                }
            }
            beforePrevious = previous;
            previous = currentLexeme;
        }

        for(int i = 0; i < indexesToDelete.size(); i++){
            outputLexemes.remove(indexesToDelete.get(i) - i);
        }

        cons.removeIf(constant ->
                outputLexemes
                .stream()
                .noneMatch(lexeme -> lexeme.getLexemeName().equals(constant.getValue()))
        );
    }

    private void TableOfLexemes () {
        inputLexemes.add(null);
        inputLexemes.add("program");
        inputLexemes.add("int");
        inputLexemes.add("real");
        inputLexemes.add("label");
        inputLexemes.add("begin");
        inputLexemes.add("cout");
        inputLexemes.add("cin");
        inputLexemes.add("for");
        inputLexemes.add("to");
        inputLexemes.add("by");
        inputLexemes.add("while");
        inputLexemes.add("end");
        inputLexemes.add("end.");
        inputLexemes.add("and");
        inputLexemes.add("or");
        inputLexemes.add("not");
        inputLexemes.add("if");
        inputLexemes.add("then");
        inputLexemes.add("goto");
        inputLexemes.add(",");
        inputLexemes.add("+");
        inputLexemes.add("-");
        inputLexemes.add("*");
        inputLexemes.add("/");
        inputLexemes.add("(");
        inputLexemes.add(")");
        inputLexemes.add("{");
        inputLexemes.add("}");
        inputLexemes.add("[");
        inputLexemes.add("]");
        inputLexemes.add("@");
        inputLexemes.add(":");
        inputLexemes.add("<");
        inputLexemes.add(">");
        inputLexemes.add("=");
        inputLexemes.add("¶");
        inputLexemes.add("<<");
        inputLexemes.add(">>");
        inputLexemes.add("<=");
        inputLexemes.add(">=");
        inputLexemes.add("==");
        inputLexemes.add("!=");
        inputLexemes.add("idn");
        inputLexemes.add("con");
    }

    public List<Lexeme> getOutputLexemesTable() {
        return outputLexemes;
    }

    public List<String> getInputLexemes(){
        return inputLexemes;
    }

    public List<Identifier> getIdentifiers() {
        return idns;
    }

    public List<Constant> getConstants() { return cons; }

    public ArrayList<Identifier> getIdentifiersArrayList(){
        ArrayList<Identifier> identifiersArrayList = new ArrayList<>();
        for (Identifier idn: idns) {
            identifiersArrayList.add(idn);
        }
        return identifiersArrayList;
    }

    public static void reset(){
        Lexeme.reset();
        Identifier.reset();
        Constant.reset();
    }
}
