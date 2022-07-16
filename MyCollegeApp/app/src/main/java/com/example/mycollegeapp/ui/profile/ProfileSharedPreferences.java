package com.example.mycollegeapp.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ProfileSharedPreferences {

    SharedPreferences spProfile;
    SharedPreferences.Editor spEditor;
    Context ct;

    public static final String IS_LOGIN="IsLoggedIn";

    public static final String KEY_NAME="name";
    public static final String KEY_REGNO="regNo";
    public static final String KEY_EMAIL="email";
    public static final String KEY_PHONE="phone";
    public static final String KEY_ROLE="role";
    public static final String KEY_GENDER="gender";
    public static final String KEY_DOB="dob";
    public static final String KEY_BRANCH="branch";
    public static final String KEY_SEM="sem";
    public static final String KEY_NAME_ABBR="nameAbbr";
    public static final String KEY_IMAGE_URL="imageUrl";
    public static final String KEY_QUALIFICATIONS="qualifications";
    public static final String KEY_DESIGNATION="designation";



    public ProfileSharedPreferences(Context ct){
        this.ct=ct;
        spProfile=ct.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        spEditor=spProfile.edit();
    }

    public void createLoginSessionForFaculty(String name,String email,String phone,String gender,String role,String regNo,String dob,String branch,String nameAbbr,String imageUrl,String qualifications,String designation){

        spEditor.putBoolean(IS_LOGIN,true);

        spEditor.putString(KEY_NAME,name);
        spEditor.putString(KEY_REGNO,regNo);
        spEditor.putString(KEY_PHONE,phone);
        spEditor.putString(KEY_EMAIL,email);
        spEditor.putString(KEY_ROLE,role);
        spEditor.putString(KEY_BRANCH,branch);
        spEditor.putString(KEY_GENDER,gender);
        spEditor.putString(KEY_DOB,dob);
        spEditor.putString(KEY_NAME_ABBR,nameAbbr);
        spEditor.putString(KEY_IMAGE_URL,imageUrl);
        spEditor.putString(KEY_QUALIFICATIONS,qualifications);
        spEditor.putString(KEY_DESIGNATION,designation);


        spEditor.commit();
    }

    public void createLoginSessionForStudent(String name,String email,String phone,String gender,String role,String regNo,String dob,String branch,String sem,String nameAbbr,String imageUrl){

        spEditor.putBoolean(IS_LOGIN,true);

        spEditor.putString(KEY_NAME,name);
        spEditor.putString(KEY_REGNO,regNo);
        spEditor.putString(KEY_PHONE,phone);
        spEditor.putString(KEY_EMAIL,email);
        spEditor.putString(KEY_ROLE,role);
        spEditor.putString(KEY_BRANCH,branch);
        spEditor.putString(KEY_GENDER,gender);
        spEditor.putString(KEY_DOB,dob);
        spEditor.putString(KEY_SEM,sem);
        spEditor.putString(KEY_NAME_ABBR,nameAbbr);
        spEditor.putString(KEY_IMAGE_URL,imageUrl);

        spEditor.commit();
    }

    public HashMap<String,String> getUserProfile(){
        HashMap<String,String> userProfile=new HashMap<>();

        userProfile.put(KEY_NAME,spProfile.getString(KEY_NAME,null));
        userProfile.put(KEY_EMAIL,spProfile.getString(KEY_EMAIL,null));
        userProfile.put(KEY_PHONE,spProfile.getString(KEY_PHONE,null));
        userProfile.put(KEY_GENDER,spProfile.getString(KEY_GENDER,null));
        userProfile.put(KEY_ROLE,spProfile.getString(KEY_ROLE,null));
        userProfile.put(KEY_REGNO,spProfile.getString(KEY_REGNO,null));
        userProfile.put(KEY_DOB,spProfile.getString(KEY_DOB,null));
        userProfile.put(KEY_BRANCH,spProfile.getString(KEY_BRANCH,null));
        userProfile.put(KEY_NAME_ABBR,spProfile.getString(KEY_NAME_ABBR,null));
        userProfile.put(KEY_IMAGE_URL,spProfile.getString(KEY_IMAGE_URL,null));
        if((spProfile.getString(KEY_ROLE,null)).equals("Student")){
            userProfile.put(KEY_SEM,spProfile.getString(KEY_SEM,null));
        }
        else {
            userProfile.put(KEY_QUALIFICATIONS, spProfile.getString(KEY_QUALIFICATIONS, null));
            userProfile.put(KEY_DESIGNATION, spProfile.getString(KEY_DESIGNATION, null));
        }
        return userProfile;
    }

    public boolean checkLogin(){
        return spProfile.getBoolean(IS_LOGIN,false);
    }

    public void logoutUserFromSession(){
        spEditor.clear();
        spEditor.commit();
    }

}
