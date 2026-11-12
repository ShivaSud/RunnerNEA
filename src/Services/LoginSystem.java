package Services;

import DatabaseConnect.DatabaseConnect;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class LoginSystem {
    // Database connection instance
    DatabaseConnect db;

    // Constructor to initialize the DatabaseConnect instance
    public LoginSystem(DatabaseConnect db) {
        this.db = db;
    }

    // Method to check if login details are correct (if the name exists in the database)
    public boolean checkLogInDetails(String name) {
        Statement stmt;
        ResultSet rs;
        String returnedName = "";

        try {
            // Create a statement and execute SQL query to check if name exists in the Runner table
            stmt = db.createStatement();
            String sql = "SELECT name FROM Runner WHERE name = '" + name + "'";
            rs = stmt.executeQuery(sql);

            // If a result is found, store the name returned from the query
            if (rs.next()) {
                returnedName = rs.getString("name");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage()); // Print any errors
        }
        return returnedName.equals(name); // Return true if name matches, false otherwise
    }

    // Method to create a new account (if the name does not already exist)
    public void createNewAccount(String name) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user until a unique name is entered

        Statement stmt;
        try {
            // Create a statement and insert a new runner record into the database
            stmt = db.createStatement();
            String sql = "INSERT INTO Runner (runnerID, name)" +
                    "VALUES(" + db.calculateNextID("Runner", "runnerID") + ",'" + name + "')";
            stmt.executeUpdate(sql); // Execute the insert query
            stmt.close(); // Close the statement
            db.commit(); // Commit the transaction to save the changes
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage()); // Print any errors
        }
    }
}