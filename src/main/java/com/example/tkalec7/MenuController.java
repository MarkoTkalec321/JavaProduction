package com.example.tkalec7;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * This method switches fxml screens.
 */
public class MenuController {
    /**
     * This method shows category search screen.
     */
    public void showCategorySearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("categorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Category search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows item search screen.
     */
    public void showItemSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("itemSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows factory search screen.
     */
    public void showFactorySearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FactorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Factory search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows store search screen.
     */
    public void showStoreSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StoreSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Store search!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows add item screen.
     */
    public void showAddItemScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewItemScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Add an item!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows add category screen.
     */
    public void showAddCategoryScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewCategoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Add a category!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows add factory screen.
     */
    public void showAddFactoryScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewFactoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Add a factory!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows add store screen.
     */
    public void showAddStoreScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewStoreScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Add a store!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows client search screen.
     */
    public void showClientSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("clientSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Search screen!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    /**
     * This method shows add client screen.
     */
    public void showAddClientScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewClient.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Add a client!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
