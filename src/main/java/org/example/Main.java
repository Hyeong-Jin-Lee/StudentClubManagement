package org.example;

import org.example.club.*;
import org.example.student.Join;
import org.example.student.Role;
import org.example.student.Student;
import org.example.student.StudentEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    private StudentEntity student = null;
    private int role = -1;
    private ClubEntity studentClub = null;

    private void addData(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("데이터를 추가하시겠습니까? (y/x): ");
        String input = scanner.nextLine();
        if(input.equals("y")||input.equals("Y")) {
            Student.createStudent(conn, 2022000000, "최가은", "010-6666-5555", "1234");
            Student.createStudent(conn, 2020039018, "이형진", "010-5607-8205", "1234");
            Student.createStudent(conn, 2020039047, "박상준", "010-1234-1234", "1234");
            Student.createStudent(conn, 2020039038, "유승환", "010-2222-1111", "1234");
            Student.createStudent(conn, 2020039008, "배용호", "010-1111-3333", "1234");
            Student.createStudent(conn, 2020039033, "김민석", "010-5555-4432", "1234");

            Advisor.createAdvisor(conn, "최경주");
            Advisor.createAdvisor(conn, "아지즈");

            Club.createClub(conn, "Nova", "hello", "최경주", 2018);
            Club.createClub(conn, "샘마루", "hi", "아지즈", 2015);

            Join.createJoin(conn, 2022000000, 1, Role.LEADER);
            Join.createJoin(conn, 2020039018, 1, Role.MANAGER);
            Join.createJoin(conn, 2020039047, 1, Role.MEMBER);
            Join.createJoin(conn, 2020039038, 1, Role.MEMBER);
            Join.createJoin(conn, 2020039008, 2, Role.LEADER);
            Join.createJoin(conn, 2020039033, 2, Role.MANAGER);

            Schedule.createSchedule(conn, "개강총회", "2학기 개강총회", "20250302 19:00:00", "20250303 02:00:00", 2022000000, 1);
            Schedule.createSchedule(conn, "종강총회", "2학기 종강총회", "20250620 19:00:00", "20250621 02:00:00", 2020039018, 1);
            Schedule.createSchedule(conn, "종강총회", "2학기 종강총회", "20250620 19:00:00", "20250621 02:00:00", 2020039008, 2);

            Activity.createActivity(conn, "MT", "MT를 갔다왔다.", "20240907 14:00:00", "20240908 11:00:00", 1);
            Activity.createActivity(conn, "알고리즘 스터디", "1주차 알고리즘 스터디", "20241114 19:00:00", "20241114 22:00:00", 1);
            Activity.createActivity(conn, "MT", "MT를 갔다왔다.", "20240907 14:00:00", "20240908 11:00:00", 2);

            Notice.createNotice(conn, "MT 신청 받습니다.", "MT 절찬 모집중", 2020039018, 1);
            Notice.createNotice(conn, "개총 신청 받습니다.", "개총 절찬 모집중", 2020039018, 1);
            Notice.createNotice(conn, "MT 신청 받습니다.", "MT 절찬 모집중", 2020039008, 2);
        }
    }

    private void checkExists(Connection conn) {
        // Create tables if they do not exist
        Student.createStudentTableIfNotExists(conn);
        Advisor.createAdvisorTableIfNotExists(conn);
        Club.createClubTableIfNotExists(conn);
        Activity.createActivityTableIfNotExists(conn);
        Schedule.createScheduleTableIfNotExists(conn);
        Notice.createNoticeTableIfNotExists(conn);
        RecruitmentNotice.createRecruitmentNoticeTableIfNotExists(conn);
        Join.createJoinTableIfNotExists(conn);
        Student_ClubReview.createStudent_ClubReviewTableIfNotExists(conn);
        Student_Notice.createStudentNoticeTableIfNotExists(conn);
    }

    private int selectChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("번호를 선택하시오: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        return choice;
    }

    private int checkChoice(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.print(prompt+" (y/x): ");
        String check = scanner.nextLine();

        if(check.equals("y")||check.equals("Y")) {
            return 1;
        }

        return 0;
    }

    private void login(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("학번: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        student = Student.findStudent(conn, studentId, password);

        if(student!=null) {
            role = student.getClub().getRole().ordinal();
            studentClub = student.getClub();
        }
    }

    private void reload(Connection conn, StudentEntity student) {
        this.student = Student.findStudent(conn, student);
    }

    private void error() {
        System.out.println("잘못된 입력입니다.");
    }

    private void start() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = Connect.getConnection();

            checkExists(conn);
            addData(conn);


            int level = 1;
            int choice = -1;
            int choice2 = -1;
            int selectId = -1; // id

            while (true) {
                if(level == 1) {
                    System.out.println("0. 종료하기");
                    System.out.println("1. 로그인하기");
                } else {
                    if(student == null) {
                        level = 1;
                        continue;
                    }

                    if(level == 2) {
                        System.out.println("-------------------------------------");
                        System.out.println("   로그인 정보");
                        System.out.println("   학번 : "+student.getStudentId());
                        System.out.println("   이름 : "+student.getStudentName());
                        if(studentClub!=null) System.out.println("   동아리 : "+studentClub.getClubName());
                        if(studentClub!=null) System.out.println("   권한 : "+Role.fromInt(role).getRoleName());
                        System.out.println("-------------------------------------");
                    }

                    if(studentClub.getClubId() == -1) { // 가입 X, 예비 회원
                        if(level == 2) {
                            System.out.println("0. 종료하기");
                            System.out.println("1. 뒤로가기");
                            if(role == Role.BMEMBER.ordinal()) {
                                System.out.println("2. 가입취소하기");
                            }
                            System.out.println("-------------------------------------");
                            // 동아리 목록 띄움
                        } else if(level == 3) {
                            System.out.println("0. 종료하기");
                            System.out.println("1. 뒤로가기");
                            if(selectId == studentClub.getClubId() && role == Role.BMEMBER.ordinal()) {
                                System.out.println("2. 가입취소하기");
                            } else {
                                System.out.println("2. 가입하기");
                            }
                            System.out.println("-------------------------------------");
                            // 동아리 정보 띄움
                        }
                    } else {
                        if(level == 2) {
                            System.out.println("0. 종료하기");
                            System.out.println("1. 뒤로가기");
                            System.out.println("2. 공지사항 확인하기");
                            System.out.println("3. 활동 확인하기");
                            System.out.println("4. 일정 확인하기");
                            System.out.println("5. 리뷰 확인하기");
                            if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) { // 회장, 관리자
                                System.out.println("6. 가입요청 확인하기");
                                System.out.println("7. 동아리 정보 확인하기");
                            }
                        } else if(level == 3) {

                            if(choice2 == 6) {
                                StudentEntity student1 = Join.findStudentIDByJoinID(conn, selectId);
                                if(student1.getClub().getRole().ordinal() != Role.BMEMBER.ordinal()) {
                                    System.out.println("관리할 수 있는 회원이 아닙니다.");
                                    level = 2;
                                    continue;
                                }
                            }

                            if(choice != 2) {
                                System.out.println("0. 종료하기");
                                System.out.println("1. 뒤로가기");
                            }

                            if(choice2 == 2) { // 공지사항
                                if(choice == 2) {
                                    System.out.print("공지 제목 : ");
                                    String title = scanner.nextLine();
                                    System.out.print("공지 내용 : ");
                                    String content = scanner.nextLine();

                                    Notice.createNotice(conn, title, content, student.getStudentId(), studentClub.getClubId());
                                } else {
                                    if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                        System.out.println("2. 공지사항 수정하기");
                                        System.out.println("3. 공지사항 삭제하기");
                                    }
                                    System.out.println("-------------------------------------");

                                    // 동아리 공지사항 상세 정보 띄움
                                    Notice.readNoticeById(conn, selectId);
                                }
                            } else if(choice2 == 3) { // 활동
                                if(choice == 2) {
                                    System.out.print("활동 이름 : ");
                                    String name = scanner.nextLine();
                                    System.out.print("활동 내용 : ");
                                    String content = scanner.nextLine();
                                    System.out.print("활동 시작 날짜 (ex. 00000000 00:00:00) : ");
                                    String startDate = scanner.nextLine();
                                    System.out.print("활동 종료 날짜 (ex. 00000000 00:00:00) : ");
                                    String endDate = scanner.nextLine();

                                    Activity.createActivity(conn, name, content, startDate, endDate, studentClub.getClubId());
                                } else {
                                    if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                        System.out.println("2. 활동 수정하기");
                                        System.out.println("3. 활동 삭제하기");
                                    }
                                    System.out.println("-------------------------------------");

                                    // 동아리 활동 상세 띄움
                                    Activity.readActivityById(conn, selectId);
                                }
                            } else if(choice2 == 4) { // 일정
                                if(choice == 2) {
                                    System.out.print("일정 이름 : ");
                                    String name = scanner.nextLine();
                                    System.out.print("일정 내용 : ");
                                    String content = scanner.nextLine();
                                    System.out.print("일정 시작 날짜 (ex. 00000000 00:00:00) : ");
                                    String startDate = scanner.nextLine();
                                    System.out.print("일정 종료 날짜 (ex. 00000000 00:00:00) : ");
                                    String endDate = scanner.nextLine();

                                    Schedule.createSchedule(conn, name, content, startDate, endDate, student.getStudentId(), studentClub.getClubId());
                                } else {
                                    if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                        System.out.println("2. 일정 수정하기");
                                        System.out.println("3. 일정 삭제하기");
                                    }
                                    System.out.println("-------------------------------------");
                                    // 동아리 일정 상세 띄움
                                    Schedule.readScheduleById(conn, selectId);
                                }
                            } else if(choice2 == 5) { // 리뷰
                                if(choice == 2) {
                                    System.out.print("내용 : ");
                                    String content = scanner.nextLine();

                                    Student_ClubReview.createStudent_ClubReview(conn, content, student.getStudentId(), studentClub.getClubId());
                                } else {
                                    if(role == Role.MEMBER.ordinal()) {
                                        System.out.println("2. 리뷰 수정하기");
                                        System.out.println("3. 리뷰 삭제하기");
                                    }
                                    System.out.println("-------------------------------------");
                                    // 동아리 리뷰 상세 띄움
                                    Student_ClubReview.readReviewById(conn, selectId);
                                }
                            } else {
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) { // 활동
                                    if(choice2 == 6) { // 가입요청
                                        System.out.println("2. 가입요청 수락하기");
                                        System.out.println("3. 가입요청 거절하기");
                                        System.out.println("-------------------------------------");
                                        // 동아리 가입요청 상세 띄움
                                    } else if(choice2 == 7) {
                                        System.out.println("2. 동아리 정보 수정하기");
                                        System.out.println("-------------------------------------");
                                        // 동아리 정보 띄움
                                        Club.readClub(conn, studentClub.getClubId());
                                        System.out.println("-------------------------------------");
                                    } else { // 오류
                                        error();
                                    }
                                } else { // 오류
                                    error();
                                }
                            }
                        }
                    }
                }
                if(level == 3) {
                    if(choice == 2) {
                        level = 2;
                        continue;
                    }
                    choice = choice2;
                }

                choice2 = selectChoice();

                if(choice2 == 0) {
                    System.out.print("종료...");
                    Connect.closeConnection();
                    return;
                }

                if(level == 1) {
                    login(conn);
                    level = 2;
                } else {
                    if(student == null) {
                        level = 1;
                        continue;
                    }


                    if(studentClub.getClubId() == -1) { // 가입 X, 예비 회원
                        if(level == 2) {
                            if(choice == 1) {
                                level = 1;
                            } else if(role == Role.BMEMBER.ordinal() && choice == 2) { // 가입 신청 취소
                                if(checkChoice("가입 신청을 취소하시겠습니까? ") == 1) {
                                    Join.deleteJoin(conn, student);
                                    System.out.println("가입 신청을 취소하였습니다.");
                                } else {
                                    System.out.println("취소되었습니다.");
                                }
                            } else {
                                ClubEntity club = Club.findClub(conn, choice - 1);
                                if(club != null) {
                                    selectId = club.getClubId();
                                    level = 3;
                                } else { // 오류
                                    selectId = -1;
                                    error();
                                }
                            }
                        } else if(level == 3) {
                            if(choice == 1) {
                                level = 2;
                            } else if(choice == 2) {
                                if(studentClub.getClubId() == selectId && studentClub.getRole().ordinal() == Role.BMEMBER.ordinal()) { // 해당 동아리에 가입 신청한 회원의 경우
                                    if(checkChoice("가입 신청을 취소하시겠습니까?") == 1) {
                                        Join.deleteJoin(conn, student);
                                        System.out.println("가입 신청을 취소하였습니다.");
                                    } else {
                                        System.out.println("취소되었습니다.");
                                    }
                                } else { // 가입 신청 || 다른 동아리에 가입 신청하는 경우
                                    if(Join.checkJoin(conn, student.getStudentId()) == 0) { // 이미 가입된 회원의 경우
                                        System.out.println("이미 가입된 회원입니다.");
                                    } else if(Join.checkJoin(conn, student.getStudentId()) == 1) { // 이미 가입신청한 회원의 경우
                                        if(checkChoice("가입을 변경하시겠습니까?") == 1) {
                                            Join.deleteJoin(conn, student);
                                            Join.createJoin(conn, student, selectId);
                                            System.out.println("가입이 변경되었습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else {
                                        if(checkChoice("가입을 신청하시겠습니까?") == 1) {
                                            Join.createJoin(conn, student, selectId);
                                            System.out.println("가입이 신청되었습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    }
                                }
                            } else { // 오류
                                error();
                            }
                        }
                    } else {
                        if(level == 2) {
                            if(choice2 == 1) {
                                level = 1;
                                continue;
                            }

                            System.out.println("0. 종료하기");
                            System.out.println("1. 뒤로가기");

                            if(choice2 == 2) { // 공지사항
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    System.out.println("2. 공지사항 생성하기");
                                }
                                System.out.println("-------------------------------------");
                                // 동아리 공지사항 목록 띄움
                                Notice.readNoticeByClubId(conn, studentClub.getClubId(), role);
                            } else if(choice2 == 3) { // 활동
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    System.out.println("2. 활동 생성하기");
                                }
                                System.out.println("-------------------------------------");
                                // 동아리 활동 목록 띄움
                                Activity.readActivityByStudentId(conn, studentClub.getClubId(), role);
                            } else if(choice2 == 4) { // 일정
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    System.out.println("2. 일정 생성하기");
                                }
                                System.out.println("-------------------------------------");
                                // 동아리 일정 목록 띄움
                                Schedule.readScheduleByClubId(conn, studentClub.getClubId(), role);
                            } else if(choice2 == 5) { // 리뷰
                                if(role == Role.MEMBER.ordinal()) {
                                    System.out.println("2. 리뷰 생성하기");
                                }
                                System.out.println("-------------------------------------");
                                // 동아리 리뷰 목록 띄움
                                Student_ClubReview.readReviewByClubId(conn, studentClub.getClubId(), role);
                            } else {
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) { // 활동
                                    if(choice2 == 6) { // 가입요청
                                        System.out.println("-------------------------------------");
                                        // 동아리 가입요청 목록 띄움
                                        Join.readJoinByClubId(conn, studentClub.getClubId(), role);
                                    } else if(choice2 != 7) { // 오류
                                        error();
                                        continue;
                                    }
                                } else { // 오류
                                    error();
                                    continue;
                                }
                            }

                            if(choice2 != 7) { // 동아리 정보
                                choice = selectChoice();

                                if(choice == 0) {
                                    System.out.print("종료...");
                                    Connect.closeConnection();
                                    return;
                                }
                                if(choice == 1) {
                                    level = 2;
                                    choice2 = -1;
                                    continue;
                                }
                            }

                            if(choice2 == 5) {
                                selectId = choice - (role == Role.MEMBER.ordinal() ? 2 : 1);
                            } else if(choice2 == 6) {
                                selectId = choice - 1;
                            } else {
                                selectId = choice - (role == Role.MEMBER.ordinal() ? 1 : 2);
                            }

                            level = 3;
                        } else if(level == 3) {
                            if(choice2 == 1) {
                                level = 2;
                                continue;
                            }

                            if(choice == 2) { // 공지사항
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    if(choice2 == 2) {
                                        //공지사항 수정하기
                                        System.out.println("* 엔터를 누르면 수정되지 않습니다");
                                        System.out.print("공지 제목 : ");
                                        String title = scanner.nextLine();
                                        System.out.print("공지 내용 : ");
                                        String content = scanner.nextLine();

                                        if(checkChoice("공지사항을 수정하시겠습니까? ") == 1) {
                                            Notice.updateNotice(conn, selectId, title, content);
                                            System.out.println("공지사항을 수정하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else if(choice2 == 3) {
                                        //공지사항 삭제하기
                                        if(checkChoice("공지사항을 삭제하시겠습니까? ") == 1) {
                                            Notice.deleteNotice(conn, selectId);
                                            System.out.println("공지사항을 삭제하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else {
                                        error();
                                    }
                                } else {
                                    error();
                                }
                            } else if(choice == 3) { // 활동
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    if(choice2 == 2) {
                                        //활동 수정하기
                                        System.out.println("* 엔터를 누르면 수정되지 않습니다");
                                        System.out.print("활동 이름 : ");
                                        String name = scanner.nextLine();
                                        System.out.print("활동 내용 : ");
                                        String content = scanner.nextLine();
                                        System.out.print("활동 시작 날짜 (ex. 00000000 00:00:00) : ");
                                        String startDate = scanner.nextLine();
                                        System.out.print("활동 종료 날짜 (ex. 00000000 00:00:00) : ");
                                        String endDate = scanner.nextLine();

                                        if(checkChoice("활동을 수정하시겠습니까? ") == 1) {
                                            Activity.updateActivity(conn, selectId, name, content, startDate, endDate);
                                            System.out.println("활동을 수정하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else if(choice2 == 3) {
                                        //활동 삭제하기
                                        if(checkChoice("활동을 식제하시겠습니까? ") == 1) {
                                            Activity.deleteActivity(conn, selectId);
                                            System.out.println("활동을 삭제하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else {
                                        error();
                                    }
                                } else {
                                    error();
                                }
                            } else if(choice == 4) { // 일정
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    if(choice2 == 2) {
                                        // 일정 수정하기
                                        System.out.println("* 엔터를 누르면 수정되지 않습니다");
                                        System.out.print("일정 이름 : ");
                                        String name = scanner.nextLine();
                                        System.out.print("일정 내용 : ");
                                        String content = scanner.nextLine();
                                        System.out.print("일정 시작 날짜 (ex. 00000000 00:00:00) : ");
                                        String startDate = scanner.nextLine();
                                        System.out.print("일정 종료 날짜 (ex. 00000000 00:00:00) : ");
                                        String endDate = scanner.nextLine();

                                        if(checkChoice("일정 수정하시겠습니까? ") == 1) {
                                            Schedule.updateSchedule(conn, selectId, name, content, startDate, endDate);
                                            System.out.println("일정을 수정하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else if(choice2 == 3) {
                                        //일정 삭제하기
                                        if(checkChoice("일정을 수정하시겠습니까? ") == 1) {
                                            Schedule.deleteSchedule(conn, selectId);
                                            System.out.println("일정을 수정하였습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else {
                                        error();
                                    }
                                } else {
                                    error();
                                }
                            } else if(choice == 5) { // 리뷰
                                if(role == Role.MEMBER.ordinal()) {
                                    if(choice2 == 2) { // 리뷰
                                        System.out.println("* 엔터를 누르면 수정되지 않습니다");
                                        String date = Student_ClubReview.findDateById(conn, selectId);
                                        System.out.print("날짜 : "+date);
                                        System.out.print("내용 : ");
                                        String content = scanner.nextLine();

                                        if(checkChoice("리뷰를 수정하시겠습니까? ") == 1) {
                                            Student_ClubReview.updateReivew(conn, selectId, content);
                                            System.out.println("리뷰가 수정되었습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else if(choice2 == 3) { // 리뷰 삭제
                                        if(checkChoice("리뷰를 삭제하시겠습니까? ") == 1) {
                                            Student_ClubReview.deleteStudent_ClubReview(conn, selectId);
                                            System.out.println("리뷰가 삭제되었습니다.");
                                        } else {
                                            System.out.println("취소되었습니다.");
                                        }
                                    } else { // 오류
                                        error();
                                    }
                                } else { // 오류
                                    error();
                                }
                            } else {
                                if(role == Role.MANAGER.ordinal() || role == Role.LEADER.ordinal()) {
                                    if(choice == 6) { // 가입요청
                                        if(choice2 == 2) {
                                            //가입요청 수락하기
                                            if(checkChoice("가입을 수락하시겠습니까? ") == 1) {
                                                Join.updateJoin(conn, selectId, Role.MEMBER);
                                                System.out.println("가입이 수락되었습니다.");
                                            } else {
                                                System.out.println("취소되었습니다.");
                                            }
                                        } else if(choice2 == 3) {
                                            //가입요청 거절하기
                                            if(checkChoice("가입을 거절하시겠습니까? ") == 1) {
                                                Join.deleteJoin(conn, selectId);
                                                System.out.println("가입이 거절되었습니다.");
                                            } else {
                                                System.out.println("취소되었습니다.");
                                            }
                                        } else {
                                            error();
                                        }
                                    } else if(choice == 7) { // 동아리 정보
                                        // 동아리 정보 수정
                                        System.out.println("동아리명 : "+ studentClub.getClubName());
                                        System.out.print("동아리소개 : ");
                                        String description = scanner.nextLine();
                                        System.out.print("지도교수 : ");
                                        String advisor = scanner.nextLine();
                                        System.out.print("설립연도 : ");
                                        int foundedYear = scanner.nextInt();
                                        scanner.nextLine();

                                        Club.updateClub(conn, new ClubEntity(studentClub.getClubId(), studentClub.getClubName(), description, advisor, foundedYear));
                                        System.out.println("동아리 정보가 수정되었습니다.");
                                    } else { // 오류
                                        error();
                                    }
                                } else { // 오류
                                    error();
                                }
                            }
                            level = 2;

                        }
                    }
                }

                if(student != null) {
                    reload(conn,student);
                }
                System.out.println();

            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
