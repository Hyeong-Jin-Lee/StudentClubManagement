package org.example;

import java.sql.*;

public class Activity {



    // 활동 테이블 생성 (없으면 생성)
    public static void createActivityTableIfNotExists(Connection conn) {


        String checkTableQuery = "SHOW TABLES LIKE 'Activity'";
        String createTableQuery = """
            CREATE TABLE Activity (
                ActivityID INT AUTO_INCREMENT PRIMARY KEY,
                Title VARCHAR(255) NOT NULL,
                Description TEXT NOT NULL,
                StartDate DATETIME NOT NULL,
                EndDate DATETIME NOT NULL
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            // 테이블 존재 여부 확인
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // 테이블이 없으면 생성
                stmt.executeUpdate(createTableQuery);
                System.out.println("Activity table created successfully.");
            } else {
                System.out.println("Activity table already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Activity table: " + e.getMessage());
        }
    }

    // 활동 추가
    public static void createActivity(Connection conn, String title, String description, Timestamp startDate, Timestamp endDate) {
        createActivityTableIfNotExists(conn);
        String query = "INSERT INTO Activity (Title, Description, StartDate, EndDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setTimestamp(3, startDate);
            stmt.setTimestamp(4, endDate);
            stmt.executeUpdate();
            System.out.println("Activity added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add activity: " + e.getMessage());
        }
    }

    // 활동 읽기 (ID로)
    public static void readActivityById(Connection conn, int activityId) {
        String query = "SELECT * FROM Activity WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Activity ID: " + rs.getInt("ActivityID"));
                System.out.println("Title: " + rs.getString("Title"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("Start Date: " + rs.getTimestamp("StartDate"));
                System.out.println("End Date: " + rs.getTimestamp("EndDate"));
            } else {
                System.out.println("No activity found with ID: " + activityId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read activity: " + e.getMessage());
        }
    }

    // 활동 업데이트
    public static void updateActivity(Connection conn, int activityId, String newTitle, String newDescription, Timestamp newStartDate, Timestamp newEndDate) {
        String query = "UPDATE Activity SET Title = ?, Description = ?, StartDate = ?, EndDate = ? WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newDescription);
            stmt.setTimestamp(3, newStartDate);
            stmt.setTimestamp(4, newEndDate);
            stmt.setInt(5, activityId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Activity updated successfully.");
            } else {
                System.out.println("No activity found with ID: " + activityId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update activity: " + e.getMessage());
        }
    }

    // 활동 삭제
    public static void deleteActivity(Connection conn, int activityId) {
        String query = "DELETE FROM Activity WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, activityId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Activity with ID " + activityId + " deleted successfully.");
            } else {
                System.out.println("No activity found with ID: " + activityId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete activity: " + e.getMessage());
        }
    }
}
