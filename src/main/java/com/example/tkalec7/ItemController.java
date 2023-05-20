package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import hr.java.production.threads.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to show items through javaFX.
 */
public class ItemController {

    @FXML
    private TextField itemNameTextField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableColumn<Item, String> nameTableColumn;

    @FXML
    private TableColumn<Item, String> idTableColumn;

    @FXML
    private TableColumn<Item, String> categoryTableColumn;

    @FXML
    private TableColumn<Item, String> widthTableColumn;

    @FXML
    private TableColumn<Item, String> heightTableColumn;

    @FXML
    private TableColumn<Item, String> lengthTableColumn;

    @FXML
    private TableColumn<Item, String> productionCostTableColumn;

    @FXML
    private TableColumn<Item, String> sellingPriceTableColumn;
    private Timeline clock;

    /**
     * This method appears when item search screen is shown.
     */
    @FXML
    public void initialize() throws SQLException, IOException, InterruptedException {

        ItemControllerThread firstThread = new ItemControllerThread(
                categoryComboBox ,itemTableView,
                nameTableColumn, idTableColumn, categoryTableColumn,
                widthTableColumn, heightTableColumn, lengthTableColumn,
                productionCostTableColumn, sellingPriceTableColumn, "items", Database.token);

        ItemControllerThread secondThread = new ItemControllerThread(
                categoryComboBox ,itemTableView,
                nameTableColumn, idTableColumn, categoryTableColumn,
                widthTableColumn, heightTableColumn, lengthTableColumn,
                productionCostTableColumn, sellingPriceTableColumn, "categories", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");
        Thread thread2 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

        executorService.execute(thread1);
        executorService.execute(thread2);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);


        Platform.runLater(new SortingItemsThread(HelloApplication.itemList));

        SortingItemsThread sortingItemsThread = new SortingItemsThread(HelloApplication.itemList);
        Thread thread3 = new Thread(sortingItemsThread);
        thread3.start();

        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                HelloApplication.getStage().setTitle("Item with the highest price: " + HelloApplication.itemList.get(0).getName());

            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * This method filters item search.
     */
    @FXML
    protected void onSearchButtonClick() throws InterruptedException {

        clock.stop();

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        ItemControllerThread thirdThread = new ItemControllerThread(itemNameTextField, categoryComboBox, itemTableView, "SearchButton", Database.token);

        Thread thread3 = new Thread(thirdThread, "thirdThread");

        executorService.execute(thread3);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }
}
