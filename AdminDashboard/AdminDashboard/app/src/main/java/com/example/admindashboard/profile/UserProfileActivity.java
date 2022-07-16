package com.example.admindashboard.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admindashboard.R;
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.faculties.EditFacultyActivity;
import com.example.admindashboard.students.EditStudentActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvName,tvRegNo,tvBranch,tvSem,tvPhone,tvEmail,tvGender,tvDOB,tvImage,tvRole;
    private LinearLayout linearLayout;
    private CircleImageView image_profile;
    private ImageView ivDobAndDesignation;
    private FloatingActionButton fabDelete,fabEdit;

    private String pageHeading,userType,regNo,role,branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvName=findViewById(R.id.tvName);
        image_profile=findViewById(R.id.profileImageView);
        ivDobAndDesignation=findViewById(R.id.ivDobAndDesignation);
        tvRegNo=findViewById(R.id.tvUSN);
        tvBranch=findViewById(R.id.tvDept);
        tvSem=findViewById(R.id.tvSem);
        tvPhone=findViewById(R.id.tvPhone);
        tvEmail=findViewById(R.id.tvEmail);
        tvGender=findViewById(R.id.tvGender);
        tvDOB=findViewById(R.id.tvDOB);
        tvImage=findViewById(R.id.tvImage);
        tvRole=findViewById(R.id.tvRole);
        fabDelete=findViewById(R.id.floatingActionButtonDelete);
        fabEdit=findViewById(R.id.floatingActionButtonEdit);
        linearLayout=findViewById(R.id.linearLayout5);

        Intent previousActivityIntent=getIntent();
        regNo=previousActivityIntent.getStringExtra("regNo");
        role=previousActivityIntent.getStringExtra("role");
        branch=previousActivityIntent.getStringExtra("branch");
        pageHeading=previousActivityIntent.getStringExtra("pageHeading");
        userType=previousActivityIntent.getStringExtra("userType");

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(branch).child(role).child(regNo);
                                reference.removeValue();
                                Intent getBack=new Intent(UserProfileActivity.this, AddDeleteUserActivity.class);
                                getBack.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getBack.putExtra("pageHeading",pageHeading);
                                getBack.putExtra("userType",userType);
                                startActivity(getBack);
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(branch).child(role).child(regNo);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   boolean registeredOrNot=snapshot.child("registered").getValue(Boolean.class);
                   tvName.setText(snapshot.child("name").getValue(String.class));
                   tvRegNo.setText(snapshot.child("regNo").getValue(String.class));
                   tvBranch.setText(snapshot.child("branch").getValue(String.class));
                   tvRole.setText(snapshot.child("role").getValue(String.class));
                   tvPhone.setText(snapshot.child("phone").getValue(String.class));
                   if(registeredOrNot){
                       fabEdit.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String editPageHeading;
                               Intent goToEdit;
                               if(pageHeading.equals("Add Student")) {
                                   editPageHeading = "Edit Student";
                                   goToEdit=new Intent(UserProfileActivity.this, EditStudentActivity.class);
                               }
                               else {
                                   editPageHeading = "Edit Faculty";
                                   goToEdit=new Intent(UserProfileActivity.this, EditFacultyActivity.class);
                               }
                               goToEdit.putExtra("pageHeading",editPageHeading);
                               goToEdit.putExtra("userType",userType);
                               goToEdit.putExtra("regNo", regNo);
                               goToEdit.putExtra("role", role);
                               goToEdit.putExtra("branch", branch);
                               startActivity(goToEdit);
                           }
                       });
                       if(snapshot.child("imageUrl").getValue(String.class).equals("default")){
                        tvImage.setText(getNameAbbr(snapshot.child("name").getValue(String.class)));
                       }
                       else{
                           tvImage.setVisibility(View.GONE);
                           Glide.with(UserProfileActivity.this).load(snapshot.child("imageUrl").getValue(String.class)).into(image_profile);
                       }
                   tvEmail.setText(snapshot.child("email").getValue(String.class));
                   tvGender.setText(snapshot.child("gender").getValue(String.class));
                   if(snapshot.child("role").getValue(String.class).toLowerCase(Locale.ROOT).equals("student")){
                     tvSem.setText(snapshot.child("sem").getValue(String.class)+" Semester");
                     tvDOB.setText(snapshot.child("dob").getValue(String.class));
                   }
                   else{
                     tvSem.setText(snapshot.child("qualifications").getValue(String.class));
                     ivDobAndDesignation.setImageResource(R.drawable.ic_baseline_label_important_24);
                     tvDOB.setText(snapshot.child("dob").getValue(String.class));
                   }
                   }
                   else{
                       fabEdit.setVisibility(View.GONE);
                       tvImage.setText(getNameAbbr(snapshot.child("name").getValue(String.class)));
                       if(snapshot.child("role").getValue(String.class).toLowerCase(Locale.ROOT).equals("student")){
                           tvDOB.setText(snapshot.child("dob").getValue(String.class));
                       }
                       else{
                           tvSem.setText(snapshot.child("qualifications").getValue(String.class));
                           ivDobAndDesignation.setImageResource(R.drawable.ic_baseline_label_important_24);
                           tvDOB.setText(snapshot.child("designation").getValue(String.class));
                       }
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getNameAbbr(String name) {
        StringBuilder initials = new StringBuilder();
        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }
        String temp = initials.toString().toUpperCase(Locale.ROOT).trim();
        StringBuilder imageText = new StringBuilder();
        if (temp.length() == 1)
            imageText.append(temp.charAt(0));
        else {
            imageText.append(temp.charAt(0));
            imageText.append(temp.charAt(1));
        }

        return imageText.toString();
    }
}