package com.company;

import javafx.scene.control.TextArea;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dasha on 15.05.2017.
 */
public class PolizExecution {
    private ArrayList<PolizWorkItem> poliz;
    private ArrayList<PolizLabel> polizLabels;
    private ArrayList<PolizLabel> polizProgramLabels;
    private ArrayList<PolizExecutionIdentification> polizCells;
    private ArrayList<PolizExecutionIdentification> identifiersTable;
    private Stack<PolizWorkItem> stack;
    private TextArea text;
    private String status;
    private int position;
    private String nameOfOutputParameter;
    Object outputParameter;
    private ArrayList<PolizExecutionInformation> polizExecutionInformations;

    public PolizExecution(ArrayList<PolizWorkItem> poliz, ArrayList<PolizLabel> polizLabels, ArrayList<PolizLabel> polizProgramLabels, ArrayList<PolizExecutionIdentification> polizCells, ArrayList<Identifier> identifiersTable, TextArea text) {
        this.poliz = poliz;
        this.polizLabels = polizLabels;
        this.polizCells = polizCells;
        this.polizProgramLabels = polizProgramLabels;
        polizExecutionInformations = new ArrayList<>();
        this.text = text;
        status = "";
        position = 0;
        stack = new Stack<>();
        this.identifiersTable = new ArrayList<>();
        for (Identifier identifier : identifiersTable) {
            this.identifiersTable.add(new PolizExecutionIdentification(identifier.getName(), identifier.getType(), null));
        }
        executePoliz();
        for (Identifier identifier : identifiersTable) {
            for (PolizExecutionIdentification idn : this.identifiersTable){
                if (identifier.getName().equals(idn.getName()) && identifier.getType().equals(idn.getType()))
                    identifier.setValue(idn.getValue());
            }
        }
    }

    private String getStackToString(){
        String temp = "";
        for (PolizWorkItem item: stack){
            temp += item.getName() + " ";
        }
        return  temp;
    }

    private void executePoliz() {
        //System.out.println(poliz.size());
        while (Objects.equals(status, "")) {
            while (Objects.equals(status, "")) {
                //System.out.println(position);
                executeNextStep();
            }
            if (status.contains("output")) {
                if (status.equals("output int") || status.equals("output real") || status.equals("output con")) {
                    text.appendText(nameOfOutputParameter + ": " + outputParameter.toString() + "\n");
                }
                status = "";
                position++;
            } else if (status.contains("input")) {
                JFrame frame = new JFrame("Поле для вводу значення змінної");
                String value = JOptionPane.showInputDialog(frame, "Ідентифікатор: " + stack.peek().getName());
                status = "";
                position++;
                if (isNumber(value)) {
                    for (PolizExecutionIdentification identifier : identifiersTable) {
                        if (identifier.getName().equals(stack.peek().getName())) {
                            if (identifier.getType().equals("int")) {
                                identifier.setValue(Integer.parseInt(String.valueOf(value)));
                            } else if (identifier.getType().equals("real")) {
                                identifier.setValue(Double.parseDouble(String.valueOf(value)));
                            }
                        }
                    }
                } else throw new IllegalArgumentException("Неправильно введене значення змінної");
            }
        }
    }

    public static boolean isNumber(String str){
        Pattern p = Pattern.compile("(^0$)|(^[1-9]([0-9])*)$");
        Matcher m = p.matcher(str);
        return m.find();
    }

