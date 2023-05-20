package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import hr.java.production.threads.FactoriesControllerThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to show factories through javaFX.
 */
public class FactoriesController {

    @FXML
    private TextField factoryNameTextField;

    @FXML
    private TableView<Factory> factoryTableView;

    @FXML
    private TableColumn<Factory, String> nameTableColumn;

    @FXML
    private TableColumn<Factory, String> idTableColumn;

    @FXML
    private TableColumn<Factory, String> addressTableColumn;

    /**
     * This method appears when factory search screen is shown.
     */
    @FXML
    public void initialize() throws InterruptedException, SQLException, IOException {

        nameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        idTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });

        addressTableColumn.setCellValueFactory(cellData -> {

            String temp = null;
            try {
                for(Address address : Database.getAllAddresses()){
                    if(address.getId().compareTo(cellData.getValue().getId()) == 0){
                        temp = address.getCity() +  " " + address.getPostalCode() + " " + address.getStreet();
                        break;
                    }
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(temp);
        });



        ObservableList<Factory> factoryObservableList = FXCollections.observableList(Database.getAllFactories());
        factoryTableView.setItems(factoryObservableList);


    }

    /**
     * This method filters factory search.
     */
    @FXML
    protected void onSearchButtonClick() throws InterruptedException {
        //String enteredItemName = factoryNameTextField.getText();

        FactoriesControllerThread thirdThread = new FactoriesControllerThread(factoryNameTextField ,factoryTableView, nameTableColumn, idTableColumn
                , addressTableColumn, "button", Database.token);

        Thread thread3 = new Thread(thirdThread, "thirdThread");
        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
        executorService.execute(thread3);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
