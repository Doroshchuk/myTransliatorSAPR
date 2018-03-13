package com.company;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by Dasha on 23.04.2017.
 */

public class Poliz {
    private ArrayList<PolizWorkItem> inputWorkItems;
    private ArrayList<PriorityTable> tablePriority;
    private Stack<PolizWorkItem> stack = new Stack<>();
    ArrayList<PolizWorkItem> poliz;
    private ArrayList<PolizLabel> polizLabels;
    public ArrayList<PolizLabel> programLabels;
    private ArrayList<PolizCell> polizWorkCells;
    private boolean isLoop;
    private int loopFeature;
    private String loopVariable;
    private ArrayList<PolizInformation> polizInformations;

    public Poliz(ArrayList<PolizWorkItem> input) {
        inputWorkItems = input;
        polizInformations = new ArrayList<>();
        tablePriority = new ArrayList<>();
        poliz = new ArrayList<>();
        polizLabels = new ArrayList<>();
        polizWorkCells = new ArrayList<>();
        programLabels = new ArrayList<>();
        tablePriority.add(new PriorityTable("begin", 0));
        tablePriority.add(new PriorityTable("if", 1));
        tablePriority.add(new PriorityTable("for", 1));
        tablePriority.add(new PriorityTable("end.", 1));
        tablePriority.add(new PriorityTable("cout", 1));
        tablePriority.add(new PriorityTable("cin", 1));
        tablePriority.add(new PriorityTable("{", 1));
        tablePriority.add(new PriorityTable("[", 1));
        tablePriority.add(new PriorityTable("(", 1));
        tablePriority.add(new PriorityTable("<<", 1));
        tablePriority.add(new PriorityTable(">>", 1));
        tablePriority.add(new PriorityTable("¶", 1));
        tablePriority.add(new PriorityTable("while", 2));
        tablePriority.add(new PriorityTable("then", 2));
        tablePriority.add(new PriorityTable("end", 2));
        tablePriority.add(new PriorityTable("to", 2));
        tablePriority.add(new PriorityTable("by", 2));
        tablePriority.add(new PriorityTable(")", 2));
        tablePriority.add(new PriorityTable("}", 2));
        tablePriority.add(new PriorityTable("]", 2));
        tablePriority.add(new PriorityTable("goto", 3));
        tablePriority.add(new PriorityTable("=", 3));
        tablePriority.add(new PriorityTable(":", 3));
        tablePriority.add(new PriorityTable("or", 4));
        tablePriority.add(new PriorityTable("and", 5));
        tablePriority.add(new PriorityTable("not", 6));
        tablePriority.add(new PriorityTable("<", 7));
        tablePriority.add(new PriorityTable(">", 7));
        tablePriority.add(new PriorityTable("<=", 7));
        tablePriority.add(new PriorityTable(">=", 7));
        tablePriority.add(new PriorityTable("==", 7));
        tablePriority.add(new PriorityTable("!=", 7));
        tablePriority.add(new PriorityTable("+", 8));
        tablePriority.add(new PriorityTable("-", 8));
        tablePriority.add(new PriorityTable("*", 9));
        tablePriority.add(new PriorityTable("/", 9));
        polizLabels = polizLabels
                .stream()
                .map(el -> new PolizLabel(el.getName(), el.getPosition()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void doWork() {
        while (inputWorkItems.size() > 0 && !Objects.equals(inputWorkItems.get(0).getName(), "begin")) {
            System.out.println(inputWorkItems.get(0).getName());
            inputWorkItems.remove(0);
        }
        while (inputWorkItems.size() > 0) {
            polizInformations.add(new PolizInformation(getInputLexeme(), getStackToString(), getPolizString()));
            if (Objects.equals(inputWorkItems.get(0).getType(), "idn") || Objects.equals(inputWorkItems.get(0).getType(), "con") || Objects.equals(inputWorkItems.get(0).getType(), "lbl")) {
                poliz.add(inputWorkItems.get(0));
                if (inputWorkItems.get(0).getType().equals("lbl") && inputWorkItems.get(1).getName().equals(":")) {
                    programLabels.add(new PolizLabel(inputWorkItems.get(0).getName(), programLabels.size() + 1));
                    poliz.get(poliz.size() - 1).setName(inputWorkItems.get(0).getName() + inputWorkItems.get(1).getName());
                    inputWorkItems.remove(0);
                }
                inputWorkItems.remove(0);
            } else {
                if (stack.size() == 0) {
                    stack.push(inputWorkItems.get(0));
                    inputWorkItems.remove(0);
                } else {
                    if (Objects.equals(inputWorkItems.get(0).getName(), "for")) {
                        String newLabel1 = "m" + (polizLabels.size() + 1);
                        String newLabel2 = "m" + (polizLabels.size() + 2);
                        String newLabel3 = "m" + (polizLabels.size() + 3);
                        polizLabels.add(new PolizLabel(newLabel1, polizLabels.size() + 1));
                        polizLabels.add(new PolizLabel(newLabel2, polizLabels.size() + 1));
                        polizLabels.add(new PolizLabel(newLabel3, polizLabels.size() + 1));
                        stack.push(new PolizWorkItem(newLabel3, "label"));
                        stack.push(new PolizWorkItem(newLabel2, "label"));
                        stack.push(new PolizWorkItem(newLabel1, "label"));
                        stack.push(inputWorkItems.get(0));
                        inputWorkItems.remove(0);
                        isLoop = true;
                        loopFeature++;
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "=") && isLoop) {
                        if (loopFeature == 1) {
                            loopVariable = poliz.get(poliz.size() - 1).getName();
                            loopFeature++;
                        }
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "to")) {
                        while (!(stack.peek().getName().equals("for"))) {
                            poliz.add(stack.pop());
                        }
                        String newWorkCell1 = "r" + (polizWorkCells.size() + 1);
                        String newWorkCell2 = "r" + (polizWorkCells.size() + 2);
                        String newWorkCell3 = "r" + (polizWorkCells.size() + 3);
                        polizWorkCells.add(new PolizCell(newWorkCell1));
                        polizWorkCells.add(new PolizCell(newWorkCell2));
                        polizWorkCells.add(new PolizCell(newWorkCell3));
                        poliz.add(new PolizWorkItem(newWorkCell1, "cell"));
                        poliz.add(new PolizWorkItem("1", "con"));
                        poliz.add(new PolizWorkItem("=", "operation"));
                        poliz.add(new PolizWorkItem(stack.elementAt(stack.size() - 2).getName() + ":", "label"));
                        poliz.add(new PolizWorkItem(newWorkCell3, "cell"));
                        inputWorkItems.remove(0);
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "by")) {
                        while (!stack.peek().getName().equals("for")) {
                            poliz.add(stack.pop());
                        }
                        poliz.add(new PolizWorkItem("=", "operation"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 2).getName(), "cell"));
                        inputWorkItems.remove(0);
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "while")) {
                        while (!stack.peek().getName().equals("for")) {
                            poliz.add(stack.pop());
                        }
                        poliz.add(new PolizWorkItem("=", "operation"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 3).getName(), "cell"));
                        poliz.add(new PolizWorkItem("0", "con"));
                        poliz.add(new PolizWorkItem("==", "operation"));
                        poliz.add(stack.elementAt(stack.size() - 3));
                        poliz.add(new PolizWorkItem("УПХ", "operation"));
                        poliz.add(new PolizWorkItem(loopVariable, "idn"));
                        poliz.add(new PolizWorkItem(loopVariable, "idn"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 2).getName(), "cell"));
                        poliz.add(new PolizWorkItem("+", "operation"));
                        poliz.add(new PolizWorkItem("=", "operation"));
                        poliz.add(new PolizWorkItem(stack.elementAt(stack.size() - 3).getName() + ":", "label"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 3).getName(), "cell"));
                        poliz.add(new PolizWorkItem("0", "con"));
                        poliz.add(new PolizWorkItem("=", "operation"));
                        poliz.add(new PolizWorkItem(loopVariable, "idn"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 1).getName(), "cell"));
                        poliz.add(new PolizWorkItem("-", "operation"));
                        poliz.add(new PolizWorkItem(polizWorkCells.get(polizWorkCells.size() - 2).getName(), "cell"));
                        poliz.add(new PolizWorkItem("*", "operation"));
                        poliz.add(new PolizWorkItem("0", "con"));
                        poliz.add(new PolizWorkItem("<=", "operation"));
                        inputWorkItems.remove(0);
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "cout") || Objects.equals(inputWorkItems.get(0).getName(), "cin")) {
                        stack.push(inputWorkItems.get(0));
                        inputWorkItems.remove(0);
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "{") || Objects.equals(inputWorkItems.get(0).getName(), "(") || Objects.equals(inputWorkItems.get(0).getName(), "[")) {
                        stack.push(inputWorkItems.get(0));
                        inputWorkItems.remove(0);
                        continue;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "goto")) {
                        if (Objects.equals(inputWorkItems.get(1).getType(), "lbl")) {
                            stack.push(inputWorkItems.get(1));
                            stack.push(inputWorkItems.get(0));
                            inputWorkItems.remove(0);
                            inputWorkItems.remove(0);
                            continue;
                        }
                    } else if (isLoop && Objects.equals(inputWorkItems.get(0).getName(), "}")) {
                        while (!stack.peek().getName().equals("{")) {
                            poliz.add(stack.pop());
                        }
                        poliz.add(new PolizWorkItem("and", "operation"));
                        poliz.add(stack.elementAt(stack.size() - 5));
                        poliz.add(new PolizWorkItem("УПХ", "operation"));
                        stack.pop();
                        inputWorkItems.remove(0);
                        isLoop = false;
                        continue;
                    }

                    int firstPriority = getPriority(stack.peek().getName());
                    int secondPriority = getPriority(inputWorkItems.get(0).getName());

                    while (firstPriority >= secondPriority && stack.size() > 0) {
                        if (isLoop && Objects.equals(stack.peek().getName(), "for"))
                            break;
                        if (loopFeature > 0 && Objects.equals(stack.peek().getName(), "for"))
                            break;
                        if (Objects.equals(stack.peek().getName(), "cout") || Objects.equals(stack.peek().getName(), "cin"))
                            if (Objects.equals(inputWorkItems.get(0).getName(), "<<") || Objects.equals(inputWorkItems.get(0).getName(), ">>"))
                                break;
                            else if (Objects.equals(inputWorkItems.get(0).getName(), "¶")) {
                                stack.pop();
                                if (stack.size() > 0)
                                    firstPriority = getPriority(stack.peek().getName());
                                continue;
                            }
                        if (Objects.equals(stack.peek().getName(), "if")) {
                            Stack<PolizWorkItem> temp = new Stack<>();
                            temp.push(stack.pop());
                            while (Objects.equals(stack.peek().getType(), "label"))
                                temp.push(stack.pop());
                            poliz.add(new PolizWorkItem(temp.pop().getName() + ":", "label"));
                            if (stack.size() > 0)
                                firstPriority = getPriority(stack.peek().getName());
                            continue;
                        }
                        if (Objects.equals(stack.peek().getName(), "goto")) {
                            stack.pop();
                            poliz.add(stack.pop());
                            poliz.add(new PolizWorkItem("БП", "operation"));
                            if (stack.size() > 0)
                                firstPriority = getPriority(stack.peek().getName());
                        } else {
                            poliz.add(stack.pop());
                            if (stack.size() > 0)
                                firstPriority = getPriority(stack.peek().getName());
                        }
                    }

                    if (Objects.equals(inputWorkItems.get(0).getName(), ")") || Objects.equals(inputWorkItems.get(0).getName(), "}") || Objects.equals(inputWorkItems.get(0).getName(), "]")) {
                            while (!Objects.equals(stack.peek().getName(), "(") || Objects.equals(inputWorkItems.get(0).getName(), "{") || Objects.equals(inputWorkItems.get(0).getName(), "[")) {
                                poliz.add(stack.pop());
                            }
                            inputWorkItems.remove(0);
                            stack.pop();
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "¶")){
                        inputWorkItems.remove(0);
                    } else if (inputWorkItems.get(0).getName().equals("end.")) {
                        while (!stack.peek().getName().equals("begin"))
                            if (Objects.equals(stack.peek().getName(), "if")) {
                                Stack<PolizWorkItem> temp = new Stack<>();
                                temp.push(stack.pop());
                                while (Objects.equals(stack.peek().getName(), "label"))
                                    temp.push(stack.pop());
                                poliz.add(new PolizWorkItem(temp.pop().getName() + ":", "label"));
                            } else {
                                poliz.add(stack.pop());
                            }
                        inputWorkItems.remove(0);
                        stack.pop();
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "then")) {
                        while (!Objects.equals(stack.peek().getName(), "if"))
                            poliz.add(stack.pop());
                        Stack<PolizWorkItem> temp = new Stack<>();
                        temp.push(stack.pop());
                        while (Objects.equals(stack.peek().getName(), "label"))
                            temp.push(stack.pop());
                        String newLabel = "m" + (polizLabels.size() + 1);
                        polizLabels.add(new PolizLabel(newLabel, polizLabels.size() + 1));
                        stack.push(new PolizWorkItem(newLabel, "label"));
                        while (temp.size() > 0)
                            stack.push(temp.pop());
                        poliz.add(new PolizWorkItem(newLabel, "label"));
                        poliz.add(new PolizWorkItem("УПХ", "operation"));
                        inputWorkItems.remove(0);
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "end")) {
                        while (!Objects.equals(stack.peek().getName(), "for")) {
                            if (!Objects.equals(stack.peek().getName(), "cout") && !Objects.equals(stack.peek().getName(), "cin"))
                                poliz.add(stack.pop());
                            else
                                stack.pop();
                        }
                        stack.pop();
                        if (stack.size() > 1) {
                            poliz.add(stack.pop());
                            stack.pop();
                            poliz.add(new PolizWorkItem("БП", "operation"));
                        }
                        poliz.add(new PolizWorkItem(stack.pop().getName() + ":", "label"));
                        inputWorkItems.remove(0);
                        loopFeature--;
                    } else if (Objects.equals(inputWorkItems.get(0).getName(), "end.")) {
                        while (!Objects.equals(stack.peek().getName(), "begin")) {
                            poliz.add(stack.pop());
                        }
                        inputWorkItems.remove(0);
                        stack.pop();
                        loopFeature--;
                    } else {
                        stack.push(inputWorkItems.get(0));
                        inputWorkItems.remove(0);
                    }
                }
            }
        }
        setRightPosition();
    }

    private int getPriority(String name) {
        int i;
        for (i = 0; i < tablePriority.size(); i++) {
            if (Objects.equals(tablePriority.get(i).getName(), name)){
                return tablePriority.get(i).getPriority();
            }
        }
        i = -1;
        return i;
    }

    private void setRightPosition(){
        for (PolizWorkItem polizElement: poliz){
            for (PolizLabel label: polizLabels) {
                if (polizElement.getType().equals("label") && polizElement.getName().equals(label.getName() + ":")){
                    label.setPosition(poliz.indexOf(polizElement) + 1);
                }
            }
            for (PolizLabel label: programLabels) {
                if (polizElement.getType().equals("lbl") && polizElement.getName().equals(label.getName() + ":")){
                    label.setPosition(poliz.indexOf(polizElement) + 1);
                    System.out.println(label.getPosition());
                }
            }
        }
    }

    public String getPolizString() {
        String result = "";
        for (int i = 0; i < poliz.size(); i++)
            result += poliz.get(i).getName() + " ";
        return result;
    }

    public ArrayList<PolizLabel> getLabels() {
        return polizLabels;
    }

    public ArrayList<PolizExecutionIdentification> getPolizWorkCells(){
        ArrayList<PolizExecutionIdentification> cells = new ArrayList<>();
        for (PolizCell cell : polizWorkCells){
            cells.add(new PolizExecutionIdentification(cell.getName(),"cell", cell.getValue()));
        }
        return cells;
    }

    public ArrayList<PolizLabel> getPolizProgramLabels(){
        return programLabels;
    }

    private String getStackToString(){
        String temp = "";
        for (PolizWorkItem item: stack){
            temp += item.getName() + " ";
        }
        return  temp;
    }

    private  String getInputLexeme(){
        return  inputWorkItems.get(0).getName();
    }

    public ArrayList<PolizInformation> getPolizInformations() {
        return polizInformations;
    }
}
