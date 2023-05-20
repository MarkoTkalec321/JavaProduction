package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This method uses threads for showing categories.
 */
public class CategoriesControllerThread implements Runnable{


    private TableView<Category> categoryTableView;
    private TableColumn<Category, String> nameTableColumn;
    private TableColumn<Category, String> idTableColumn;
    private TableColumn<Category, String> descriptionTableColumn;
    private String type;
    private final String token;
    public static List<Category> categories = new ArrayList<>();

    private String enteredCategoryName;
    private String enteredCategoryDescription;

    /**
     * Takes in categoryTableView, nameTableColumn, idTableColumn, descriptionTableColumn, type, token.
     * @param categoryTableView
     * @param nameTableColumn
     * @param idTableColumn
     * @param descriptionTableColumn
     * @param type
     * @param token
     */
    public CategoriesControllerThread(TableView<Category> categoryTableView,
                                      TableColumn<Category, String> nameTableColumn,
                                      TableColumn<Category, String> idTableColumn,
                                      TableColumn<Category, String> descriptionTableColumn,
                                      String type, String token) {
        this.categoryTableView = categoryTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.descriptionTableColumn = descriptionTableColumn;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in type, token, idTableColumn, enteredCategoryName, enteredCategoryDescription, categoryTableView.
     * @param type
     * @param token
     * @param enteredCategoryName
     * @param enteredCategoryDescription
     * @param categoryTableView
     */
    public CategoriesControllerThread(String type, String token, String enteredCategoryName, String enteredCategoryDescription, TableView<Category> categoryTableView) {
        this.type = type;
        this.token = token;
        this.enteredCategoryName = enteredCategoryName;
        this.enteredCategoryDescription = enteredCategoryDescription;
        this.categoryTableView = categoryTableView;
    }

    /**
     * This method is called with executorService.
     */
    @Override
    public void run() {
        while (Database.activeConnectionWithDatabase == true) {
            try {
                System.out.println("Ime dretve: " + Thread.currentThread().getName());
                //wait();
                synchronized (this.token) {
                    this.token.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Database.activeConnectionWithDatabase = true;

        if(type.compareTo("initialize") == 0) {
            nameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            descriptionTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getDescription());
            });

            try {
                categoryTableView.setItems(FXCollections.observableList(Database.getAllCategories()));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(type.compareTo("gumb") == 0) {
            List<Category> filteredList = null;
            try {
                filteredList = new ArrayList<>(Database.getAllCategories());
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }


            filteredList = filteredList.stream()
                    .filter(n -> n.getName().toLowerCase().contains(enteredCategoryName.toLowerCase()))
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(n -> n.getDescription().toLowerCase().contains(enteredCategoryDescription.toLowerCase()))
                    .collect(Collectors.toList());


            categoryTableView.setItems(FXCollections.observableList(filteredList));
        }

        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
