package com.example.admindashboard.toppers;

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
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.students.AddStudentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddTopperActivity extends AppCompatActivity {
    private EditText editTextTRegNo,editTextCgpa;
    private Button addButton;
    private TextView textViewYear;

    private ProfileSharedPreferences profile;
    private HashMap<String,String> profileData;
    private String branch,regNo,cgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topper);

        profile=new ProfileSharedPreferences(AddTopperActivity.this);
        profileData=profile.getUserProfile();
        branch=profileData.get(ProfileSharedPreferences.KEY_BRANCH);

        addButton = findViewById(R.id.addButton);
        editTextTRegNo = findViewById(R.id.editTextTRegNo);
        editTextCgpa = findViewById(R.id.editTextCgpa);
        textViewYear = findViewById(R.id.textViewYear);

        String heading=getIntent().getStringExtra("TVHeading");
        String year=getIntent().getStringExtra("Year");

        textViewYear.setText(year);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddTopperActivity.this)){
                    Toast.makeText(AddTopperActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validateRegNo()|!validateCgpa())
                    return;
                regNo=editTextTRegNo.getText().toString().trim();
                cgpa=editTextCgpa.getText().toString().trim();
                Query checkExistenceInTopperList= FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child(year).orderByChild("regNo").equalTo(regNo);
                checkExistenceInTopperList.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Query checkExistenceInProfiles= FirebaseDatabase.getInstance().getReference("root").child("profiles").child(branch).child("student").orderByChild("regNo").equalTo(regNo);
                            checkExistenceInProfiles.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        Toast.makeText(AddTopperActivity.this,"No such user in database.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Topper topper=new Topper(snapshot.child(regNo).child("name").getValue(String.class),branch,year,regNo,cgpa);
                                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child(year);
                                        reference.child(regNo).setValue(topper);
                                        Intent intent = new Intent(AddTopperActivity.this, TopperListActivity.class);
                                        intent.putExtra("Year",year);
                                        intent.putExtra("TVHeading",heading);
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
                        else {
                            Toast.makeText(AddTopperActivity.this,"Topper already exists.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private boolean validateRegNo(){
        String regNo=editTextTRegNo.getText().toString().trim();
        String checkSpaces="\\A\\w{1,20}\\z";
        if(regNo.isEmpty()) {
            editTextTRegNo.setError("Field cannot be empty");
            return false;
        }
        else if(regNo.length()!=10){
            editTextTRegNo.setError("Invalid ID");
            return false;
        }
        else if(!regNo.matches(checkSpaces)){
            editTextTRegNo.setError("No whitespaces allowed");
            return false;
        }
        else{
            editTextTRegNo.setError(null);
            return true;
        }
    }

    private boolean validateCgpa(){
        String cgpa=editTextCgpa.getText().toString().trim();
        if(cgpa.isEmpty()) {
            editTextCgpa.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextCgpa.setError(null);
            return true;
        }

    }

    private boolean isConnected(AddTopperActivity activity) {
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