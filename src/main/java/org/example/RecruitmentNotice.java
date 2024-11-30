package org.example;

import java.sql.*;

public class RecruitmentNotice {

    // Create the RecruitmentNotice table if it doesn't exist
    public static void createRecruitmentNoticeTableIfNotExists(Connection conn) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS RecruitmentNotice (" +
                "recruitmentNoticeId INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "description TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("RecruitmentNotice table is ready.");
        } catch (SQLException e) {
            System.out.println("Error creating RecruitmentNotice table: " + e.getMessage());
        }
    }

    // Create a new recruitment notice
    public static void createRecruitmentNotice(Connection conn, String title, String description) {
        createRecruitmentNoticeTableIfNotExists(conn);

        String insertSQL = "INSERT INTO RecruitmentNotice (title, description) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Recruitment Notice created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating recruitment notice: " + e.getMessage());
        }
    }

    // Read a recruitment notice by its ID
    public static void readRecruitmentNoticeById(Connection conn, int id) {
        String selectSQL = "SELECT * FROM RecruitmentNotice WHERE recruitmentNoticeId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");
                System.out.println("Recruitment Notice ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("Created At: " + createdAt);
            } else {
                System.out.println("No recruitment notice found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading recruitment notice: " + e.getMessage());
        }
    }

    // Update a recruitment notice
    public static void updateRecruitmentNotice(Connection conn, int id, String title, String description) {
        String updateSQL = "UPDATE RecruitmentNotice SET title = ?, description = ? WHERE recruitmentNoticeId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Recruitment Notice updated successfully.");
            } else {
                System.out.println("No recruitment notice found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating recruitment notice: " + e.getMessage());
        }
    }

    // Delete a recruitment notice by its ID
    public static void deleteRecruitmentNotice(Connection conn, int id) {
        String deleteSQL = "DELETE FROM RecruitmentNotice WHERE recruitmentNoticeId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Recruitment Notice deleted successfully.");
            } else {
                System.out.println("No recruitment notice found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting recruitment notice: " + e.getMessage());
        }
    }
}
