package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Client;
import hr.java.production.threads.AddNewClientControllerThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to add new clients through javaFX.
 */
public class AddNewClientController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField dateOfBirthTextField;
    private Timeline clock;

    /**
     * This method appears when Add new clients screen is shown.
     */
    @FXML
    public void initialize() throws SQLException, IOException {
        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {

            /**
             * Fills the ComboBox element with addresses, checks if clientId exists and if it does,
             * fills up used data for specific client into TextFields.
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {

                Set<String> set = new HashSet<>();
                try {
                    for(Address address : Database.getAllAddresses()) {
                        set.add(address.getCity());
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }

                if (ClientController.clientId != 0) {
                    try {
                        for (Client client : Database.getAllClients()) {
                            if (client.getId() == ClientController.clientId) {
                                firstNameTextField.setText(client.getName());
                                lastNameTextField.setText(client.getLastName());
                                dateOfBirthTextField.setText(client.getDateOfBirth());
                                break;
                            }
                        }
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }), new KeyFrame(Duration.seconds(1)));
        //clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    /**
     * Method that calls when it is clicked on save client button.
     * @throws InterruptedException
     */
    @FXML
    public void onClickSaveClient() throws InterruptedException, SQLException, IOException {

        String enteredFirstName = firstNameTextField.getText();
        String enteredLastName = lastNameTextField.getText();
        String enteredDate = dateOfBirthTextField.getText();

        StringJoiner s = new StringJoiner("\n");

        if (enteredFirstName.isEmpty()) {
            s.add("Client first name must not be empty!");
        }

        if (enteredLastName.isEmpty()) {
            s.add("Client last name must not be empty!");
        }

        if (enteredDate.isEmpty()) {
            s.add("Client date of birth must not be empty!");
        }
        else {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                df.setLenient(false);
                df.parse(enteredDate);

            } catch (ParseException e){
                s.add("Client date of birth is not in correct form!");
            }
        }

        if (s.toString().isEmpty()) {
            if (ClientController.clientId == 0) {
                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
                AddNewClientControllerThread secondThread = new AddNewClientControllerThread(enteredFirstName, enteredLastName,
                        enteredDate, (Database.getAllClients().size() + 1), "insert", Database.token);
                Thread thread2 = new Thread(secondThread, "secondThread");
                executorService.execute(thread2);

                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.SECONDS);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Client added successfully");
                alert.setHeaderText("Client added successfully");
                alert.setContentText("Client '" + enteredFirstName + "' was saved successfully.");
                alert.showAndWait();
            } else {

                System.out.println("client id " + ClientController.clientId);
                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
                AddNewClientControllerThread fourthThread = new AddNewClientControllerThread(enteredFirstName, enteredLastName,
                        enteredDate, Database.getAllClients().size() + 1, ClientController.clientId, "edit", Database.token);

                Thread thread4 = new Thread(fourthThread, "fourthThread");
                executorService.execute(thread4);

                executorService.shutdown();
                executorService.awaitTermination(100, TimeUnit.SECONDS);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Client edited successfully");
                alert.setHeaderText("Client edited successfully");
                alert.setContentText("Client '" + enteredFirstName + "' was edited successfully.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }
}
