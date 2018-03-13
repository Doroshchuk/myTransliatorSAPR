package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Dasha on 10.10.2016.
 */

public class Controller implements Initializable {
    @FXML
    private TableView<Lexeme> table1;
    @FXML
    private TableColumn<Lexeme,Integer> column1_1;
    @FXML
    private TableColumn<Lexeme,Integer> column1_2;
    @FXML
    private TableColumn<Lexeme,String> column1_3;
    @FXML
    private TableColumn<Lexeme,Integer> column1_4;
    @FXML
    private TableColumn<Lexeme,Integer> column1_5;

    @FXML
    private TableView<Identifier> table2;
    @FXML
    private TableColumn<Identifier,Integer> column2_1;
    @FXML
    private TableColumn<Identifier,String> column2_2;
    @FXML
    private TableColumn<Identifier,String> column2_3;
    @FXML
    private TableColumn<Identifier,Object> column2_4;

    @FXML
    private TableView<Constant> table3;
    @FXML
    private TableColumn<Constant,Integer> column3_1;
    @FXML
    private TableColumn<Constant,String> column3_2;

    @FXML
    private TableView<RisingParsingTableData> table4;
    @FXML
    private TableColumn<RisingParsingTable,Integer> column4_1;
    @FXML
    private TableColumn<RisingParsingTable,String> column4_2;
    @FXML
    private TableColumn<RisingParsingTable,Integer> column4_3;
    @FXML
    private TableColumn<RisingParsingTable,String> column4_4;

    @FXML
    private TableView<PolizLabel> table5;
    @FXML
    private TableColumn<PolizLabel,String> column5_1;
    @FXML
    private TableColumn<PolizLabel,Integer> column5_2;

    @FXML
    private TableView<PolizInformation> table6;
    @FXML
    private TableColumn<PolizInformation,String> column6_1;
    @FXML
    private TableColumn<PolizInformation,String> column6_2;
    @FXML
    private TableColumn<PolizInformation,String> column6_3;

    @FXML
    private TableView<PolizExecutionInformation> table7;
    @FXML
    private TableColumn<PolizExecutionInformation,String> column7_1;
    @FXML
    private TableColumn<PolizExecutionInformation,String> column7_2;

    @FXML
    private TextArea console;

    @FXML
    private TextArea poliz;

    @FXML
    private TextArea inputProgram;

    @FXML
    private Button performing;

    private String getProgramFromTA(){
        return inputProgram.getText() + "\t";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        performing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                performProgram();
            }
        });
        column1_1.setCellValueFactory(new PropertyValueFactory<>("lexemeNumber"));
        column1_2.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
        column1_3.setCellValueFactory(new PropertyValueFactory<>("lexemeName"));
        column1_4.setCellValueFactory(new PropertyValueFactory<>("lexemeCode"));
        column1_5.setCellValueFactory(new PropertyValueFactory<>("idnCode"));
        column2_1.setCellValueFactory(new PropertyValueFactory<>("code"));
        column2_2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2_3.setCellValueFactory(new PropertyValueFactory<>("type"));
        column2_4.setCellValueFactory(new PropertyValueFactory<>("value"));
        column3_1.setCellValueFactory(new PropertyValueFactory<>("code"));
        column3_2.setCellValueFactory(new PropertyValueFactory<>("value"));
        column4_1.setCellValueFactory(new PropertyValueFactory<>("step"));
        column4_2.setCellValueFactory(new PropertyValueFactory<>("stack"));
        column4_3.setCellValueFactory(new PropertyValueFactory<>("sign"));
        column4_4.setCellValueFactory(new PropertyValueFactory<>("input"));
        column5_1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column5_2.setCellValueFactory(new PropertyValueFactory<>("position"));
        column6_1.setCellValueFactory(new PropertyValueFactory<>("input"));
        column6_2.setCellValueFactory(new PropertyValueFactory<>("stack"));
        column6_3.setCellValueFactory(new PropertyValueFactory<>("poliz"));
        column7_1.setCellValueFactory(new PropertyValueFactory<>("input"));
        column7_2.setCellValueFactory(new PropertyValueFactory<>("stack"));
    }

    private void performProgram(){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(getProgramFromTA());
        RisingParsingTable risingParsingTable = null;
        console.clear();
        poliz.clear();
        try{
            lexicalAnalyzer.analyze();
            risingParsingTable = new RisingParsingTable(lexicalAnalyzer);
            risingParsingTable.analyze();
        } catch (Exception e){
            console.setText(e.getMessage());
        }
        ObservableList<Lexeme> outputTable = FXCollections.observableArrayList(lexicalAnalyzer.getOutputLexemesTable());
        table1.setItems(outputTable);
        Identifier.reset();
        ObservableList<Identifier> identifiersTable = FXCollections.observableArrayList(lexicalAnalyzer.getIdentifiers());
        table2.setItems(identifiersTable);
        Constant.reset();
        ObservableList<Constant> constantsTable = FXCollections.observableArrayList(lexicalAnalyzer.getConstants());
        table3.setItems(constantsTable);
        assert risingParsingTable != null;
        ObservableList<RisingParsingTableData> parsingInformationsTable = FXCollections.observableArrayList(risingParsingTable.getResult());
        table4.setItems(parsingInformationsTable);
        ArrayList<PolizWorkItem> inputVariables = new ArrayList<>();
        for(int i = 0; i < outputTable.size(); i++){
            if (outputTable.get(i).getLexemeCode() == 43)
                if (outputTable.get(i).getIdentifier().getType().equals("label"))
                    inputVariables.add(new PolizWorkItem(outputTable.get(i).getLexemeName(), "lbl"));
                else inputVariables.add(new PolizWorkItem(outputTable.get(i).getLexemeName(), "idn"));
            else if (outputTable.get(i).getLexemeCode() == 44)
                inputVariables.add(new PolizWorkItem(outputTable.get(i).getLexemeName(), "con"));
            else
                inputVariables.add(new PolizWorkItem(outputTable.get(i).getLexemeName(), "operation"));
        }
        Poliz builder = new Poliz(inputVariables);
        builder.doWork();
        ObservableList<PolizLabel> labelsTable = FXCollections.observableArrayList(builder.getLabels());
        table5.setItems(labelsTable);
        poliz.setText(builder.getPolizString());
        ObservableList<PolizInformation> polizInformation = FXCollections.observableArrayList(builder.getPolizInformations());
        table6.setItems(polizInformation);
        PolizExecution execution = null;
        try {
            execution = new PolizExecution(builder.poliz, builder.getLabels(), builder.getPolizProgramLabels(), builder.getPolizWorkCells(), lexicalAnalyzer.getIdentifiersArrayList(), console);
        } catch (Exception e){
            console.setText(e.getMessage());
        }
        ObservableList<PolizExecutionInformation> polizExecutionInformation = FXCollections.observableArrayList(execution.getPolizExecutionInformations());
        table7.setItems(polizExecutionInformation);
        table2.refresh();
        LexicalAnalyzer.reset();
    }
}
