package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = Connect.getConnection();
            // Create tables if they do not exist
            Student.createStudentTableIfNotExists(conn);
            Club.createClubTableIfNotExists(conn);

            while (true) {
                System.out.println("1. Create student");
                System.out.println("2. Read student by ID");
                System.out.println("3. Update student");
                System.out.println("4. Delete student");
                System.out.println("5. Create club");
                System.out.println("6. Read club by name");
                System.out.println("7. Update club");
                System.out.println("8. Delete club");
                System.out.println("9. Create notice");
                System.out.println("10. Read notice by ID");
                System.out.println("11. Update notice");
                System.out.println("12. Delete notice");
                System.out.println("13. Create activity");
                System.out.println("14. Read activity by ID");
                System.out.println("15. Update activity");
                System.out.println("16. Delete activity");
                System.out.println("17. Create recruitment notice");  // 추가된 메뉴
                System.out.println("18. Read recruitment notice by ID");  // 추가된 메뉴
                System.out.println("19. Update recruitment notice");  // 추가된 메뉴
                System.out.println("20. Delete recruitment notice");
                System.out.println("21. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character


                switch (choice) {
                    case 1:
                        System.out.print("Enter student ID: ");
                        int studentId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter student name: ");
                        String studentName = scanner.nextLine();
                        System.out.print("Enter student contact: ");
                        String studentContact = scanner.nextLine();
                        Student.createStudent(conn, studentId, studentName, studentContact);
                        break;
                    case 2:
                        System.out.println("Enter student ID: ");
                        studentId = scanner.nextInt();
                        Student.readStudentById(conn, studentId);
                        break;
                    case 3:
                        System.out.println("Enter student ID to update: ");
                        studentId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.println("Enter new name: ");
                        studentName = scanner.nextLine();
                        System.out.println("Enter new contact: ");
                        studentContact = scanner.nextLine();
                        Student.updateStudent(conn, studentId, studentName, studentContact);
                        break;
                    case 4:
                        System.out.println("Enter student ID to delete: ");
                        studentId = scanner.nextInt();
                        Student.deleteStudent(conn, studentId);
                        break;
                    case 5:
                        System.out.println("Enter club name: ");
                        String clubName = scanner.nextLine();
                        System.out.println("Enter club description: ");
                        String description = scanner.nextLine();
                        System.out.println("Enter faculty advisor: ");
                        String advisor = scanner.nextLine();
                        System.out.println("Enter establishment year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        Club.createClub(conn, clubName, description, advisor, year);
                        break;
                    case 6:
                        System.out.println("Enter club name: ");
                        clubName = scanner.nextLine();
                        Club.readClubByName(conn, clubName);
                        break;
                    case 7:
                        System.out.println("Enter club name to update: ");
                        clubName = scanner.nextLine();
                        System.out.println("Enter new description: ");
                        description = scanner.nextLine();
                        System.out.println("Enter new faculty advisor: ");
                        advisor = scanner.nextLine();
                        System.out.println("Enter new establishment year: ");
                        year = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        Club.updateClub(conn, clubName, description, advisor, year);
                        break;
                    case 8:
                        System.out.println("Enter club name to delete:" );
                        clubName = scanner.nextLine();
                        Club.deleteClub(conn, clubName);
                        break;
                    case 9:
                        System.out.print("Enter notice title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter notice content: ");
                        String content = scanner.nextLine();
                        Notice.createNotice(conn, title, content);
                        break;
                    case 10:
                        System.out.print("Enter notice ID: ");
                        int noticeId = scanner.nextInt();
                        Notice.readNoticeById(conn, noticeId);
                        break;
                    case 11:
                        System.out.print("Enter notice ID to update: ");
                        noticeId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new title: ");
                        title = scanner.nextLine();
                        System.out.print("Enter new content: ");
                        content = scanner.nextLine();
                        Notice.updateNotice(conn, noticeId, title, content);
                        break;
                    case 12:
                        System.out.print("Enter notice ID to delete: ");
                        noticeId = scanner.nextInt();
                        Notice.deleteNotice(conn, noticeId);
                        break;
                    case 13:
                        System.out.print("Enter activity title: ");
                        String title_act = scanner.nextLine();
                        System.out.print("Enter activity description: ");
                        String description_act = scanner.nextLine();
                        System.out.print("Enter activity start date (YYYY-MM-DD HH:MM:SS): ");
                        String startDateStr = scanner.nextLine();
                        System.out.print("Enter activity end date (YYYY-MM-DD HH:MM:SS): ");
                        String endDateStr = scanner.nextLine();

                        Timestamp startDate = Timestamp.valueOf(startDateStr);
                        Timestamp endDate = Timestamp.valueOf(endDateStr);
                        Activity.createActivity(conn, title_act, description_act, startDate, endDate);
                        break;
                    case 14:
                        System.out.print("Enter activity ID: ");
                        int activityId = scanner.nextInt();
                        Activity.readActivityById(conn, activityId);
                        break;
                    case 15:
                        System.out.print("Enter activity ID to update: ");
                        activityId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new title: ");
                        title = scanner.nextLine();
                        System.out.print("Enter new description: ");
                        description = scanner.nextLine();
                        System.out.print("Enter new start date (YYYY-MM-DD HH:MM:SS): ");
                        startDateStr = scanner.nextLine();
                        System.out.print("Enter new end date (YYYY-MM-DD HH:MM:SS): ");
                        endDateStr = scanner.nextLine();

                        startDate = Timestamp.valueOf(startDateStr);
                        endDate = Timestamp.valueOf(endDateStr);
                        Activity.updateActivity(conn, activityId, title, description, startDate, endDate);
                        break;
                    case 16:
                        System.out.print("Enter activity ID to delete:");
                        activityId = scanner.nextInt();
                        Activity.deleteActivity(conn, activityId);
                        break;
                    case 17:
                        // Create Recruitment Notice
                        System.out.print("Enter recruitment notice title: ");
                        String recruitTitle = scanner.nextLine();
                        System.out.print("Enter recruitment notice description: ");
                        String recruitDesc = scanner.nextLine();
                        RecruitmentNotice.createRecruitmentNotice(conn, recruitTitle, recruitDesc);
                        break;
                    case 18:
                        // Read Recruitment Notice by ID
                        System.out.print("Enter recruitment notice ID: ");
                        int recruitNoticeId = scanner.nextInt();
                        RecruitmentNotice.readRecruitmentNoticeById(conn, recruitNoticeId);
                        break;
                    case 19:
                        // Update Recruitment Notice
                        System.out.print("Enter recruitment notice ID to update: ");
                        recruitNoticeId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new title: ");
                        recruitTitle = scanner.nextLine();
                        System.out.print("Enter new description: ");
                        recruitDesc = scanner.nextLine();
                        RecruitmentNotice.updateRecruitmentNotice(conn, recruitNoticeId, recruitTitle, recruitDesc);
                        break;
                    case 20:
                        // Delete Recruitment Notice
                        System.out.print("Enter recruitment notice ID to delete: ");
                        recruitNoticeId = scanner.nextInt();
                        RecruitmentNotice.deleteRecruitmentNotice(conn, recruitNoticeId);
                        break;
                    case 21:
                        System.out.print("Exiting...");
                        Connect.closeConnection();
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
