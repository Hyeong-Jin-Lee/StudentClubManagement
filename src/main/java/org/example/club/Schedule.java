package org.example.club;

import org.example.student.Role;
import org.example.student.Student;
import org.example.student.StudentEntity;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Schedule {

    // Create the RecruitmentNotice table if it doesn't exist
    public static void createScheduleTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Schedule'";
        String createTableSQL = """
            CREATE TABLE Schedule (
                ScheduleID INT AUTO_INCREMENT PRIMARY KEY,
                Title VARCHAR(255) NOT NULL,
                Content TEXT,
                StartDate DATETIME NOT NULL,
                EndDate DATETIME NOT NULL,
                ClubID INT NOT NULL,
                CreatorID INT NOT NULL,
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
    public static void createSchedule(Connection conn, String title, String content, String startDate, String endDate, int creatorID, int clubID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        dateFormat.setLenient(false);
        String insertSQL = "INSERT INTO Schedule (Title, Content, StartDate, EndDate, CreatorID, ClubID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setTimestamp(3, new Timestamp(dateFormat.parse(startDate).getTime()));
            pstmt.setTimestamp(4, new Timestamp(dateFormat.parse(endDate).getTime()));
            pstmt.setInt(5, creatorID);
            pstmt.setInt(6, clubID);
            pstmt.executeUpdate();
            System.out.println("Schedule created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Schedule: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readScheduleByClubId(Connection conn, int clubID, int role) {
        String query = "SELECT * FROM Schedule WHERE ClubID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.printf("%-15s%-30s%-5s\n","Schedule ID","Title","CreatorID");
                do {
                    StudentEntity student = Student.findStudent(conn, new StudentEntity(rs.getInt("CreatorID")));
                    System.out.printf("%-15s%-30s%-5s\n", Integer.toString(rs.getInt("ScheduleID")+(role == Role.MEMBER.ordinal() ? 1 : 2))+".", rs.getString("Title"), student.getStudentName());
                } while(rs.next());
            } else {
                System.out.println("No schedule found with ID: " + clubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read schedule: " + e.getMessage());
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
                System.out.println("일정명 : " + title);
                System.out.println("일정 내용 : " + content);
                System.out.println("시작 날짜 : " + startDate);
                System.out.println("종료 날짜 : " + endDate);
            } else {
                System.out.println("No Schedule found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error reading Schedule: " + e.getMessage());
        }
    }

    // Update a Schedule
    public static void updateSchedule(Connection conn, int id, String title, String content, String startDate, String endDate) {
        String query = "UPDATE Schedule SET ";
        int c = 0;
        if(!title.equals("")) {
            query += "Title = ? "; c = 1;
        }
        if(!content.equals("")) {
            if(c == 1) query += ", ";
            query += "Content = ? "; c = 1;
        }
        if(!startDate.equals("")) {
            if(c == 1) query += ", ";
            query += "StartDate = ? "; c = 1;
        }
        if(!endDate.equals("")) {
            if(c == 1) query += ", ";
            query += "EndDate = ? ";
        }
        query += "WHERE ScheduleID = ?";
        System.out.println(query);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1;
            if(!title.equals("")) stmt.setString(i++, title);
            if(!content.equals("")) stmt.setString(i++, content);
            if(!startDate.equals("")) stmt.setString(i++, startDate);
            if(!endDate.equals("")) stmt.setString(i++, endDate);
            stmt.setInt(i, id);
            if(i == 1) return;
            int rowsAffected = stmt.executeUpdate();
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
