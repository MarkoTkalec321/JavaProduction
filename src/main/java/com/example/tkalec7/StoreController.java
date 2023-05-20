package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import hr.java.production.threads.StoreControllerThread;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to show stores through javaFX.
 */
public class StoreController {

    @FXML
    private TextField storeNameTextField;

    @FXML
    private TableView<Store> storeTableView;

    @FXML
    private TableColumn<Store, String> nameTableColumn;

    @FXML
    private TableColumn<Store, String> idTableColumn;

    @FXML
    private TableColumn<Store, String> addressTableColumn;

    /**
     * This method appears when store search screen is shown.
     */
    @FXML
    public void initialize() throws InterruptedException {

        StoreControllerThread firstThread = new StoreControllerThread(storeTableView, nameTableColumn, idTableColumn, addressTableColumn, "store", Database.token);
        Thread thread1 = new Thread(firstThread, "firstThread");
        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
        executorService.execute(thread1);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    /**
     * This method filters store search.
     */
    @FXML
    protected void onSearchButtonClick() throws InterruptedException {

        StoreControllerThread secondThread = new StoreControllerThread(storeNameTextField, storeTableView, nameTableColumn, idTableColumn, addressTableColumn, "button", Database.token);
        Thread thread2 = new Thread(secondThread, "secondThread");
        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
        executorService.execute(thread2);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
