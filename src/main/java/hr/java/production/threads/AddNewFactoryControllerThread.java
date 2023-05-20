package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This method uses threads for adding new factory into database.
 */
public class AddNewFactoryControllerThread implements Runnable{

    private ComboBox<String> cityComboBox;
    private ChoiceBox<String> choiceBox;
    private Factory factory;
    private String enteredFactoryName;
    private Address address;
    private Item item;
    private String type;
    private final String token;

    /**
     * Takes in address, type, token.
     * @param address
     * @param type
     * @param token
     */
    public AddNewFactoryControllerThread(Address address, String type, String token) {
        this.address = address;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in enteredFactoryName, address, type, token.
     * @param enteredFactoryName
     * @param address
     * @param type
     * @param token
     */
    public AddNewFactoryControllerThread(String enteredFactoryName, Address address, String type, String token) {
        this.enteredFactoryName = enteredFactoryName;
        this.address = address;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in cityComboBox, type, token.
     * @param cityComboBox
     * @param type
     * @param token
     */
    public AddNewFactoryControllerThread(ComboBox<String> cityComboBox, String type, String token) {
        this.cityComboBox = cityComboBox;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in choiceBox, type, token.
     * @param choiceBox
     * @param type
     * @param token
     */
    public AddNewFactoryControllerThread(ChoiceBox<String> choiceBox, String type, String token) {
        this.choiceBox = choiceBox;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in factory, item, type, token.
     * @param factory
     * @param item
     * @param type
     * @param token
     */
    public AddNewFactoryControllerThread(Factory factory, Item item, String type, String token) {
        this.factory = factory;
        this.item = item;
        this.type = type;
        this.token = token;
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

        if(type.compareTo("addresses") == 0) {
            Set<String> set = new HashSet<>();
            for(Address address : HelloApplication.addressList) {
                set.add(address.getCity());
                //options2.add(address.getCity());
            }
            List<String> options2 = new ArrayList<>(set);
            ObservableList<String> options = FXCollections.observableList(options2);
            cityComboBox.setItems(options);
        }

        if(type.compareTo("items") == 0) {
            List<String> options1 = new ArrayList<>();
            for(Item item : HelloApplication.itemList){
                options1.add(item.getName());
            }

            ObservableList<String> cursors = FXCollections.observableArrayList(options1);
            choiceBox.setItems(cursors);
        }
        if(type.compareTo("button1") == 0) {
            try {
                Database.insertNewFactory(enteredFactoryName, address);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.compareTo("button2") == 0) {
            try {
                Database.insertNewFactoryItem(factory, item);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        if(type.compareTo("button3") == 0) {
            try {
                Database.insertNewAddress(address);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }

    }
}
