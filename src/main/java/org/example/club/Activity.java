package org.example.club;

import org.example.student.Role;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Activity {

    // 활동 테이블 생성 (없으면 생성)
    public static void createActivityTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Activity'";
        String createTableQuery = """
            CREATE TABLE Activity (
                ActivityID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                ActivityName VARCHAR(255) NOT NULL,
                ActivityContent VARCHAR(255) NOT NULL,
                StartDate DATETIME NOT NULL,
                EndDate DATETIME NOT NULL,
                ClubID INT NOT NULL,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT    
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            // 테이블 존재 여부 확인
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // 테이블이 없으면 생성
                stmt.executeUpdate(createTableQuery);
                System.out.println("Activity table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Activity table: " + e.getMessage());
        }
    }

    // 활동 추가
    public static void createActivity(Connection conn, String activityName, String content, String startDate, String endDate, int clubID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        dateFormat.setLenient(false);
        String query = "INSERT INTO Activity (ActivityName, ActivityContent, StartDate, EndDate, ClubID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, activityName);
            stmt.setString(2, content);
            stmt.setTimestamp(3, new Timestamp(dateFormat.parse(startDate).getTime()));
            stmt.setTimestamp(4, new Timestamp(dateFormat.parse(endDate).getTime()));
            stmt.setInt(5, clubID);
            stmt.executeUpdate();
            System.out.println("Activity added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add activity: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // 활동 읽기 (ID로)
    public static void readActivityById(Connection conn, int activityId) {
        String query = "SELECT * FROM Activity WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("활동명 : " + rs.getString("ActivityName"));
                System.out.println("활동 내용 : " + rs.getString("ActivityContent"));
                System.out.println("시작 날짜 : " + rs.getString("StartDate"));
                System.out.println("종료 날짜 : " + rs.getString("EndDate"));
            } else {
                System.out.println("No activity found with ID: " + activityId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read activity: " + e.getMessage());
        }
    }

    public static void readActivityByStudentId(Connection conn, int clubID, int role) {
        String query = "SELECT * FROM Activity WHERE ClubID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-15s%-30s\n","ActivityID","ActivityName");
                do {
                    System.out.printf("%-15s%-30s\n", Integer.toString(rs.getInt("ActivityID")+(role == Role.MEMBER.ordinal() ? 1 : 2))+".", rs.getString("ActivityName"));
                } while(rs.next());
            } else {
                System.out.println("No activity found with ID: " + clubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read activity: " + e.getMessage());
        }
    }

    // 활동 업데이트
    public static void updateActivity(Connection conn, int activityId, String activityName, String activityContent, String startDate, String endDate) {
        String query = "UPDATE Activity SET ";
        int c = 0;
        if(!activityName.equals("")) {
            query += "ActivityName = ? ";
            c = 1;
        }
        if(!activityContent.equals("")) {
            if(c == 1) query += ", ";
            query += "ActivityContent = ?";
            c = 1;
        }
        if(!startDate.equals("")) {
            if(c == 1) query += ", ";
            query += "StartDate = ? ";
            c = 1;
        }
        if(!endDate.equals("")) {
            if(c == 1) query += ", ";
            query += "EndDate = ? ";
        }
        query += "WHERE ActivityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1;
            if(!activityName.equals("")) stmt.setString(i++, activityName);
            if(!activityContent.equals("")) stmt.setString(i++, activityContent);
            if(!startDate.equals("")) stmt.setString(i++, startDate);
            if(!endDate.equals("")) stmt.setString(i++, endDate);
            stmt.setInt(i, activityId);
            if(i == 1) return;
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
