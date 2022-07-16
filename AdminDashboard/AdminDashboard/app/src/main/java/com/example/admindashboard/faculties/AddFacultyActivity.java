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
import com.example.admindashboard.profile.AddUserClass;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class AddFacultyActivity extends AppCompatActivity {
    private String pageHeading,userType,name,phone,regNo,search,qualifications,designation;

    private CountryCodePicker ccpPhone;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;

    private TextView tvHeading;
    private EditText etName,etPhone,etRegNo,etDesignation,etQualifications;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        spProfile = new ProfileSharedPreferences(this);
        profileData=spProfile.getUserProfile();

        Intent previousActivityIntent=getIntent();
        pageHeading=previousActivityIntent.getStringExtra("pageHeading");
        userType=previousActivityIntent.getStringExtra("userType");

        tvHeading=findViewById(R.id.textViewHeading);
        ccpPhone=findViewById(R.id.ccpNumber);
        etName=findViewById(R.id.editTextName);
        addButton=findViewById(R.id.addButton);
        etRegNo=findViewById(R.id.editTextRegNo);
        etPhone=findViewById(R.id.editTextPhone);
        etDesignation=findViewById(R.id.editTextDesignation);
        etQualifications=findViewById(R.id.editTextQualifications);
        tvHeading.setText(pageHeading);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddFacultyActivity.this)){
                    Toast.makeText(AddFacultyActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePhone()|!validateName()|!validateRegNo()|!validateDesignation()|!validateQualifications())
                    return;
                String userEnteredPhone=etPhone.getText().toString().trim();
                phone="+"+ccpPhone.getSelectedCountryCode()+userEnteredPhone;
                name=etName.getText().toString().trim();
                regNo=etRegNo.getText().toString().trim();
                search=name.toLowerCase();
                designation=etDesignation.getText().toString().trim();
                qualifications=etQualifications.getText().toString().trim();
                Query checkExistenceQuery = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userType).orderByChild("regNo").equalTo(regNo);
                checkExistenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(AddFacultyActivity.this,"User already exists.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Query checkPhoneQuery = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userType).orderByChild("phone").equalTo(phone);
                            checkPhoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Toast.makeText(AddFacultyActivity.this,"Phone number already registered.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userType);
                                        String role;
                                        if(userType.equals("student"))
                                            role="Student";
                                        else
                                            role="Faculty";
                                        AddUserClass newUser=new AddUserClass(name,regNo,phone,search,role,profileData.get(ProfileSharedPreferences.KEY_BRANCH),qualifications,designation,false);
                                        reference.child(regNo).setValue(newUser);
                                        Intent intent = new Intent(AddFacultyActivity.this, AddDeleteUserActivity.class);
                                        intent.putExtra("pageHeading",pageHeading);
                                        intent.putExtra("userType",userType);
                                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    private boolean validateRegNo(){
        String regNo=etRegNo.getText().toString().trim();
        String checkSpaces="\\A\\w{1,20}\\z";
        if(regNo.isEmpty()) {
            etRegNo.setError("Field cannot be empty");
            return false;
        }
        else if(regNo.length()!=10){
            etRegNo.setError("Invalid ID");
            return false;
        }
        else if(!regNo.matches(checkSpaces)){
            etRegNo.setError("No whitespaces allowed");
            return false;
        }
        else{
            etRegNo.setError(null);
            return true;
        }

    }

    private boolean isConnected(AddFacultyActivity activity) {
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
}