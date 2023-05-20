package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import hr.java.production.threads.AddNewFactoryControllerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to add new factory through javaFX.
 */
public class AddNewFactoryController {

    @FXML
    private TextField factoryNameTextField;

    @FXML
    private TextField streetNameTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private ChoiceBox<String> choiceBox;

    public static List<Item> items = new ArrayList<>();
    public static List<Factory> factories = new ArrayList<>();
    public static List<Item> itemList = new ArrayList<>();

    /**
     * This method appears when Add new factory screen is shown.
     */
    @FXML
    public void initialize() throws InterruptedException {


        AddNewFactoryControllerThread firstThread = new AddNewFactoryControllerThread(cityComboBox, "addresses", Database.token);
        AddNewFactoryControllerThread secondThread = new AddNewFactoryControllerThread(choiceBox, "items", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");
        Thread thread2 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

        executorService.execute(thread1);
        executorService.execute(thread2);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }
    /**
     * On button click saves factory using threads.
     */
    @FXML
    public void onSaveButtonClick() throws SQLException, IOException {

        String enteredFactoryName = factoryNameTextField.getText();
        String enteredStreetName = streetNameTextField.getText();
        String enteredHouseNumber = houseNumberTextField.getText();
        Optional<String> enteredCity = Optional.ofNullable(cityComboBox.getValue());
        Optional<String> enteredChoiceBox = Optional.ofNullable(choiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredFactoryName.isEmpty()){
            s.add("Factory name must not be empty!");
        }

        if(enteredStreetName.isEmpty()){
            s.add("Street name must not be empty!");
        }

        if(enteredHouseNumber.isEmpty()){
            s.add("House number must not be empty!");
        }

        if(enteredCity.isEmpty()){
            s.add("City must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){
            if(enteredChoiceBox.isPresent() && enteredCity.isPresent()) {
                for(Address address : Database.getAllAddresses()){
                    if(enteredCity.get().toLowerCase().compareTo(address.getCity().toLowerCase()) == 0){

                        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
                        AddNewFactoryControllerThread fourthThread = new AddNewFactoryControllerThread(enteredFactoryName, address, "button1", Database.token);
                        Thread thread4 = new Thread(fourthThread, "fourthThread");
                        executorService.execute(thread4);
                        executorService.shutdown();

                        address = new BuilderPattern()
                                .street(enteredStreetName)
                                .house(enteredHouseNumber)
                                .city(address.getCity())
                                .code(address.getPostalCode())
                                .build();

                        ExecutorService executorService1 = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
                        AddNewFactoryControllerThread fifthThread = new AddNewFactoryControllerThread(address, "button3", Database.token);
                        Thread thread5 = new Thread(fifthThread, "fifthThread");
                        executorService1.execute(thread5);
                        executorService1.shutdown();

                        break;
                    }
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/factorySerialized.dat"))) {

            out.writeObject(factories);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    /**
     * On button click saves item using threads.
     */
    @FXML
    public void onSaveItemButtonClick() throws SQLException, IOException {

        String enteredFactoryName = factoryNameTextField.getText();
        String enteredStreetName = streetNameTextField.getText();
        String enteredHouseNumber = houseNumberTextField.getText();
        Optional<String> enteredCity = Optional.ofNullable(cityComboBox.getValue());
        Optional<String> enteredChoiceBox = Optional.ofNullable(choiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredFactoryName.isEmpty()){
            s.add("Factory name must not be empty!");
        }

        if(enteredStreetName.isEmpty()){
            s.add("Street name must not be empty!");
        }

        if(enteredHouseNumber.isEmpty()){
            s.add("House number must not be empty!");
        }

        if(enteredCity.isEmpty()){
            s.add("City must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){
            if(enteredChoiceBox.isPresent() && enteredCity.isPresent()) {

                int flag = 0;
                outerloop:
                for(Factory factory : Database.getAllFactories()) {
                    for (Item item : Database.getAllItems()) {
                        if (item.getName().compareTo(enteredChoiceBox.get()) == 0) {
                            if (factory.getName().toLowerCase().compareTo(enteredFactoryName.toLowerCase()) == 0){

                                flag = Database.checkFactoryItemDuplicates(factory, item);
                                if(flag == 2){
                                    break outerloop;
                                }

                                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
                                AddNewFactoryControllerThread thirdThread = new AddNewFactoryControllerThread(factory, item, "button2", Database.token);
                                Thread thread3 = new Thread(thirdThread, "thirdThread");
                                executorService.execute(thread3);
                                executorService.shutdown();

                                flag = 1;
                                break outerloop;
                            }
                        }
                    }
                }

                if(flag == 1) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Item added successfully");
                    alert1.setHeaderText("Item added successfully");
                    alert1.setContentText("The Item '" + enteredChoiceBox.get() + "' was saved successfully.");
                    alert1.showAndWait();
                }
                else if(flag == 2){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Errors");
                    alert.setHeaderText("There are duplicates in the database!\nPlease try again.");
                    alert.setContentText(s.toString());
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Errors");
                    alert.setHeaderText("There is no store in database with this name!\nPlease try again.");
                    alert.setContentText(s.toString());
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }
}
