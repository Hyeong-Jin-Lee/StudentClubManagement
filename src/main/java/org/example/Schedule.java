package org.example;

import java.sql.*;

public class Schedule {

    // Create the RecruitmentNotice table if it doesn't exist
    public static void createScheduleTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Schedule'";
        String createTableSQL = """
            CREATE TABLE Schedule (
                ScheduleID INT AUTO_INCREMENT PRIMARY KEY,
                Title VARCHAR(255),
                Content TEXT,
                StartDate VARCHAR(255),
                EndDate VARCHAR(255),
                ClubID INT,
                CreatorID INT,
                FOREIGN KEY (CreatorID)
                REFERENCES Student(StudentID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT
      
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if(!rs.next()){
                stmt.executeUpdate(createTableSQL);
                System.out.println("Schedule table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Schedule table: " + e.getMessage());
        }
    }

    // Create a new recruitment notice
    public static void createSchedule(Connection conn, String title, String content, String startDate,String endDate, int creatorID, int clubID) {
        String insertSQL = "INSERT INTO Schedule (Title, Content, StartDate, EndDate, CreatorID, ClubID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, startDate);
            pstmt.setString(3, endDate);
            pstmt.setInt(4, creatorID);
            pstmt.setInt(5, clubID);
            pstmt.executeUpdate();
            System.out.println("Recruitment Notice created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating recruitment notice: " + e.getMessage());
        }
    }

    // Read a recruitment notice by its ID
    public static void readScheduleById(Connection conn, int id) {
        String selectSQL = "SELECT * FROM Schedule WHERE ScheduleID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("Title");
                String content = rs.getString("Content");
                String startDate = rs.getString("StartDate");
                String endDate = rs.getString("EndDate");
                System.out.println("Recruitment Notice ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Content: " + content);
                System.out.println("StartDate: " + startDate);
                System.out.println("EndDate: " + endDate);
            } else {
                System.out.println("No Schedule found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Schedule: " + e.getMessage());
        }
    }

    // Update a Schedule
    public static void updateSchedule(Connection conn, int id, String title, String content) {
        String updateSQL = "UPDATE Schedule SET Title = ?, Content = ? WHERE ScheduleID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Schedule updated successfully.");
            } else {
                System.out.println("No Schedule found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating Schedule: " + e.getMessage());
        }
    }

    // Delete a recruitment notice by its ID
    public static void deleteSchedule(Connection conn, int id) {
        String deleteSQL = "DELETE FROM Schedule WHERE ScheduleID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Schedule deleted successfully.");
            } else {
                System.out.println("No Schedule found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Schedule: " + e.getMessage());
        }
    }
}