    private void executeNextStep() {
        if (position != poliz.size())
        {
            polizExecutionInformations.add(new PolizExecutionInformation(poliz.get(position).getName(), getStackToString()));
        }
        //System.out.println(poliz.get(position));
        status = "";
        nameOfOutputParameter = "";
        if (position > poliz.size() - 1) {
            status = "Виконання полізу пройшло успішно!";
            return;
        }
        if ((poliz.get(position).getType().equals("label") || poliz.get(position).getType().equals("lbl")) && poliz.get(position).getName().endsWith(":")) {
            position++;
        } else if (poliz.get(position).getType().equals("idn") || poliz.get(position).getType().equals("con") || poliz.get(position).getType().equals("lbl") || poliz.get(position).getType().equals("label") || poliz.get(position).getType().equals("cell")) {
            stack.push(poliz.get(position));
            position++;
        } else if (poliz.get(position).getType().equals("operation")) {
            if (poliz.get(position).getName().equals(">>")) {
                status = "input ";
                if (status.length() > 0) {
                    String identifier = stack.peek().getName();
                    for (PolizExecutionIdentification idn : identifiersTable) {
                        if (Objects.equals(idn.getName(), identifier)) {
                            status += idn.getType();
                            return;
                        }
                    }
                    throw new IllegalArgumentException("Неправильно введене значення змінної.");
                }
            } else if (poliz.get(position).getName().equals("<<")) {
                status = "output ";
                if (status.length() > 0) {
                    PolizWorkItem item = stack.peek();
                    if (item.getType().equals("con")){
                        status += item.getType();
                        outputParameter = item.getName();
                        return;
                    } else if (item.getType().equals("idn")){
                        for (PolizExecutionIdentification idn : identifiersTable) {
                            if (Objects.equals(idn.getName(), item.getName())) {
                                status += idn.getType();
                                nameOfOutputParameter += idn.getName();
                                outputParameter = idn.getValue();
                                return;
                            }
                        }throw new IllegalArgumentException("Ідентифікатор не знайдено.");
                    }
                }
            } else if (poliz.get(position).getName().equals("+") || poliz.get(position).getName().equals("/") || poliz.get(position).getName().equals("*") || poliz.get(position).getName().equals("-")) {
                if (stack.size() > 1) {
                    PolizWorkItem item1 = stack.pop();
                    PolizWorkItem item2 = stack.pop();

                    double val2 = getItemValue(item1);
                    double val1 = getItemValue(item2);
                    String operation = poliz.get(position).getName();
                    double res = 0;
                    if (operation.equals("+"))
                        res = val1 + val2;
                    else if (operation.equals("-"))
                        res = val1 - val2;
                    else if (operation.equals("*"))
                        res = val1 * val2;
                    else if (operation.equals("/"))
                        res = val1 / val2;
                    stack.push(new PolizWorkItem(Double.toString(res), "con"));
                    position++;
                } else
                    throw new UnsupportedOperationException();
            } else if (poliz.get(position).getName().equals(">") || poliz.get(position).getName().equals("<") || poliz.get(position).getName().equals("<=") || poliz.get(position).getName().equals(">=") || poliz.get(position).getName().equals("==")) {
                PolizWorkItem item1 = stack.pop();
                PolizWorkItem item2 = stack.pop();

                double val2 = getItemValue(item1);
                double val1 = getItemValue(item2);

                String operation = poliz.get(position).getName();

                boolean res = false;

                if (operation.equals(">"))
                    res = val1 > val2;
                else if (operation.equals("<"))
                    res = val1 < val2;
                else if (operation.equals("<="))
                    res = val1 <= val2;
                else if (operation.equals(">="))
                    res = val1 >= val2;
                else if (operation.equals("=="))
                    res = val1 == val2;
                stack.push(new PolizWorkItem(Boolean.toString(res), "con"));
                position++;
            } else if (poliz.get(position).getName().equals("=")) {
                PolizWorkItem item1 = stack.pop();
                PolizWorkItem item2 = stack.pop();

                double val = getItemValue(item1);

                if (item2.getType().equals("idn")) {
                    for (PolizExecutionIdentification idn : identifiersTable) {
                        if (Objects.equals(idn.getName(), item2.getName())) {
                            if (idn.getType().equals("int")) {
                                idn.setValue((int) Double.parseDouble(String.valueOf(val)));
                            } else if (idn.getType().equals("real")) {
                                idn.setValue(Double.parseDouble(String.valueOf(val)));
                            } else throw new UnsupportedOperationException();
                        }
                    }
                } else if (item2.getType().equals("cell")) {
                    for (PolizExecutionIdentification cell : polizCells) {
                        if (Objects.equals(cell.getName(), item2.getName())) {
                            cell.setValue(Double.parseDouble(String.valueOf(val)));
                        }
                    }
                }
                position++;
            } else if (poliz.get(position).getName().equals("and") || poliz.get(position).getName().equals("or")) {
                PolizWorkItem item1 = stack.pop();
                PolizWorkItem item2 = stack.pop();

                String operation = poliz.get(position).getName();

                boolean res = false;

                if (operation.equals("and"))
                    res = Boolean.parseBoolean(item1.getName()) && Boolean.parseBoolean(item2.getName());
                else if (operation.equals("or"))
                    res = Boolean.parseBoolean(item1.getName()) || Boolean.parseBoolean(item2.getName());
                stack.push(new PolizWorkItem(Boolean.toString(res), "con"));
                position++;
            } else if (poliz.get(position).getName().equals("not")){
                PolizWorkItem item = stack.pop();
                boolean res = !(Boolean.parseBoolean(item.getName()));
                stack.push(new PolizWorkItem(Boolean.toString(res), "con"));
                position++;
            } else if (poliz.get(position).getName().equals("УПХ")) {
                PolizWorkItem item1 = stack.pop();
                PolizWorkItem item2 = stack.pop();

                if (item2.getName().equals("false")) {
                    if (item1.getType().equals("label")){
                        for (PolizLabel label : polizLabels) {
                            if (label.getName().equals(item1.getName())){
                                position = label.getPosition();
                            }
                        }
                    } else if (item1.getType().equals("lbl")){
                        for (PolizLabel label : polizProgramLabels) {
                            if (label.getName().equals(item1.getName())){
                                position = label.getPosition();
                            }
                        }
                    }
                }
                else
                    position++;
            } else if (poliz.get(position).getName().equals("БП")) {
                PolizWorkItem item = stack.pop();

                if (item.getType().equals("label")){
                    for (PolizLabel label : polizLabels) {
                        if (label.getName().equals(item.getName())){
                            position = label.getPosition();
                        }
                    }
                } else if (item.getType().equals("lbl")){
                    for (PolizLabel label : polizProgramLabels) {
                        if (label.getName().equals(item.getName())){
                            position = label.getPosition();
                        }
                    }
                }
            }
        }
    }

