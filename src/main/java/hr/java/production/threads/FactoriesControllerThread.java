package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Factory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This method uses threads for showing factory into database.
 */
public class FactoriesControllerThread implements Runnable{

    private TextField factoryNameTextField;
    private TableView<Factory> factoryTableView;
    private TableColumn<Factory, String> nameTableColumn;
    private TableColumn<Factory, String> idTableColumn;
    private TableColumn<Factory, String> addressTableColumn;
    private String type;
    private final String token;
    private List<Address> addressList;

    /**
     * Takes in factoryNameTextField, factoryTableView, nameTableColumn, idTableColumn, addressTableColumn, type, token.
     * @param factoryNameTextField
     * @param factoryTableView
     * @param nameTableColumn
     * @param idTableColumn
     * @param addressTableColumn
     * @param type
     * @param token
     */
    public FactoriesControllerThread(TextField factoryNameTextField, TableView<Factory> factoryTableView, TableColumn<Factory, String> nameTableColumn, TableColumn<Factory, String> idTableColumn, TableColumn<Factory, String> addressTableColumn, String type, String token) {
        this.factoryNameTextField = factoryNameTextField;
        this.factoryTableView = factoryTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.addressTableColumn = addressTableColumn;
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

        if(type.compareTo("factories") == 0) {
            nameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            addressTableColumn.setCellValueFactory(cellData -> {

                String temp = null;

                    for (Address address : addressList) {
                        if (address.getId().compareTo(cellData.getValue().getId()) == 0) {
                            temp = address.getCity() + " " + address.getPostalCode() + " " + address.getStreet();
                            break;
                        }
                    }

                return new SimpleStringProperty(temp);
            });

            ObservableList<Factory> factoryObservableList = null;
            try {
                factoryObservableList = FXCollections.observableList(Database.getAllFactories());
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            factoryTableView.setItems(factoryObservableList);
        }

        if(type.compareTo("addresses") == 0) {
            addressTableColumn.setCellValueFactory(cellData -> {
                String temp = null;
                for(Address address : addressList){
                    if(address.getId().compareTo(cellData.getValue().getId()) == 0){
                        temp = address.getCity() +  " " + address.getPostalCode() + " " + address.getStreet();
                        break;
                    }
                }
                return new SimpleStringProperty(temp);
            });
        }

        if(type.compareTo("button") == 0) {
            String enteredItemName = factoryNameTextField.getText();
            List<Factory> filteredList = new ArrayList<>(HelloApplication.factoryList);

            if(Optional.of(enteredItemName).isEmpty() == false) {
                filteredList = filteredList.stream()
                        .filter(n -> n.getName().contains(enteredItemName))
                        .collect(Collectors.toList());
            }

            factoryTableView.setItems(FXCollections.observableList(filteredList));
        }



        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
