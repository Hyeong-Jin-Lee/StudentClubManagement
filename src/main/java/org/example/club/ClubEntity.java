package org.example.club;

import org.example.student.Role;

public class ClubEntity {
    private int clubId;
    private String clubName;
    private String description;
    private String advisorName;
    private int foundedYear;
    private Role role;

    public ClubEntity(int clubId, String clubName, String description, String advisorName, int foundedYear) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description = description;
        this.advisorName = advisorName;
        this.foundedYear = foundedYear;
    }

    public ClubEntity(int clubId, String clubName, Role role) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.role = role;
    }

    public ClubEntity(int clubId, Role role) {
        this.clubId = clubId;
        this.role = role;
    }

    public ClubEntity(int clubId, String clubName) {
        this.clubId = clubId;
        this.clubName = clubName;
    }

    public ClubEntity(int clubId) {
        this.clubId = clubId;
    }

    public int getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDescription() {
        return description;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public Role getRole() {
        return role;
    }
}
