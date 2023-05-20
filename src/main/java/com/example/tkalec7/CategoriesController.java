package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.threads.CategoriesControllerThread;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to show categories through javaFX.
 */
public class CategoriesController {

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextField categoryDescriptionTextField;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, String> nameTableColumn;

    @FXML
    private TableColumn<Category, String> idTableColumn;

    @FXML
    private TableColumn<Category, String> descriptionTableColumn;

    public static List<Category> categories = new ArrayList<>();

    /**
     * This method appears when category search screen is shown.
     */
    @FXML
    public void initialize() throws InterruptedException {

        CategoriesControllerThread firstThread = new CategoriesControllerThread(categoryTableView, nameTableColumn, idTableColumn,
                descriptionTableColumn, "initialize", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");
        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
        executorService.execute(thread1);
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    /**
     * This method filters category search.
     */
    @FXML
    protected void onSearchButtonClick() throws InterruptedException {

        String enteredCategoryName = categoryNameTextField.getText();
        String enteredCategoryDescription = categoryDescriptionTextField.getText();

        CategoriesControllerThread secondThread = new CategoriesControllerThread("gumb", Database.token, enteredCategoryName,
                enteredCategoryDescription, categoryTableView);

        Thread thread1 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }
}