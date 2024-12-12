package org.example.student;

import org.example.club.ClubEntity;

public class StudentEntity {
    private int studentId;
    private String studentName;
    private String contact;
    private ClubEntity club;

    public StudentEntity(int studentId, String studentName, String contact, ClubEntity club) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.contact = contact;
        this.club = club;
    }

    public StudentEntity(int studentId) {
        this.studentId = studentId;
    }

    public StudentEntity(int studentId, ClubEntity club) {
        this.studentId = studentId;
        this.club = club;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getContact() {
        return contact;
    }

    public ClubEntity getClub() {
        return club;
    }
}
