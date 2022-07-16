package com.example.mycollegeapp.ui.teachers;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TeachersDetailActivity extends AppCompatActivity {
    private TextView teacherName, designation, department, email, phone, qualifications;

    private String regNo,role,branch;

    private ProfileSharedPreferences spProfile;
    private HashMap<String,String> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        spProfile = new ProfileSharedPreferences(TeachersDetailActivity.this);
        profileData=spProfile.getUserProfile();

        teacherName=findViewById(R.id.facultyName);
        designation=findViewById(R.id.facultyDesignation);
        department=findViewById(R.id.facultyDepartment);
        email=findViewById(R.id.facultyEmail);
        phone=findViewById(R.id.facultyContactNumber);
        qualifications=findViewById(R.id.facultyQualifications);

        regNo=getIntent().getStringExtra("regNo");
        role=getIntent().getStringExtra("role");
        branch=getIntent().getStringExtra("branch");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(branch).child(role).child(regNo);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    teacherName.setText(snapshot.child("name").getValue(String.class));
                    phone.setText(snapshot.child("phone").getValue(String.class));
                    email.setText(snapshot.child("email").getValue(String.class));
                    qualifications.setText(snapshot.child("qualifications").getValue(String.class));
                    designation.setText(snapshot.child("designation").getValue(String.class));
                    switch(branch){
                        case "CSE":
                            department.setText("Computer Science and Engineering");
                            break;
                        case "ISE":
                            department.setText("Information Science and Engineering");
                            break;
                        case "ECE":
                            department.setText("Electronics and Communication Engineering");
                            break;
                        case "ME":
                            department.setText("Mechanical Engineering");
                            break;
                        case "BT":
                            department.setText("Bio-Technology");
                            break;
                        case "MT":
                            department.setText("Mechatronics");
                            break;
                        case "EEE":
                            department.setText("Electrical and Electronics Engineering");
                            break;
                        case "AU":
                            department.setText("Automobile Engineering");
                            break;
                    }
                        }
                    }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}