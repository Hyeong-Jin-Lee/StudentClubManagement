package org.example;

import java.sql.*;

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
                CreaterID VARCHAR(255) NOT NULL,
                ClubID VARCHAR(255) NOT NULL,
                FOREIGN KEY (ClubID)
                REFERENCES Club(ClubID) ON UPDATE CASCADE ON DELETE RESTRICT,
                FOREIGN KEY (CreaterID)
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
    public static void createNotice(Connection conn, String title, String content, String creationDate, int creatorID, int clubID) {
        String query = "INSERT INTO Notice (Title, Content, CreationDate, CreatorID, ClubID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, creationDate);
            stmt.setInt(4, creatorID);
            stmt.setInt(5, clubID);
            stmt.executeUpdate();
            System.out.println("Notice added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add notice: " + e.getMessage());
        }
    }

    // 공지사항 읽기 (ID로)
    public static void readNoticeById(Connection conn, int noticeId) {
        String query = "SELECT * FROM Notice WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, noticeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Notice ID: " + rs.getInt("NoticeID"));
                System.out.println("Title: " + rs.getString("Title"));
                System.out.println("Content: " + rs.getString("Content"));
                System.out.println("Creation Date: " + rs.getTimestamp("CreationDate"));
            } else {
                System.out.println("No notice found with ID: " + noticeId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to read notice: " + e.getMessage());
        }
    }

    // 공지사항 업데이트
    public static void updateNotice(Connection conn, int noticeId, String newTitle, String newContent, String creationDate, int creatorID, int clubID) {
        String query = "UPDATE Notice SET Title = ?, Content = ? WHERE NoticeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newContent);
            stmt.setString(3, creationDate);
            stmt.setInt(4, creatorID);
            stmt.setInt(5, clubID);
            stmt.setInt(6, noticeId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Notice updated successfully.");
            } else {
                System.out.println("No notice found with ID: " + noticeId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update notice: " + e.getMessage());
        }
    }

    // 공지사항 삭제
    public static void deleteNotice(Connection conn, int noticeId) {
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

