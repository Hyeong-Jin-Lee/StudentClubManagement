package org.example;

import java.sql.*;

public class Student_ClubReview {

    // Create the Student_ClubReview table if it doesn't exist
    public static void createStudent_ClubReviewTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Student_ClubReview'";
        String createTableSQL = """
            CREATE TABLE Student_ClubReview (
                ReviewID INT AUTO_INCREMENT PRIMARY KEY,
                ReviewDate VARCHAR(255),
                Content INT,
                ClubID INT,
                StudentID INT,
                FOREIGN KEY (StudentID)
                REFERENCES Student(StudentID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if(!rs.next()){
                stmt.executeUpdate(createTableSQL);
                System.out.println("Student_ClubReview table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Student_ClubReview table: " + e.getMessage());
        }
    }

    // Create a new Student_ClubReview
    public static void createStudent_ClubReview(Connection conn, String reviewDate, String content, int studentID, int clubID) {
        String insertSQL = "INSERT INTO Schedule (ReviewDate, Content, StudentID, ClubID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, reviewDate);
            pstmt.setString(2, content);
            pstmt.setInt(3, studentID);
            pstmt.setInt(4, clubID);
            pstmt.executeUpdate();
            System.out.println("Join Student_ClubReview successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Student_ClubReview: " + e.getMessage());
        }
    }

    // Read a Student_ClubReview by its ID
    public static void readReviewById(Connection conn, int id) {
        String selectSQL = "SELECT * FROM Student_ClubReview WHERE ReviewID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String reviewDate = rs.getString("ReviewDate");
                String content = rs.getString("Content");
                int studentID = rs.getInt("StudentID");
                int clubID = rs.getInt("ClubID");
                System.out.println("Review ID: " + id);
                System.out.println("StudentID: " + studentID);
                System.out.println("ClubID: " + clubID);
                System.out.println("Content: " + content);
                System.out.println("ReviewDate: " + reviewDate);
            } else {
                System.out.println("No Review found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Review: " + e.getMessage());
        }
    }

    // Update a Review
    public static void updateJoin(Connection conn, int id, String content) {
        String updateSQL = "UPDATE Student_ClubReview SET Content = ?, WHERE Review = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student_ClubReview updated successfully.");
            } else {
                System.out.println("No Student_ClubReview found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating Student_ClubReview: " + e.getMessage());
        }
    }

    // Delete a Join by its ID
    public static void deleteJoin(Connection conn, int id) {
        String deleteSQL = "DELETE FROM Student_ClubReview WHERE ReviewID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student_ClubReview deleted successfully.");
            } else {
                System.out.println("No Student_ClubReview found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Student_ClubReview: " + e.getMessage());
        }
    }
}