    private double getItemValue(PolizWorkItem item)
    {
        double result = 0;
        if (item.getType().equals("con")) {
            return Double.parseDouble(item.getName());
        } else if (item.getType().equals("idn") || item.getType().equals("cell")) {
            if (item.getType().equals("idn")) {
                for (PolizExecutionIdentification idn : identifiersTable) {
                    if (idn.getName().equals(item.getName())){
                        if (Objects.equals(idn.getType(), "int")) {
                            if (idn.getValue() != null) {
                                result = Integer.parseInt(String.valueOf(idn.getValue()));
                            } else throw new NullPointerException("The identify " + idn.getName() + " didn't initialized");
                        } else if (Objects.equals(idn.getType(), "real")) {
                            if (idn.getValue() != null) {
                                result = Double.parseDouble(String.valueOf(idn.getValue()));
                            } else throw new NullPointerException("The identify " + idn.getName() + " didn't initialized");
                        } else throw new IllegalArgumentException();
                    }
                }
            } else {
                for (PolizExecutionIdentification cell : polizCells){
                    if (cell.getName().equals(item.getName())){
                        result = Double.parseDouble(String.valueOf(cell.getValue()));
                    }
                }
            }
        } else throw new IllegalArgumentException();
        return result;
    }

    public ArrayList<PolizExecutionInformation> getPolizExecutionInformations() {
        return polizExecutionInformations;
    }
}