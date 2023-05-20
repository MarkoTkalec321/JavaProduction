package hr.java.production.threads;

import database.Database;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This method uses threads for adding new category into database.
 */
public class AddNewCategoryControllerThread implements Runnable{

    private String enteredCategoryName, enteredCategoryDescription;
    private String type;
    private final String token;

    /**
     * Takes in enteredCategoryName, enteredCategoryDescription, type, token.
     * @param enteredCategoryName
     * @param enteredCategoryDescription
     * @param type
     * @param token
     */
    public AddNewCategoryControllerThread(String enteredCategoryName, String enteredCategoryDescription, String type, String token) {
        this.enteredCategoryName = enteredCategoryName;
        this.enteredCategoryDescription = enteredCategoryDescription;
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

        if(type.compareTo("cat") == 0) {
            try {
                Database.insertNewCategory(enteredCategoryName, enteredCategoryDescription);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        Database.activeConnectionWithDatabase = false;

        //System.out.println("Dretve: " + Thread.currentThread().getName());

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
