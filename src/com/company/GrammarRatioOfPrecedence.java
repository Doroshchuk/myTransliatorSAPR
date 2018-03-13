package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Dasha on 03.12.2016.
 */

public class GrammarRatioOfPrecedence {
    private LinkedHashMap<String, String[][]> grammarMap;
    private LinkedHashSet<String> terminalLexemesSet;
    private String errors;

    public LinkedHashMap<String, String[][]> getGrammarMap() {
        return grammarMap;
    }

    public String getErrors() {
        return errors;
    }

    public GrammarRatioOfPrecedence (String file) throws FileNotFoundException {
        errors = "";
        grammarMap = new LinkedHashMap<>();
        terminalLexemesSet = new LinkedHashSet<>();
        parseGrammarLexemesFile(file);
    }


    private void parseGrammarLexemesFile(String file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(fis);
        String[] entireRow;
        String[] partRow;
        String[][] entireArray;

        while (scanner.hasNextLine()) {
            entireRow = scanner.nextLine().split(" *::= *");
            partRow = entireRow[1].split("\\|");
            entireArray = new String[partRow.length][];
            for (int i = 0; i < entireArray.length; i++) {
                entireArray[i] = partRow[i].split(" +");
            }
            grammarMap.put(entireRow[0], entireArray);
        }

        scanner.close();
        try {
            checking();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        addTerminalLexemes();
    }

    private void checking() {
        for (String key : grammarMap.keySet()) {
            for (String[] line : grammarMap.get(key)) {
                for (String lexeme : line) {
                    if (!grammarMap.containsKey(lexeme) && isNotTerminal(lexeme)) {
                        throw new IllegalArgumentException(key + " не содержит :" + lexeme + "\n");
                    }
                }
            }
        }
    }

    private boolean isNotTerminal(String lexeme) {
        return lexeme.length() > 2 && lexeme.charAt(0) == '<' && lexeme.charAt(lexeme.length() - 1) == '>';
    }

    private void addTerminalLexemes() {
        for (String key : grammarMap.keySet()) {
            for (String[] line : grammarMap.get(key)) {
                for (String lexeme : line) {
                    if (!isNotTerminal(lexeme) && !terminalLexemesSet.contains(lexeme)) {
                        terminalLexemesSet.add(lexeme);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : grammarMap.keySet()) {
            builder.append(key);
            builder.append(":=");
            for (String[] line : grammarMap.get(key)) {
                for (String subLine : line) {
                    builder.append(subLine);
                }
                builder.append("|");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private LinkedHashMap<String, Integer> getLexemesMap() {
        int index = 0;
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (String key : grammarMap.keySet()) {
            map.put(key, index++);
        }
        for (String key : terminalLexemesSet) {
            map.put(key, index++);
        }
        map.put("#", index);
        return map;
    }

    private String[][] createPrecedenceTable(LinkedHashMap<String, Integer> lexemesMap) {
        String[][] table = new String[lexemesMap.size() + 1][lexemesMap.size() + 1];
        int index = 1;
        for (String key : lexemesMap.keySet()) {
            table[0][index] = key;
            table[index++][0] = key;
        }
        return table;
    }

    private HashMap<String, Iterable<String>> getFirstsMap(LinkedHashMap<String, Integer> lexemesMap) {
        HashMap<String, Iterable<String>> firstMap = new HashMap<>();
        for (String key : lexemesMap.keySet()) {
            if (isNotTerminal(key)) {
                firstMap.put(key, getFirstPlus(key));
            }
        }
        return firstMap;
    }

    private HashMap<String, Iterable<String>> getLastMap(LinkedHashMap<String, Integer> lexemesMap) {
        HashMap<String, Iterable<String>> lastMap = new HashMap<>();
        for (String key : lexemesMap.keySet()) {
            if (isNotTerminal(key)) {
                lastMap.put(key, getLastPlus(key));
            }
        }
        return lastMap;
    }

    private TreeSet<String> getFirstPlus(String parent) {
        LinkedList<String> queue = new LinkedList<>();
        TreeSet<String> firsts = new TreeSet<>();
        queue.push(parent);
        String lexeme;
        while (!queue.isEmpty()) {
            lexeme = queue.removeFirst();
            for (String[] line : grammarMap.get(lexeme)) {
                if (!firsts.contains(line[0])) {
                    firsts.add(line[0]);
                    if (isNotTerminal(line[0])) {
                        queue.add(line[0]);
                    }
                }
            }
        }
        return firsts;
    }

    private TreeSet<String> getLastPlus(String parent) {
        LinkedList<String> queue = new LinkedList<>();
        TreeSet<String> lasts = new TreeSet<>();
        queue.push(parent);
        String lexeme;
        while (!queue.isEmpty()) {
            lexeme = queue.removeFirst();
            for (String[] line : grammarMap.get(lexeme)) {
                if (!lasts.contains(line[line.length - 1])) {
                    lasts.add(line[line.length - 1]);
                    if (isNotTerminal(line[line.length - 1])) {
                        queue.add(line[line.length - 1]);
                    }
                }
            }
        }
        return lasts;
    }

    private void fillingOfEquals(String[][] precedenceTable, LinkedHashMap<String, Integer> lexemesMap) {
        for (String[][] line : grammarMap.values()) {
            for (String[] lexemes : line) {
                for (int i = 0; i < lexemes.length - 1; i++) {
                    precedenceTable[lexemesMap.get(lexemes[i]) + 1][lexemesMap.get(lexemes[i + 1]) + 1] = "=";
                }
            }
        }
    }

    private void fillingOfLess(String[][] precedenceTable, LinkedHashMap<String, Integer> lexemesMap
            , HashMap<String, Iterable<String>> firstMap) {
        for (int i = 1; i < precedenceTable.length; i++) {
            for (int j = 1; j < precedenceTable[i].length; j++) {
                if (precedenceTable[i][j] != null && precedenceTable[i][j].equals("=")) { // находим отношение R=V, V - нетерминал
                    if (firstMap.containsKey(precedenceTable[0][j])) { // проверяем есть ли First+(V)
                        for (String lexeme : firstMap.get(precedenceTable[0][j])) { // перебираем элементы First+
                            if (precedenceTable[i][lexemesMap.get(lexeme) + 1] == null
                                    || Objects.equals(precedenceTable[i][lexemesMap.get(lexeme) + 1], "<∙")) {
                                precedenceTable[i][lexemesMap.get(lexeme) + 1] = "<∙"; // устанавливаем < между R i First+
                            } else {
                                errors += "Конфликт <∙. Отношение " + precedenceTable[i][0] + " и "
                                        + precedenceTable[0][lexemesMap.get(lexeme) + 1] + " уже существует "
                                        + precedenceTable[i][lexemesMap.get(lexeme) + 1] + "\n";
                            }
                        }
                    }
                }
            }
        }
    }

    private void fillingOfMore(String[][] precedenceTable, LinkedHashMap<String, Integer> lexemesMap
            , HashMap<String, Iterable<String>> firstMap, HashMap<String, Iterable<String>> lastMap) {
        for (int i = 1; i < precedenceTable.length; i++) {
            for (int j = 1; j < precedenceTable[i].length; j++) {
                if (precedenceTable[i][j] != null && precedenceTable[i][j].equals("=")) {
                    if (lastMap.containsKey(precedenceTable[i][0])) {
                        if (isNotTerminal(precedenceTable[0][j])) { // пункт 3.2
                            for (String lexeme : lastMap.get(precedenceTable[i][0])) { // Last+(R)
                                for (String lexemeS : firstMap.get(precedenceTable[0][j])) {// First+(V)
                                    if (precedenceTable[lexemesMap.get(lexeme) + 1][lexemesMap.get(lexemeS) + 1] == null ||
                                            Objects.equals(precedenceTable[lexemesMap.get(lexeme) + 1][lexemesMap.get(lexemeS) + 1], "∙>")) {
                                        precedenceTable[lexemesMap.get(lexeme) + 1][lexemesMap.get(lexemeS) + 1] = "∙>";
                                    } else {
                                        errors += "Конфликт ∙>. Отношение " + precedenceTable[lexemesMap.get(lexeme) + 1][0] + " + "
                                                + precedenceTable[0][lexemesMap.get(lexemeS) + 1] + ") уже существует " +
                                                precedenceTable[lexemesMap.get(lexeme) + 1][j] + "\n";
                                    }
                                }
                            }
                        } // пункт 3.1
                        for (String lexeme : lastMap.get(precedenceTable[i][0])) { // Last+(R)
                            if (precedenceTable[lexemesMap.get(lexeme) + 1][j] == null ||
                                    Objects.equals(precedenceTable[lexemesMap.get(lexeme) + 1][j], "∙>")) {
                                precedenceTable[lexemesMap.get(lexeme) + 1][j] = "∙>";
                            } else {
                                errors += "Конфликт ∙>. Отношение " + precedenceTable[lexemesMap.get(lexeme) + 1][0] + " + "
                                        + precedenceTable[0][j] + " уже существует " + precedenceTable[lexemesMap.get(lexeme) + 1][j] + "\n";
                            }
                        }

                    }
                }
            }
        }
    }


    private void fillingOfSharp(String[][] precedenceTable) {
        for (int i = 1; i < precedenceTable.length - 1; i++) {
            precedenceTable[precedenceTable.length - 1][i] = "<∙";
            precedenceTable[i][precedenceTable.length - 1] = "∙>";
        }
    }


    public String[][] getPrecedenceTable() {
        LinkedHashMap<String, Integer> lexemesMap = getLexemesMap();
        String[][] precedenceTable = createPrecedenceTable(lexemesMap);
        HashMap<String, Iterable<String>> firstPlusMap = getFirstsMap(lexemesMap);
        HashMap<String, Iterable<String>> lastPlusMap = getLastMap(lexemesMap);
        fillingOfEquals(precedenceTable, lexemesMap);
        fillingOfLess(precedenceTable, lexemesMap, firstPlusMap);
        fillingOfMore(precedenceTable, lexemesMap, firstPlusMap, lastPlusMap);
        fillingOfSharp(precedenceTable);
        return precedenceTable;
    }

    public static void main(String argv[]) throws FileNotFoundException {
        GrammarRatioOfPrecedence grammarRatioOfPrecedence = new GrammarRatioOfPrecedence("grammarTable.txt");
        String[][] table = grammarRatioOfPrecedence.getPrecedenceTable();
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] != null)
                    System.out.printf("%13s", table[i][j]);
                else
                    System.out.print("       |      ");
            }
            System.out.println();
        }
    }
}
