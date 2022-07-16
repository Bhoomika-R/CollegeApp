package com.example.admindashboard.clubs;

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
import com.example.admindashboard.sports.EditSportActivity;
import com.example.admindashboard.sports.Sport;
import com.example.admindashboard.sports.SportsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditClubActivity extends AppCompatActivity {
    private EditText editTextClubGenre,editTextLeadContact,editTextClubDescription,editTextLeadName;
    private TextView tvClubName;
    private Button editButton;

    private String clubName,clubType,clubGenre,clubLeadName,clubLeadContactNumber,clubDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        editTextClubGenre=findViewById(R.id.editTextClubGenre);
        editTextLeadContact=findViewById(R.id.editTextLeadContact);
        editTextClubDescription=findViewById(R.id.editTextClubDescription);
        editTextLeadName=findViewById(R.id.editTextLeadName);
        editButton=findViewById(R.id.editClubsButton);
        tvClubName=findViewById(R.id.tvClubName);

        Intent previousIntent=getIntent();
        clubName=previousIntent.getStringExtra("clubName").toString().trim();
        clubType=previousIntent.getStringExtra("clubType").toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("clubs").child(clubType).child(clubName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    clubGenre=snapshot.child("clubGenre").getValue(String.class);
                    clubLeadName=snapshot.child("clubLeadName").getValue(String.class);
                    clubLeadContactNumber=snapshot.child("clubLeadContactNumber").getValue(String.class);
                    clubDescription=snapshot.child("clubDescription").getValue(String.class);

                    tvClubName.setText(clubName);
                    editTextClubGenre.setText(clubGenre);
                    editTextLeadName.setText(clubLeadName);
                    editTextLeadContact.setText(clubLeadContactNumber);
                    editTextClubDescription.setText(clubDescription);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isConnected(EditClubActivity.this)){
                                Toast.makeText(EditClubActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!validatePhone()|!validateClubGenre()|!validateLeadName()|!validateClubDescription())
                                return;

                            clubGenre=editTextClubGenre.getText().toString().trim();
                            clubLeadName=editTextLeadName.getText().toString().trim();
                            clubLeadContactNumber=editTextLeadContact.getText().toString().trim();
                            clubDescription=editTextClubDescription.getText().toString().trim();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("root").child("clubs").child(clubType);
                            Club newClub=new Club(clubName,clubType,clubGenre,clubLeadName,clubLeadContactNumber,clubDescription);
                            reference.child(clubName).setValue(newClub);
                            Intent intent = new Intent(EditClubActivity.this, ClubActivity.class);
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
        String phone = editTextLeadContact.getText().toString().trim();
        if (phone.isEmpty()) {
            editTextLeadContact.setError("Enter valid phone number");
            return false;
        }
        else if(phone.length()!=10){
            editTextLeadContact.setError("Enter valid phone number");
            return false;
        }
        else if(!phone.matches("[0-9]+")){
            editTextLeadContact.setError("Enter valid phone number");
            return false;
        }
        else {
            editTextLeadContact.setError(null);
            return true;
        }
    }

    private boolean validateLeadName(){
        String leadName=editTextLeadName.getText().toString().trim();
        if(leadName.isEmpty()) {
            editTextLeadName.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextLeadName.setError(null);
            return true;
        }
    }

    private boolean validateClubGenre(){
        String genre=editTextClubGenre.getText().toString().trim();
        if(genre.isEmpty()) {
            editTextClubGenre.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextClubGenre.setError(null);
            return true;
        }
    }

    private boolean validateClubDescription(){
        String description=editTextClubDescription.getText().toString().trim();
        if(description.isEmpty()) {
            editTextClubDescription.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextClubDescription.setError(null);
            return true;
        }
    }

    private boolean isConnected(EditClubActivity activity) {
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