package org.example;

import java.sql.*;

public class Join {

    // Create the RecruitmentNotice table if it doesn't exist
    public static void createJoinTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Join'";
        String createTableSQL = """
            CREATE TABLE Join (
                JoinID INT AUTO_INCREMENT PRIMARY KEY,
                JoinDate VARCHAR(255),
                StudentID INT,
                ClubID INT,
                RoleID INT,
                FOREIGN KEY (StudentID)
                REFERENCES Student(StudentID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (RoleID)
                REFERENCES Role(RoleID) ON UPDATE CASCADE ON DELETE RESTRICT,
      
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if(!rs.next()){
                stmt.executeUpdate(createTableSQL);
                System.out.println("Join table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Join table: " + e.getMessage());
        }
    }

    // Create a new Join
    public static void createJoin(Connection conn, String joinDate, int studentID, int clubID,int roleID) {
        String insertSQL = "INSERT INTO Schedule (JoinDate, StudentID, ClubID, RoleID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, joinDate);
            pstmt.setInt(2, studentID);
            pstmt.setInt(3, clubID);
            pstmt.setInt(4, roleID);
            pstmt.executeUpdate();
            System.out.println("Join created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Join: " + e.getMessage());
        }
    }

    // Read a Join by its ID
    public static void readJoinById(Connection conn, int id) {
        String selectSQL = "SELECT * FROM Join WHERE JoinID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String joinDate = rs.getString("JoinDate");
                int studentID = rs.getInt("StudentID");
                int clubID = rs.getInt("ClubID");
                int roleID = rs.getInt("RoleID");
                System.out.println("Join ID: " + id);
                System.out.println("JoinDate: " + joinDate);
                System.out.println("StudentID: " + studentID);
                System.out.println("ClubID: " + clubID);
                System.out.println("RoleID: " + roleID);
            } else {
                System.out.println("No Join found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Join: " + e.getMessage());
        }
    }

    // Update a Join
    public static void updateJoin(Connection conn, int id, String joinDate) {
        String updateSQL = "UPDATE Join SET JoinDate = ? WHERE JoinID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, joinDate);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Join updated successfully.");
            } else {
                System.out.println("No Join found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error updating Join: " + e.getMessage());
        }
    }

    // Delete a Join by its ID
    public static void deleteJoin(Connection conn, int id) {
        String deleteSQL = "DELETE FROM Join WHERE JoinID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Join deleted successfully.");
            } else {
                System.out.println("No Join found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Join: " + e.getMessage());
        }
    }
}
