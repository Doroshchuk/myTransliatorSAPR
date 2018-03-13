package com.company;

/**
 * Created by Dasha on 26.02.2017.
 */

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class RisingParsingTable {
    private LinkedList<String> stack;
    private LinkedList<String> inputLexemes;
    private LinkedHashMap<String, String[][]> grammarMap;
    private String[][] grammarTable;
    private Integer line;
    private List<Lexeme> lexemes;
    private List<RisingParsingTableData> result;

    public List<RisingParsingTableData> getResult() {
        return result;
    }

    public void setResult(List<RisingParsingTableData> result) {
        this.result = result;
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public void setLexemes(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public RisingParsingTable(LexicalAnalyzer analyzer) throws FileNotFoundException {
        stack = new LinkedList<>();
        inputLexemes = new LinkedList<>();
        line = 1;
        setLexemes(analyzer.getOutputLexemesTable());
        GrammarRatioOfPrecedence tree = new GrammarRatioOfPrecedence("D:\\САПР\\1-5\\SAPR_Transliator\\src\\com\\company\\grammarTable.txt");
        grammarTable = tree.getPrecedenceTable();
        grammarMap = tree.getGrammarMap();
    }

    public void analyze() {
        ArrayList<RisingParsingTableData> parsedRisingElements;
        parsedRisingElements = parseInformation();
        ArrayList<RisingParsingTableData> result = parsedRisingElements
                .stream()
                .map(el -> new RisingParsingTableData(el.getStep(), el.getStack(), el.getSign(), el.getInput()))
                .collect(Collectors.toCollection(ArrayList::new));
        setResult(result);
    }

    public ArrayList<RisingParsingTableData> parseInformation() {
        String stackLexeme;
        String sign;
        ArrayList<RisingParsingTableData> parsedData = new ArrayList<>();
        LinkedList<String> inputEl = new LinkedList<>();
        stack.push("#");
        for (Lexeme lexeme : getLexemes()) {
            inputEl.addLast(lexeme.getLexemeName());
            if (lexeme.getConstant() == null && lexeme.getIdentifier() == null) {
                inputLexemes.addLast(lexeme.getLexemeName());
            }
            if (lexeme.getConstant() != null) {
                inputLexemes.addLast("con");
            }
            if (lexeme.getIdentifier() != null) {
                inputLexemes.addLast("idn");
            }
        }
        inputEl.addLast("#");
        inputLexemes.addLast("#");
        for (int i = 0; i < inputLexemes.size(); i++) {
            stackLexeme = stack.peek();
            if (Objects.equals(stack.peek(), "¶")) line++;
            sign = grammarTable[getFirstElementRelation(stackLexeme, grammarTable)][getSecondElementRelation(inputLexemes.get(i), grammarTable)];
            System.out.println(stackLexeme + " + " + inputLexemes.get(i));
            if (Objects.equals(sign, "<∙") || Objects.equals(sign, "=")) {
                parsedData.add(new RisingParsingTableData(parsedData.size(), getStackString(stack), sign, getInputString(inputEl)));
                stack.push(inputLexemes.get(i));
                inputLexemes.pop();
                inputEl.pop();
                i--;
            } else if (Objects.equals(sign, "∙>")) {
                parsedData.add(new RisingParsingTableData(parsedData.size(), getStackString(stack), sign, getInputString(inputEl)));
                findBase();
                i--;
            } else if (!Objects.equals(stackLexeme, "#") && !Objects.equals(inputLexemes.get(i), "#")) {
                throw new IllegalArgumentException("Помилка в рядку " + line + ". Не існує відношення між " + stackLexeme + " та " + inputEl.get(i));
            }
            System.out.println(sign);
        }
        return parsedData;
    }

    private String getStackString(LinkedList<String> stack) {
        String stackString = "";
        for (String string : stack) {
            stackString = string + " " + stackString;
        }
        return stackString;
    }

    private String getInputString(LinkedList<String> input) {
        String inputString = "";
        for (String string : input) {
            inputString += string + " ";
        }
        return inputString;
    }

    private HashMap<String, String> reverseGrammar(HashMap<String, String[][]> grammar) {
        HashMap<String, String> reversedGrammarMap = new HashMap<>();
        for (String key : grammar.keySet()) {
            for (String[] key1 : grammar.get(key)) {
                StringBuilder builder = new StringBuilder();
                for (String word : key1) {
                    builder.append(word);
                }
                reversedGrammarMap.put(builder.toString(), key);
            }
        }
        return reversedGrammarMap;
    }

    private void findBase() {
        StringBuilder newLexeme = new StringBuilder();
        LinkedList<String> localStack = new LinkedList<>();
        String firstStackElement = stack.pop();

        while (getRelation(stack.peek(), firstStackElement).equals("=")) {
            localStack.push(firstStackElement);
            firstStackElement = stack.pop();
        }
        localStack.push(firstStackElement);

        while (!localStack.isEmpty()) {
            newLexeme.append(localStack.pop());
        }

        HashMap<String, String> reversedGrammarMap = reverseGrammar(grammarMap);
        if (reversedGrammarMap.containsKey(newLexeme.toString())) {
            String newValue = reversedGrammarMap.get(newLexeme.toString());
            stack.push(newValue);
        } else if (!Objects.equals(newLexeme.toString(), "<програма>")) {
            throw new IllegalArgumentException("Помилка в рядку " + line + ". Не можна перетворити ланцюжок " + newLexeme.toString());
        }
    }

    private String getRelation(String elem1, String elem2) {
        return grammarTable[getFirstElementRelation(elem1, grammarTable)][getSecondElementRelation(elem2, grammarTable)];
    }

    private int getFirstElementRelation(String lexeme, String[][] table) {
        for (int i = 1; i < table.length; i++) {
            if (Objects.equals(table[i][0], lexeme)) {
                return i;
            }
        }
        return -1;
    }

    private int getSecondElementRelation(String lexeme, String[][] table) {
        for (int j = 1; j < table.length; j++) {
            if (Objects.equals(table[0][j], lexeme)) {
                return j;
            }
        }
        return -1;
    }
}
