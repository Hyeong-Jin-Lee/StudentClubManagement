package org.example.club;

import org.example.student.Role;

import java.sql.*;

public class Club {

    public static void createClubTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Club'";
        String createTableQuery = """
            CREATE TABLE Club (
                ClubID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                ClubName VARCHAR(255),
                Description TEXT,
                AdvisorID INT, 
                FoundedYear INT,
                FOREIGN KEY (AdvisorID)
                REFERENCES Advisor(AdvisorID) ON UPDATE CASCADE ON DELETE RESTRICT
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

    public static void createClub(Connection conn, String clubName, String description, String advisorName, int foundedYear) {
        int advisorID = Advisor.readAdvisorByName(conn, advisorName);

        if(advisorID == -1) {
            System.out.println("교수님 정보가 없습니다.");
        } else {
            String query = "INSERT INTO Club (ClubName, Description, AdvisorID, FoundedYear) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, clubName);
                stmt.setString(2, description);
                stmt.setInt(3, advisorID);
                stmt.setInt(4, foundedYear);
                stmt.executeUpdate();
                System.out.println("Club added successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to add club: " + e.getMessage());
            }
        }
    }

    public static void readClub(Connection conn, int ClubID) {
        String query = "SELECT * FROM Club WHERE ClubID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ClubID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("동아리명 : "+rs.getString("ClubName"));
                System.out.println("동아리소개 : "+rs.getString("Description"));
                System.out.println("지도교수 : "+Advisor.readAdvisorById(conn, rs.getInt("AdvisorID")));
                System.out.println("설립연도 : "+rs.getInt("FoundedYear"));

            } else {
                System.out.println("No club found with the id: " + ClubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read club: " + e.getMessage());
        }
    }

    public static ClubEntity findClub(Connection conn, int ClubID) {
        String query = "SELECT * FROM Club WHERE ClubID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ClubID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ClubEntity club = new ClubEntity(rs.getInt("ClubID"), rs.getString("ClubName"));

                return club;
            } else {
                System.out.println("No club found with the id: " + ClubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read club: " + e.getMessage());
        }
        return null;
    }

    public static ClubEntity findClub(Connection conn, int ClubID, Role role) {
        if(role.ordinal() == Role.BMEMBER.ordinal()) return new ClubEntity(-1);

        String query = "SELECT * FROM Club WHERE ClubID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ClubID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ClubEntity club = new ClubEntity(rs.getInt("ClubID"), rs.getString("ClubName"), role);

                return club;
            } else {
                System.out.println("No club found with the id: " + ClubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read club: " + e.getMessage());
        }
        return null;
    }

    public static void updateClub(Connection conn, ClubEntity club) {
        int advisorID = Advisor.readAdvisorByName(conn, club.getAdvisorName());

        if(advisorID == -1) {
            System.out.println("교수님 정보가 없습니다.");
        } else {
            String query = "UPDATE Club SET Description = ?, AdvisorID = ?, FoundedYear = ? WHERE ClubId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, club.getDescription());
                stmt.setInt(2, advisorID);
                stmt.setInt(3, club.getFoundedYear());
                stmt.setInt(4, club.getClubId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Club updated successfully.");
                } else {
                    System.out.println("No club found with the name: " + club.getClubName());
                }
            } catch (SQLException e) {
                System.out.println("Failed to update club: " + e.getMessage());
            }
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
