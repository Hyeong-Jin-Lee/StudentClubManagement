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
                System.out.println("13. Exit");
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
                        System.out.println("Enter notice ID to delete:");
                        noticeId = scanner.nextInt();
                        Notice.deleteNotice(conn, noticeId);
                        break;
                    case 13:
                        System.out.println("Exiting...");
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
