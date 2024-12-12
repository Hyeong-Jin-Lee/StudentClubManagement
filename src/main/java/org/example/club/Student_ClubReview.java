package org.example.club;

import org.example.student.Role;

import java.sql.*;

public class Student_ClubReview {

    // Create the Student_ClubReview table if it doesn't exist
    public static void createStudent_ClubReviewTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Student_ClubReview'";
        String createTableSQL = """
            CREATE TABLE Student_ClubReview (
                ReviewID INT AUTO_INCREMENT PRIMARY KEY,
                ReviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
                System.out.println("Review table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Review table: " + e.getMessage());
        }
    }

    // Create a new Student_ClubReview
    public static void createStudent_ClubReview(Connection conn, String content, int studentID, int clubID) {
        String insertSQL = "INSERT INTO Student_ClubReview (Content, StudentID, ClubID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, studentID);
            pstmt.setInt(3, clubID);
            pstmt.executeUpdate();
            System.out.println("Create Review successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Review: " + e.getMessage());
        }
    }

    public static void readReviewByClubId(Connection conn, int clubID, int role) {
        String query = "SELECT * FROM Student_ClubReview WHERE ClubID = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.printf("%-15s%-30s%-5s\n","ID","Content","Date");
                do {
                    System.out.printf("%-15s%-30s%-5s\n", Integer.toString(rs.getInt("ReviewID")+(role == Role.MEMBER.ordinal() ? 2 : 1))+".", rs.getString("Content"), rs.getString("Date"));
                } while(rs.next());
            } else {
                System.out.println("리뷰가 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to read Review: " + e.getMessage());
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
                System.out.println("날짜 : " + reviewDate);
                System.out.println("내용 : " + content);
            } else {
                System.out.println("No Review found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Review: " + e.getMessage());
        }
    }

    public static String findDateById(Connection conn, int id) {
        String selectSQL = "SELECT * FROM Student_ClubReview WHERE ReviewID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String reviewDate = rs.getString("ReviewDate");
                return reviewDate;
            } else {
                System.out.println("No Review found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Review: " + e.getMessage());
        }
        return null;
    }

    // Update a Review
    public static void updateReivew(Connection conn, int id, String content) {
        String query = "UPDATE Student_ClubReview SET ";
        if(!content.equals("")) {
            query += "Content = ? ";
        }
        query += "WHERE ReviewID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1;
            if(!content.equals("")) stmt.setString(i++, content);
            stmt.setInt(i, id);
            if(i == 1) return;
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student_ClubReview updated successfully.");
            } else {
                System.out.println("No Student_ClubReview found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating Student_ClubReview: " + e.getMessage());
        }
    }

    public static void deleteStudent_ClubReview(Connection conn, int id) {
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
