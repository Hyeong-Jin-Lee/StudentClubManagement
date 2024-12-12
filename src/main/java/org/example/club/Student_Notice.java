package org.example.club;

import java.sql.*;

public class Student_Notice {

    // 공지사항 테이블 생성 (없으면 생성)
    public static void createStudentNoticeTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Student_Notice'";
        String createTableQuery = """
            CREATE TABLE Student_Notice (
                Student_NoticeID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                StudentID INT NOT NULL,
                NoticeID INT NOT NULL,
                NotificationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                IsRead TINYINT(1) NOT NULL,
                FOREIGN KEY (StudentID)
                REFERENCES Student(StudentID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (NoticeID)
                REFERENCES Notice(NoticeID) ON UPDATE CASCADE ON DELETE RESTRICT
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkTableQuery);
            if (!rs.next()) { // 테이블이 없으면 생성
                stmt.executeUpdate(createTableQuery);
                System.out.println("Notice table created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to check or create Notice table: " + e.getMessage());
        }
    }

    // 공지사항 확인
    public static void createStudentNotice(Connection conn, int studentID, int noticeID) {
        String query = "INSERT INTO Student_Notice (StudentID, NoticeID, IsRead) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            stmt.setInt(2, noticeID);
            stmt.setInt(3, 1);
            System.out.println("Notice added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add notice: " + e.getMessage());
        }
    }

    // 변경된 공지사항 재확인
    public static void updateStudentNotice(Connection conn, int studentID, int noticeID) {
        String query = "UPDATE Student_Notice SET IsRead = ? WHERE NoticeID = ? AND StudentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, 1);
            stmt.setInt(2, noticeID);
            stmt.setInt(3, studentID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notice updated successfully.");
            } else {
                System.out.println("No notice found with ID: " + noticeID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update notice: " + e.getMessage());
        }
    }

    // 공지사항 변경시 확인 여부 변경
    public static void updateStudentNotice(Connection conn, int noticeID) {
        String query = "UPDATE Student_Notice SET IsRead = ? WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, 0);
            stmt.setInt(2, noticeID);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notice updated successfully.");
            } else {
                System.out.println("No notice found with ID: " + noticeID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update notice: " + e.getMessage());
        }
    }

    // 공지사항 삭제
    public static void deleteStudentNotice(Connection conn, int noticeId) {
        String query = "DELETE FROM Student_Notice WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, noticeId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Notice with ID " + noticeId + " deleted successfully.");
            } else {
                System.out.println("No notice found with ID: " + noticeId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete notice: " + e.getMessage());
        }
    }
}

