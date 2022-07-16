package com.example.admindashboard.clubs;

public class Club {
    private String clubName,clubType,clubGenre,clubLeadName,clubLeadContactNumber,clubDescription;

    public Club(String clubName, String clubType, String clubGenre, String clubLeadName, String clubLeadContactNumber, String clubDescription) {
        this.clubName = clubName;
        this.clubType = clubType;
        this.clubGenre = clubGenre;
        this.clubLeadName = clubLeadName;
        this.clubLeadContactNumber = clubLeadContactNumber;
        this.clubDescription = clubDescription;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    public String getClubGenre() {
        return clubGenre;
    }

    public void setClubGenre(String clubGenre) {
        this.clubGenre = clubGenre;
    }

    public String getClubLeadName() {
        return clubLeadName;
    }

    public void setClubLeadName(String clubLeadName) {
        this.clubLeadName = clubLeadName;
    }

    public String getClubLeadContactNumber() {
        return clubLeadContactNumber;
    }

    public void setClubLeadContactNumber(String clubLeadContactNumber) {
        this.clubLeadContactNumber = clubLeadContactNumber;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }
}
