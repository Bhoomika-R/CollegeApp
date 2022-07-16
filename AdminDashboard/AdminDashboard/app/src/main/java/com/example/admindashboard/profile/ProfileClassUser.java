package com.example.admindashboard.profile;

public class ProfileClassUser {
        private String name,regNo,branch,phone,email,gender,dob,role,password,sem,qualifications,designation;
        private boolean registered;
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        private String status;
        private String search;

        public ProfileClassUser(){}

        public ProfileClassUser(String name, String regNo, String branch, String phone, String email, String gender, String dob, String role, String password, String imageUrl, String status, String search,String qualifications,String designation,boolean registered) {
            this.name = name;
            this.regNo = regNo;
            this.branch = branch;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
            this.dob = dob;
            this.role = role;
            this.password = password;
            this.imageUrl = imageUrl;
            this.status = status;
            this.qualifications = qualifications;
            this.designation = designation;
            this.registered= registered;
            this.search= search;
        }
        public ProfileClassUser(String name, String regNo, String branch, String phone, String email, String gender, String dob, String role, String password,String sem, String imageUrl, String status, String search,boolean registered) {
            this.name = name;
            this.regNo = regNo;
            this.branch = branch;
            this.phone = phone;
            this.email = email;
            this.gender = gender;
            this.dob = dob;
            this.role = role;
            this.password = password;
            this.sem=sem;
            this.imageUrl = imageUrl;
            this.status = status;
            this.registered= registered;
            this.search= search;
        }

       public ProfileClassUser(String name, String regNo, String phone, String role, boolean registered, String search) {
        this.name = name;
        this.regNo = regNo;
        this.phone = phone;
        this.role = role;
        this.registered = registered;
        this.search = search;
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

        public void setName(String name) {
            this.name = name;
        }

        public String getRegNo() {
            return regNo;
        }

        public void setRegNo(String regNo) {
            this.regNo = regNo;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isRegistered() {
            return registered;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }

        public String getSem() {
            return sem;
        }

        public void setSem(String sem) {
            this.sem = sem;
        }


}
