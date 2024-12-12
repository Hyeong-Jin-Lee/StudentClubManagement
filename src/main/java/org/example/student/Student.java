package org.example.student;

import org.example.club.ClubEntity;

import java.sql.*;

public class Student {

    public static void createStudentTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Student'";
        String createTableQuery = """
            CREATE TABLE Student (
                StudentID INT PRIMARY KEY,
                Name VARCHAR(100) NOT NULL,
                Contact VARCHAR(50) NOT NULL,
                Password VARCHAR(50) NOT NULL
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

    public static void createStudent(Connection conn, int studentId, String name, String contact, String password) {
        String query = "INSERT INTO Student (StudentID, Name, Contact, Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, name);
            stmt.setString(3, contact);
            stmt.setString(4, password);
            stmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add student: " + e.getMessage());
        }
    }

    public static StudentEntity findStudent(Connection conn, int studentId, String password) {
        String query = "SELECT * FROM Student WHERE StudentID = ? AND Password LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ClubEntity club = Join.findClubIdByStudentID(conn, studentId);
                StudentEntity student = new StudentEntity(rs.getInt("StudentID"), rs.getString("Name"), rs.getString("Contact"), club);
                return student;
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
        return null;
    }

    public static StudentEntity findStudent(Connection conn, StudentEntity student) {
        String query = "SELECT * FROM Student WHERE StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, student.getStudentId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ClubEntity club = Join.findClubIdByStudentID(conn, student.getStudentId());
                student = new StudentEntity(rs.getInt("StudentID"), rs.getString("Name"), rs.getString("Contact"), club);
                return student;
            } else {
                System.out.println("No student found with ID: " + student.getStudentId());
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
        return null;
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
