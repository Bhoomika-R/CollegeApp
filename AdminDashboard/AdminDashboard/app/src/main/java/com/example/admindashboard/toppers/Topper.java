package com.example.admindashboard.toppers;

public class Topper {
    private String name,branch,year,regNo,cgpa;

    public Topper(String name, String branch, String year, String regNo, String cgpa) {
        this.name = name;
        this.branch = branch;
        this.year = year;
        this.regNo = regNo;
        this.cgpa = cgpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }
}
