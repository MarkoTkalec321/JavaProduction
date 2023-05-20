package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.threads.AddNewCategoryControllerThread;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller used to add new category through javaFX.
 */
public class AddNewCategoryController {

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextField categoryDescriptionTextField;

    public static List<Category> categories = new ArrayList<>();

    /**
     * This method appears when Add new category screen is shown.
     */
    @FXML
    public void initialize() {}
    /**
     * On button click saves category using threads.
     */
    @FXML
    public void onSaveButtonClick() {
        String enteredCategoryName = categoryNameTextField.getText();
        String enteredCategoryDescription = categoryDescriptionTextField.getText();


        StringJoiner s = new StringJoiner("\n");

        if(enteredCategoryName.isEmpty()){
            s.add("Category name must not be empty!");
        }

        if(enteredCategoryDescription.isEmpty()){
            s.add("Category description must not be empty!");
        }


        if(s.toString().isEmpty()){

            HelloApplication.categoryList.add(new Category(enteredCategoryName, HelloApplication.categoryList.size() + 1, enteredCategoryDescription));
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Category added successfully");
            alert1.setHeaderText("Category added successfully");
            alert1.setContentText("The Category '" + enteredCategoryName + "' was saved successfully.");
            alert1.showAndWait();

            ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);
            AddNewCategoryControllerThread firstThread = new AddNewCategoryControllerThread(enteredCategoryName, enteredCategoryDescription,
                    "cat", Database.token);

            Thread thread1 = new Thread(firstThread, "firstThread");
            executorService.execute(thread1);
            executorService.shutdown();

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
