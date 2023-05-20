package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application of javaFX.
 */
public class HelloApplication extends Application {

    public static Stage mainStage;
    public static List<Category> categoryList = new ArrayList<>();
    public static List<Item> itemList = new ArrayList<>();
    public static List<Item> unsortedItemList = new ArrayList<>();
    public static List<Factory> factoryList = new ArrayList<>();
    public static List<Address> addressList = new ArrayList<>();
    public static List<Item> itemListFromFactory = new ArrayList<>();
    public static List<Store> storeList = new ArrayList<>();
    public static List<Item> itemListFromStore = new ArrayList<>();
    public static List<Client> clientList = new ArrayList<>();

    /**
     * Loads main stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuBar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        stage.setTitle("Welcome to my app!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches start method.
     */
    public static void main(String[] args) {
        try{

            System.out.println("Connection to base successful!");

            categoryList = Database.getAllCategories();

           /*for(Category category : categoryList){
               System.out.println(category.getName() + " " + category.getDescription() + " " + category.getId());
            }*/

            itemList = Database.getAllItems();
            unsortedItemList = new ArrayList<>(itemList);

            /*for(Item item : itemList){
                System.out.println(item.getId() + " " + item.getCategoryId() + " " + item.getName() + " " +
                        item.getWidth() + " " + item.getHeight() + " " + item.getLength() + " " + item.getProductionCost() + " " +
                        item.getSellingPrice());
            }*/

            addressList = Database.getAllAddresses();

            /*for(Address address : addressList){
                System.out.println(address.getId() + " " + address.getCity() +  " " + address.getStreet() + " " + address.getPostalCode() + " " + address.getHouseNumber());
            }*/

            factoryList = Database.getAllFactories();

            /*for(Factory factory : factoryList) {
                System.out.println(factory.getId() + " " + factory.getName() + " " + factory.getAddressId());
            }*/

            storeList = Database.getAllStores();

            /*for(Store store : storeList) {
                System.out.println(store.getId() + " " + store.getName() + " " + store.getWebAddress());
            }*/

            itemListFromFactory = Database.getItemsFromFactory();

            /*for(Item item : itemListFromFactory){
                System.out.println(item.getId() + " " + item.getCategoryId() + " " + item.getName() + " " +
                        item.getWidth() + " " + item.getHeight() + " " + item.getLength() + " " + item.getProductionCost() + " " +
                        item.getSellingPrice());
            }*/

            itemListFromStore = Database.getItemsFromStore();

            /*for(Item item : itemListFromStore){
                System.out.println(item.getId() + " " + item.getCategoryId() + " " + item.getName() + " " +
                        item.getWidth() + " " + item.getHeight() + " " + item.getLength() + " " + item.getProductionCost() + " " +
                        item.getSellingPrice());
            }*/

            //clientList.clear();
            clientList = Database.getAllClients();

            /*for(Client client : clientList) {
                System.out.println(client.getName() + " " + client.getLastName() + " " + client.getDateOfBirth() + " " + client.getAddress().getStreet());
            }*/
            launch();

            System.out.println("Disconnected from base successfully!");

        } catch(SQLException | IOException ex){
            System.out.println("Error connecting to base!");
            ex.printStackTrace();
        }
    }
    /**
     * Loads main stage.
     */
    public static Stage getStage(){
        return mainStage;
    }
}