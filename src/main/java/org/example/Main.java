package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static String IP = "192.168.56.101";
    private static String PORT = "4567";
    private static String DB_NAME = "sw_club";
    private static final String URL = "jdbc:mysql://"+IP+":"+PORT+"/"+DB_NAME+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "";
    private static final String PASSWORD = "";

    private static Connection conn = null;
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);



            Scanner scanner = new Scanner(System.in);
            while (true) {
                printMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        connectToDatabase();
                        break;
                    case 99:
                        System.out.println("Exiting system...");
                        closeConnection();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    private static void printMenu() {
        System.out.println("----------------------------");
        System.out.println("1. connection");
        System.out.println("99. quit");
        System.out.println("----------------------------");
    }

    private static void connectToDatabase() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } else {
                System.out.println("Already connected to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the connection.");
            e.printStackTrace();
        }
    }
}