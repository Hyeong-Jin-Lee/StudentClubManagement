package org.example;

import java.sql.*;

public class Activity {

    // 활동 테이블 생성 (없으면 생성)
    public static void createActivityTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Activity'";
        String createTableQuery = """
            CREATE TABLE Activity (
                ActivityID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                ActivityName VARCHAR(255) NOT NULL,
                Date TEXT NOT NULL,
                ClubName VARCHAR(255),
                FOREIGN KEY (ClubName)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT    
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
    public static void createActivity(Connection conn, String activityName, String date, int clubID) {

        String query = "INSERT INTO Activity (activityName, Date, ClubID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, activityName);
            stmt.setString(2, date);
            stmt.setInt(3, clubID);
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
                System.out.println("Activity Name: " + rs.getInt("ActivityName"));
                System.out.println("Date: " + rs.getString("Date"));
                System.out.println("ClubID: " + rs.getString("ClubID"));
            } else {
                System.out.println("No activity found with ID: " + activityId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read activity: " + e.getMessage());
        }
    }

    // 활동 업데이트
    public static void updateActivity(Connection conn, int activityId, String activityName, String date, int clubID) {
        String query = "UPDATE Activity SET ActivityName = ?, Date = ? WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, activityName);
            stmt.setString(2, date);
            stmt.setInt(3, clubID);
            stmt.setInt(4, activityId);
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
