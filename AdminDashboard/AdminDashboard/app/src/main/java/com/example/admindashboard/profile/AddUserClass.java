package com.example.admindashboard.profile;

public class AddUserClass {
    private String name,regNo,phone,search,role,branch,qualifications,designation;
    private boolean registered;

    public AddUserClass(String name, String regNo, String phone,String search,String role,String branch, boolean registered) {
        this.name = name;
        this.regNo = regNo;
        this.phone = phone;
        this.search = search;
        this.role = role;
        this.branch = branch;
        this.registered = registered;
    }

    public AddUserClass(String name, String regNo, String phone, String search, String role, String branch, String qualifications, String designation, boolean registered) {
        this.name = name;
        this.regNo = regNo;
        this.phone = phone;
        this.search = search;
        this.role = role;
        this.branch = branch;
        this.qualifications = qualifications;
        this.designation = designation;
        this.registered = registered;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public String getSearch() {
        return search;
    }

    public String getRole() {
        return role;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
