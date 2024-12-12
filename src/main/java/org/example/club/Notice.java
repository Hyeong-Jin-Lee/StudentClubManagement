package org.example.club;

import org.example.student.Role;

import java.sql.*;
import java.util.Date;

public class Notice {

    // 공지사항 테이블 생성 (없으면 생성)
    public static void createNoticeTableIfNotExists(Connection conn) {
        String checkTableQuery = "SHOW TABLES LIKE 'Notice'";
        String createTableQuery = """
            CREATE TABLE Notice (
                NoticeID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                Title VARCHAR(255) NOT NULL,
                Content TEXT NOT NULL,
                CreationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CreatorID INT NOT NULL,
                ClubID INT NOT NULL,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (CreatorID)
                REFERENCES Student(StudentID) ON UPDATE CASCADE ON DELETE RESTRICT
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

    // 공지사항 추가
    public static void createNotice(Connection conn, String title, String content, int creatorID, int clubID) {
        String query = "INSERT INTO Notice (Title, Content, CreatorID, ClubID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setInt(3, creatorID);
            stmt.setInt(4, clubID);
            stmt.executeUpdate();
            System.out.println("Notice added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add notice: " + e.getMessage());
        }
    }

    // 동아리별 공지사항 목록 출력
    public static void readNoticeByClubId(Connection conn, int clubID, int role) {
        String query = "SELECT * FROM Notice N LEFT JOIN Student_Notice SN ON N.NoticeID = SN.NoticeID WHERE N.ClubID = ? " +
                       "UNION SELECT * FROM Notice N RIGHT JOIN Student_Notice SN ON N.NoticeID = SN.NoticeID WHERE N.ClubID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clubID);
            stmt.setInt(2, clubID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.printf("%-15s%-30s%-5s\n","Notice ID","Title","Read");
                do {
                    System.out.printf("%-15s%-30s%-5s\n", Integer.toString(rs.getInt("NoticeID")+(role == Role.MEMBER.ordinal() ? 1 : 2))+".", rs.getString("Title"), "R");
                } while(rs.next());
            } else {
                System.out.println("No notice found with ID: " + clubID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read notice: " + e.getMessage());
        }
    }

    // 공지사항 상세 읽기
    public static void readNoticeById(Connection conn, int noticeID) {
        String query = "SELECT * FROM Notice WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, noticeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Title: " + rs.getString("Title"));
                System.out.println("Content: " + rs.getString("Content"));
                System.out.println("Creation Date: " + rs.getTimestamp("CreationDate"));
            } else {
                System.out.println("No notice found with ID: " + noticeID);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read notice: " + e.getMessage());
        }
    }

    // 공지사항 업데이트
    public static void updateNotice(Connection conn, int noticeId, String newTitle, String newContent) {
        String query = "UPDATE Notice SET ";
        int c = 0;
        if(!newTitle.equals("")) {
            query += "Title = ? ";
            c = 1;
        }
        if(!newContent.equals("")) {
            if(c == 1) query += ", ";
            query += "Content = ?  ";
        }
        query += "WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int i = 1;
            if(!newTitle.equals("")) stmt.setString(i++, newTitle);
            if(!newContent.equals("")) stmt.setString(i++, newContent);
            stmt.setInt(i, noticeId);
            if(i == 1) return;
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notice updated successfully.");
                Student_Notice.updateStudentNotice(conn, noticeId);
            } else {
                System.out.println("No notice found with ID: " + noticeId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update notice: " + e.getMessage());
        }
    }

    // 공지사항 삭제
    public static void deleteNotice(Connection conn, int noticeId) {
        Student_Notice.deleteStudentNotice(conn, noticeId);

        String query = "DELETE FROM Notice WHERE NoticeID = ?";
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

