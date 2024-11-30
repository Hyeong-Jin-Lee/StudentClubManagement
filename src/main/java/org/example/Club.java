package org.example;

import java.sql.*;

public class Club {

    public static void createClubTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Club'";
        String createTableQuery = """
            CREATE TABLE Club (
                ClubName VARCHAR(255) PRIMARY KEY,
                Description TEXT,
                FacultyAdvisor VARCHAR(100),
                EstablishmentYear INT
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // If the table does not exist
                stmt.executeUpdate(createTableQuery);
                System.out.println("Club table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Club table: " + e.getMessage());
        }
    }

    public static void createClub(Connection conn, String clubName, String description, String facultyAdvisor, int establishmentYear) {
        String query = "INSERT INTO Club (ClubName, Description, FacultyAdvisor, EstablishmentYear) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubName);
            stmt.setString(2, description);
            stmt.setString(3, facultyAdvisor);
            stmt.setInt(4, establishmentYear);
            stmt.executeUpdate();
            System.out.println("Club added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add club: " + e.getMessage());
        }
    }

    public static void readClubByName(Connection conn, String clubName) {
        String query = "SELECT * FROM Club WHERE ClubName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Club Name: " + rs.getString("ClubName"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("Faculty Advisor: " + rs.getString("FacultyAdvisor"));
                System.out.println("Establishment Year: " + rs.getInt("EstablishmentYear"));
            } else {
                System.out.println("No club found with the name: " + clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read club: " + e.getMessage());
        }
    }

    public static void updateClub(Connection conn, String clubName, String newDescription, String newFacultyAdvisor, int newEstablishmentYear) {
        String query = "UPDATE Club SET Description = ?, FacultyAdvisor = ?, EstablishmentYear = ? WHERE ClubName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newDescription);
            stmt.setString(2, newFacultyAdvisor);
            stmt.setInt(3, newEstablishmentYear);
            stmt.setString(4, clubName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Club updated successfully.");
            } else {
                System.out.println("No club found with the name: " + clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update club: " + e.getMessage());
        }
    }

    public static void deleteClub(Connection conn, String clubName) {
        String query = "DELETE FROM Club WHERE ClubName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Club with name " + clubName + " deleted successfully.");
            } else {
                System.out.println("No club found with the name: " + clubName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete club: " + e.getMessage());
        }
    }
}
