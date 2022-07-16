package com.example.admindashboard.faculties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindashboard.R;
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.profile.ProfileClassUser;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.sports.AddSportActivity;
import com.example.admindashboard.sports.SportsActivity;
import com.example.admindashboard.students.EditStudentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class EditFacultyActivity extends AppCompatActivity {
    private String pageHeading,userType,name,phone,email,search,sem,gender,dob,imageUrl,status,password,regNo,role,branch,roleData,designation,qualifications;
    private CountryCodePicker ccpPhone;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;

    private TextView tvHeading;
    private EditText etName,etPhone,etEmail,etDesignation,etQualifications;
    private Button editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_faculty);

        spProfile = new ProfileSharedPreferences(this);
        profileData=spProfile.getUserProfile();

        Intent previousActivityIntent=getIntent();
        pageHeading=previousActivityIntent.getStringExtra("pageHeading");
        userType=previousActivityIntent.getStringExtra("userType");
        regNo=previousActivityIntent.getStringExtra("regNo");
        role=previousActivityIntent.getStringExtra("role");
        branch=previousActivityIntent.getStringExtra("branch");

        tvHeading=findViewById(R.id.textViewHeading);
        etEmail=findViewById(R.id.editTextTEmail);
        ccpPhone=findViewById(R.id.ccpNumber);
        etName=findViewById(R.id.editTextName);
        etPhone=findViewById(R.id.editTextPhone);
        etQualifications=findViewById(R.id.editTextQualifications);
        etDesignation=findViewById(R.id.editTextDesignation);
        editButton=findViewById(R.id.saveEditedTaskButton);

        tvHeading.setText(pageHeading);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(branch).child(userType).child(regNo);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name=snapshot.child("name").getValue(String.class);
                    email=snapshot.child("email").getValue(String.class);
                    phone=snapshot.child("phone").getValue(String.class);
                    password=snapshot.child("password").getValue(String.class);
                    imageUrl=snapshot.child("imageUrl").getValue(String.class);
                    dob=snapshot.child("dob").getValue(String.class);
                    gender=snapshot.child("gender").getValue(String.class);
                    qualifications=snapshot.child("qualifications").getValue(String.class);
                    designation=snapshot.child("designation").getValue(String.class);
                    status=snapshot.child("status").getValue(String.class);
                    search=snapshot.child("search").getValue(String.class);
                    roleData=snapshot.child("role").getValue(String.class);

                    etName.setText(name);
                    etEmail.setText(email);
                    etDesignation.setText(designation);
                    etQualifications.setText(qualifications);
                    etPhone.setText(phone.substring(3));

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isConnected(EditFacultyActivity.this)){
                                Toast.makeText(EditFacultyActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!validateName()|!validatePhone()|!validateEmail()|!validateDesignation()|!validateQualifications())
                                return;
                            email=etEmail.getText().toString().trim();
                            name=etName.getText().toString().trim();
                            designation=etDesignation.getText().toString().trim();
                            qualifications=etQualifications.getText().toString().trim();
                            String userEnteredPhone=etPhone.getText().toString().trim();
                            phone="+"+ccpPhone.getSelectedCountryCode()+userEnteredPhone;
                            search=name.toLowerCase();
                            ProfileClassUser user=new ProfileClassUser(name,regNo,branch,phone,email,gender,dob,roleData,password,imageUrl,status,search,qualifications,designation,true);
                            reference.setValue(user);
                            Intent intent = new Intent(EditFacultyActivity.this, AddDeleteUserActivity.class);
                            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validatePhone(){
        String val = etPhone.getText().toString().trim();
        if (val.isEmpty()) {
            etPhone.setError("Enter valid phone number");
            return false;
        }
        else if(val.length()!=10){
            etPhone.setError("Enter valid phone number");
            return false;
        }
        else if(!val.matches("[0-9]+")){
            etPhone.setError("Enter valid phone number");
            return false;
        }
        else {
            etPhone.setError(null);
            return true;
        }
    }

    private boolean validateName(){
        String name=etName.getText().toString().trim();
        if(name.isEmpty()) {
            etName.setError("Field cannot be empty");
            return false;
        }
        else{
            etName.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        String email=etEmail.getText().toString().trim();
        String checkEmail="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.isEmpty()) {
            etEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!email.matches(checkEmail)){
            etEmail.setError("Invalid Email");
            return false;
        }
        else{
            etEmail.setError(null);
            return true;
        }
    }

    private boolean isConnected(EditFacultyActivity activity) {
        ConnectivityManager manager= (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mob=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi!=null&&wifi.isConnected())||(mob!=null&&mob.isConnected())){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateQualifications(){
        String qualifications=etQualifications.getText().toString().trim();
        if(qualifications.isEmpty()) {
            etQualifications.setError("Field cannot be empty");
            return false;
        }
        else{
            etQualifications.setError(null);
            return true;
        }

    } private boolean validateDesignation(){
        String designation=etDesignation.getText().toString().trim();
        if(designation.isEmpty()) {
            etDesignation.setError("Field cannot be empty");
            return false;
        }
        else{
            etDesignation.setError(null);
            return true;
        }

    }
}