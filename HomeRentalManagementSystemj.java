package Anudipdemo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeRentalManagementSystemj 						// assume Manager
{
	
	
	
	    private static final String URL = "jdbc:mysql://localhost:3306/shreyashdemo";
	    private static final String USER = "root";
	    private static final String PASSWORD = "root123";

	    public static void main(String[] args) 
	    {
	        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) 
	        {
	            createTables(connection);
	            System.out.println("Tables created successfully.");

	            // Scanner for user input
	            Scanner scanner = new Scanner(System.in);

	            boolean continueOperation = true;
	            while (continueOperation) {
	                // Display menu
	                System.out.println("\nSelect operation:");
	                System.out.println("1. Create User");
	                System.out.println("2. Create Transaction");
	                System.out.println("3. Delete User");
	                System.out.println("4. Update Transaction");
	                System.out.println("5. Exit");

	                // Read user choice
	                System.out.print("Enter your choice: ");
	                int choice = scanner.nextInt();
	                scanner.nextLine(); // Consume newline

	                switch (choice) {
	                    case 1:
	                        createUser(connection, scanner);
	                        break;
	                    case 2:
	                        createTransaction(connection, scanner);
	                        break;
	                    case 3:
	                        deleteUser(connection, scanner);
	                        break;
	                    case 4:
	                        updateTransaction(connection, scanner);
	                        break;
	                    case 5:
	                        continueOperation = false;
	                        break;
	                    default:
	                        System.out.println("Invalid choice. Please enter a number from 1 to 5.");
	                        break;
	                }
	            }

	            scanner.close();
	        } catch (SQLException e) 
	        {
	            e.printStackTrace();
	        }
	    }

	    private static void createTables(Connection connection) throws SQLException 
	    {
	    	
	    	//Person who need the room 
	        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS User(userId INT AUTO_INCREMENT PRIMARY KEY,first_name VARCHAR(255) NOT NULL,last_name VARCHAR(255) NOT NULL,nature_of_id VARCHAR(255) NOT NULL,ID_number VARCHAR(255) NOT NULL,pin VARCHAR(255) NOT NULL,amount DOUBLE NOT NULL)";

	        String createTransactionTableSQL = "CREATE TABLE IF NOT EXISTS Transaction (reference_number INT AUTO_INCREMENT PRIMARY KEY,userId INT,receiver VARCHAR(255) NOT NULL,amount DOUBLE NOT NULL,date DATE NOT NULL,FOREIGN KEY (userId) REFERENCES User(userId))";

	        try (Statement statement = connection.createStatement()) {
	            statement.executeUpdate(createUserTableSQL);
	            statement.executeUpdate(createTransactionTableSQL);
	        }
	    }

	    private static void createUser(Connection connection, Scanner scanner) throws SQLException 
	    {
	        System.out.println("Enter user details:");
	        System.out.print("First Name: ");
	        String firstName = scanner.nextLine();
	        System.out.print("Last Name: ");
	        String lastName = scanner.nextLine();
	        System.out.print("Nature of ID: ");
	        String natureOfId = scanner.nextLine();
	        System.out.print("ID Number: ");
	        String idNumber = scanner.nextLine();
	        System.out.print("PIN: ");
	        String pin = scanner.nextLine();
	        System.out.print("Amount: ");
	        double amount = scanner.nextDouble();
	        scanner.nextLine(); // Consume newline

	        String query = "INSERT INTO User (first_name, last_name, nature_of_id, ID_number, pin, amount) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, firstName);
	            statement.setString(2, lastName);
	            statement.setString(3, natureOfId);
	            statement.setString(4, idNumber);
	            statement.setString(5, pin);
	            statement.setDouble(6, amount);
	            statement.executeUpdate();
	            System.out.println("User created successfully.");
	        }
	    }

	    private static void createTransaction(Connection connection, Scanner scanner) throws SQLException
	    {
	        System.out.println("Enter transaction details:");
	        System.out.print("User ID: ");
	        int userId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        System.out.print("Receiver: ");
	        String receiver = scanner.nextLine();
	        System.out.print("Amount: ");
	        double transactionAmount = scanner.nextDouble();
	        scanner.nextLine(); // Consume newline
	        System.out.print("Date (YYYY-MM-DD): ");
	        String date = scanner.nextLine();

	        String query = "INSERT INTO Transaction (userId, receiver, amount, date) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, userId);
	            statement.setString(2, receiver);
	            statement.setDouble(3, transactionAmount);
	            statement.setString(4, date);
	            statement.executeUpdate();
	            System.out.println("Transaction created successfully.");
	        }
	    }

	    private static void deleteUser(Connection connection, Scanner scanner) throws SQLException 
	    {
	        System.out.print("Enter the User ID to delete: ");
	        int userId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        String deleteTransactionsSQL = "DELETE FROM Transaction WHERE userId = ?";
	        try (PreparedStatement deleteTransactionsStatement = connection.prepareStatement(deleteTransactionsSQL)) 
	        {
	            deleteTransactionsStatement.setInt(1, userId);
	            deleteTransactionsStatement.executeUpdate();
	        }

	        String deleteUserSQL = "DELETE FROM User WHERE userId = ?";
	        try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserSQL)) {
	            deleteUserStatement.setInt(1, userId);
	            int rowsDeleted = deleteUserStatement.executeUpdate();
	            if (rowsDeleted > 0) {
	                System.out.println("User deleted successfully.");
	            } else {
	                System.out.println("No user found with the provided ID.");
	            }
	        }
	    }

	    private static void updateTransaction(Connection connection, Scanner scanner) throws SQLException 
	    {
	        System.out.print("Enter the Reference Number of the Transaction to update: ");
	        int referenceNumber = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        System.out.print("Enter the new Receiver: ");
	        String newReceiver = scanner.nextLine();

	        String updateTransactionSQL = "UPDATE Transaction SET receiver = ? WHERE reference_number = ?";
	        try (PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionSQL)) 
	        {
	            updateTransactionStatement.setString(1, newReceiver);
	            updateTransactionStatement.setInt(2, referenceNumber);
	            int rowsUpdated = updateTransactionStatement.executeUpdate();
	            if (rowsUpdated > 0) {
	                System.out.println("Transaction updated successfully.");
	            } else {
	                System.out.println("No transaction found with the provided reference number.");
	            }
	        }
	    }
	}
