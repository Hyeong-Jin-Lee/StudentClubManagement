package org.example;

import java.sql.*;

public class Student {

    public static void createStudentTableIfNotExists(Connection conn) {
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
                stmt.executeUpdate(createTableQuery);
                System.out.println("Student table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Student table: " + e.getMessage());
        }
    }

    public static void createStudent(Connection conn, int studentId, String name, String contact) {
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

    public static void readStudentById(Connection conn, int studentId) {
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

    public static void updateStudent(Connection conn, int studentId, String newName, String newContact) {
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

    public static void deleteStudent(Connection conn, int studentId) {
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
