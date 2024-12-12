package org.example.club;

import org.example.student.Role;

import java.sql.*;

public class Advisor {

    public static void createAdvisorTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Advisor'";
        String createTableQuery = """
            CREATE TABLE Advisor (
                AdvisorID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                Name VARCHAR(100) NOT NULL
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // If the table does not exist
                stmt.executeUpdate(createTableQuery);
                System.out.println("Advisor table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Advisor table: " + e.getMessage());

        }
    }

    // Create a new Join
    public static void createAdvisor(Connection conn, String name) {
        String insertSQL = "INSERT INTO Advisor (Name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Advisor created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Join: " + e.getMessage());
        }
    }

    // 교수님 확인
    public static int readAdvisorByName(Connection conn, String name) {
        String selectSQL = "SELECT * FROM Advisor WHERE Name LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("AdvisorID");
            } else {
                System.out.println("No Advisor found with Name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Advisor: " + e.getMessage());
        }
        return -1;
    }

    public static String readAdvisorById(Connection conn, int advisorID) {
        String selectSQL = "SELECT * FROM Advisor WHERE AdvisorID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, advisorID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("AdvisorID");
            } else {
                System.out.println("No Advisor found with ID: " + advisorID);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Advisor: " + e.getMessage());
        }
        return null;
    }
}
