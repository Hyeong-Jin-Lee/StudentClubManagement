package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static String IP = "192.168.56.101";
    private static String PORT = "4567";
    private static String DB_NAME = "sw_club";
    private static final String URL = "jdbc:mysql://"+IP+":"+PORT+"/"+DB_NAME+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

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
                    case 2:
                        addStudent(scanner);
                        break;
                    case 3:
                        readStudentById(scanner);
                        break;
                    case 4:
                        updateStudent(scanner);
                    case 5:
                        deleteStudent(scanner);
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
        System.out.println("1. Connection");
        System.out.println("2. Add Student");
        System.out.println("3. Read Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
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

    // 1. Add Student
    private static void addStudent(Scanner scanner) {
        // Check if the Student table exists, if not, create it
        createStudentTableIfNotExists();

        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Contact: ");
        String contact = scanner.nextLine();

        String query = "INSERT INTO Student (StudentID, Name, Contact) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, name);
            stmt.setString(3, contact);
            stmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add student: " + e.getMessage());
        }
    }

    // Method to check if the Student table exists, and create it if not
    private static void createStudentTableIfNotExists() {
        String checkTableQuery = "SHOW TABLES LIKE 'Student'";
        String createTableQuery = """
        CREATE TABLE Student (
            StudentID INT PRIMARY KEY,
            Name VARCHAR(100) NOT NULL,
            Contact VARCHAR(50) NOT NULL
        )
        """;

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // If the table does not exist
                System.out.println("Student table does not exist. Creating table...");
                stmt.executeUpdate(createTableQuery);
                System.out.println("Student table created successfully.");
            } else {
                System.out.println("Student table already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Student table: " + e.getMessage());
        }
    }

    // Read Student by ID
    private static void readStudentById(Scanner scanner) {


        System.out.print("Enter Student ID to read: ");
        int studentId = scanner.nextInt();

        String query = "SELECT * FROM Student WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Student ID: " + rs.getInt("StudentID"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Contact: " + rs.getString("Contact"));
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
    }

    // Update Student
    private static void updateStudent(Scanner scanner) {


        System.out.print("Enter Student ID to update: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new Contact: ");
        String newContact = scanner.nextLine();

        String query = "UPDATE Student SET Name = ?, Contact = ? WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setString(2, newContact);
            stmt.setInt(3, studentId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update student: " + e.getMessage());
        }


        }
    // Delete Student by ID
    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter Student ID to delete: ");
        int studentId = scanner.nextInt();

        String query = "DELETE FROM Student WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with ID " + studentId + " deleted successfully.");
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete student: " + e.getMessage());
        }
    }
}