package org.example.student;

import org.example.club.Club;
import org.example.club.ClubEntity;

import java.sql.*;

public class Join {
    // Create the Join table if it doesn't exist
    public static void createJoinTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Join'";
        String createTableSQL = """
            CREATE TABLE `Join` (
                JoinID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                JoinDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                StudentID INT,
                ClubID INT,
                Role INT,
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
                System.out.println("Join table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Join table: " + e.getMessage());
        }
    }

    // Create a new Join
    public static void createJoin(Connection conn, int studentID, int clubID, Role role) {
        String insertSQL = "INSERT INTO `Join` (StudentID, ClubID, Role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, clubID);
            pstmt.setInt(3, role.ordinal());
            pstmt.executeUpdate();
            System.out.println("Join created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Join: " + e.getMessage());
        }
    }

    public static int checkJoin(Connection conn, int studentID) {
        String query = "SELECT * FROM `Join` WHERE StudentID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if(Role.fromInt(rs.getInt("Role")) == Role.BMEMBER) {
                    return 1; // 예비 회원
                } else {
                    return 2; // 기존 회원
                }
            } else {
                System.out.println("No student found with ID: " + studentID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
        return 0;
    }

    public static void readJoinByClubId(Connection conn, int clubID, int role) {
        String query = "SELECT * FROM `Join` WHERE ClubID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-15s%-30s%-15s\n","JoinID","StudentName","Role");
                do {
                    StudentEntity student = Student.findStudent(conn, new StudentEntity(rs.getInt("StudentID")));
                    System.out.printf("%-15s%-30s%-15s\n", Integer.toString(rs.getInt("JoinID")+1)+".", student.getStudentName(), Role.fromInt(rs.getInt("Role")).getRoleName());
                } while(rs.next());
            } else {
                System.out.println("No activity found with ID: " + clubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read activity: " + e.getMessage());
        }
    }

    // Create a new Join
    public static void createJoin(Connection conn, StudentEntity student, int clubID) {
        String insertSQL = "INSERT INTO `Join` (StudentID, ClubID, RoleID) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, student.getStudentId());
            pstmt.setInt(2, clubID);
            pstmt.setInt(3, Role.MEMBER.ordinal());
            pstmt.executeUpdate();
            System.out.println("Join created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Join: " + e.getMessage());
        }
    }

    public static StudentEntity findStudentIDByJoinID(Connection conn, int joinID) {
        String query = "SELECT * FROM `Join` WHERE JoinID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, joinID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = Role.fromInt(rs.getInt("Role"));
                StudentEntity student = new StudentEntity(rs.getInt("StudentID"), new ClubEntity(rs.getInt("ClubID"), role));
                return student;
            } else {
                System.out.println("No join found with ID: " + joinID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
        return null;
    }

    public static ClubEntity findClubIdByStudentID(Connection conn, int studentID) {
        String query = "SELECT * FROM `Join` WHERE StudentID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = Role.fromInt(rs.getInt("Role"));
                return Club.findClub(conn, rs.getInt("ClubID"), role);
            } else {
                System.out.println("No join found with ID: " + studentID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read student: " + e.getMessage());
        }
        return null;
    }

    // Update a Join
    public static void updateJoin(Connection conn, int id, Role role) {
        String updateSQL = "UPDATE `Join` SET Role = ? WHERE JoinID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setInt(1, role.ordinal());
            pstmt.setInt(2, id);
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
    public static void deleteJoin(Connection conn, int joinID) {
        String deleteSQL = "DELETE FROM `Join` WHERE JoinID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, joinID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Join deleted successfully.");
            } else {
                System.out.println("No Join found with joinID: " + joinID);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Join: " + e.getMessage());
        }
    }

    public static void deleteJoin(Connection conn, StudentEntity student) {
        String deleteSQL = "DELETE FROM `Join` WHERE StudentID = ? AND ClubID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, student.getStudentId());
            pstmt.setInt(2, student.getClub().getClubId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Join deleted successfully.");
            } else {
                System.out.println("No Join found with studentId: " + student.getStudentId());
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Join: " + e.getMessage());
        }
    }
}
