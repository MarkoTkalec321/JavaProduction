package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Client;
import hr.java.production.threads.ClientControllerThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to show clients through javaFX.
 */
public class ClientController {

    @FXML
    private TextField firstNameTextfield;

    @FXML
    private TextField lastNameTextfield;

    @FXML
    private TextField dateOfBirthTextField;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, String> firstNameTableColumn;

    @FXML
    private TableColumn<Client, String> lastNameTableColumn;

    @FXML
    private TableColumn<Client, String> dateTableColumn;

    public static long clientId = 0;

    private Timeline clock;

    /**
     * Shows this screen when client controller screen is pressed.
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    public void initialize() throws SQLException, IOException, InterruptedException {

        firstNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        lastNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLastName());
        });

        dateTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDateOfBirth());
        });

        clientTableView.setItems(FXCollections.observableList(Database.getAllClients()));

        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){

            /**
             * Switches between the youngest and the oldest client every 5 seconds.
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {


                try {
                    if(!Database.getAllClients().isEmpty()) {
                        HelloApplication.getStage().setTitle("The youngest client: " + Database.getAllClients().get(0).getName());
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }

                int size = 0;
                try {
                    size = Database.getAllClients().size();
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(!Database.getAllClients().isEmpty()) {
                        HelloApplication.getStage().setTitle("The oldest client: " + Database.getAllClients().get(size - 1).getName());
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }), new KeyFrame(Duration.seconds(5)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


    }

    /**
     * Searches a client based on filters.
     * @throws InterruptedException
     */
    @FXML
    public void clientSearchButton() throws InterruptedException {

        ClientControllerThread secondThread = new ClientControllerThread(firstNameTextfield,
                lastNameTextfield, dateOfBirthTextField,
                 clientTableView,
                firstNameTableColumn, lastNameTableColumn,
                dateTableColumn,
                "button", Database.token);

        Thread thread2 = new Thread(secondThread, "secondThread");
        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
        executorService.execute(thread2);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    /**
     * This method is called when certain row is right-clicked to edit a client.
     */
    @FXML
    public void editClientButton() {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewClient.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

        if((clientTableView.getSelectionModel().getSelectedItem()) != null){
            System.out.println(clientTableView.getSelectionModel().getSelectedItem().getId());
            clientId = clientTableView.getSelectionModel().getSelectedItem().getId();
        }
    }
}
