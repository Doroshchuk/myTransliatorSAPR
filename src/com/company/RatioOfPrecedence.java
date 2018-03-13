package com.company;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Dasha on 03.12.2016.
 */

public class RatioOfPrecedence extends Application {

    public void start(Stage primaryStage) throws Exception{
        Stage stage = new Stage();
        stage.setTitle("Конфликты, которые возникли в грамматике");
        Group root = new Group();
        Group root1 = new Group();
        primaryStage.setTitle("Doroshchuk Dasha TR-42");

        GrammarRatioOfPrecedence tree = new GrammarRatioOfPrecedence("D:\\САПР\\1-5\\SAPR_Transliator\\src\\com\\company\\grammarTable.txt");
        String[][] grammarTable = tree.getPrecedenceTable();
        String errors = tree.getErrors();
        Boolean er = false;
        Text errorsText = new Text();
        if (!Objects.equals(errors, "")){
            errorsText.setText(errors);
            er = true;
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(errorsText);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(200);

        //создание таблицы
        TableView table = new TableView();
        table.setEditable(true);

        for (int i = 0; i < grammarTable[0].length; i++) {
            TableColumn tableColumn = new TableColumn(grammarTable[0][i]);
            final int colNum = i;
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNum]));
                }
            });
            table.getColumns().add(tableColumn);
        }
        table.getColumns().add(new TableColumn(grammarTable[0][grammarTable.length - 1]));
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(grammarTable));
        data.remove(0);
        table.setItems(data);


        //создание формы
        table.setPrefHeight(700);
        table.setPrefWidth(1366);

        root.getChildren().addAll(table);
        root1.getChildren().addAll(scrollPane);
        Scene scene = new Scene(root, 1366, 700);
        if(er){
            Scene scene1 = new Scene(root1, 400, 200);
            stage.setScene(scene1);
            stage.show();
        }
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
