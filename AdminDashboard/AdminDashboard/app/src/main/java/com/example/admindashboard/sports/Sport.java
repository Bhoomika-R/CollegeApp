package com.example.admindashboard.sports;

public class Sport {

    private String sportName;
    private String sportType;
    private String sportGender;
    private String captain;
    private String captainContactNumber;
    private String teamLegacy;

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public Sport(String sportName,String sportGender, String sportType,String captain,String captainContactNumber,String teamLegacy) {
        this.sportName = sportName;
        this.sportGender = sportGender;
        this.sportType = sportType;
        this.captain = captain;
        this.captainContactNumber = captainContactNumber;
        this.teamLegacy = teamLegacy;
    }

    public String getSportGender() {
        return sportGender;
    }

    public void setSportGender(String sportGender) {
        this.sportGender = sportGender;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getCaptainContactNumber() {
        return captainContactNumber;
    }

    public void setCaptainContactNumber(String captainContactNumber) {
        this.captainContactNumber = captainContactNumber;
    }

    public String getTeamLegacy() {
        return teamLegacy;
    }

    public void setTeamLegacy(String teamLegacy) {
        this.teamLegacy = teamLegacy;
    }

    public String getSportName() {
        return sportName;
    }

    public String getSportType() {
        return sportType;
    }
}
