package com.example.admindashboard.clubs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admindashboard.R;
import com.example.admindashboard.sports.AddSportActivity;
import com.example.admindashboard.sports.Sport;
import com.example.admindashboard.sports.SportsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddClubActivity extends AppCompatActivity {
    private Spinner clubsTypeSpinner;
    private EditText editTextClubName,editTextClubGenre,editTextLeadName,editTextLeadContact,editTextClubDescription;
    private Button addButton;

    private String clubName,clubType,clubGenre,clubLeadName,clubLeadContactNumber,clubDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club);

        clubsTypeSpinner=findViewById(R.id.clubsTypeSpinner);
        editTextClubName=findViewById(R.id.editTextClubName);
        editTextClubGenre=findViewById(R.id.editTextClubGenre);
        editTextLeadName=findViewById(R.id.editTextLeadName);
        editTextLeadContact=findViewById(R.id.editTextLeadContact);
        editTextClubDescription=findViewById(R.id.editTextClubDescription);
        addButton=findViewById(R.id.addClubsButton);

        setupSpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddClubActivity.this)){
                    Toast.makeText(AddClubActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePhone()|!validateClubName()|!validateClubGenre()|!validateLeadName()|!validateClubDescription())
                    return;
                clubName=editTextClubName.getText().toString().trim();
                clubType=clubsTypeSpinner.getSelectedItem().toString();
                clubGenre=editTextClubGenre.getText().toString().trim();
                clubLeadName=editTextLeadName.getText().toString().trim();
                clubLeadContactNumber=editTextLeadContact.getText().toString().trim();
                clubDescription=editTextClubDescription.getText().toString().trim();

                Query checkExistenceQuery = FirebaseDatabase.getInstance().getReference("root").child("clubs").child(clubType).orderByChild("clubName").equalTo(clubName);
                checkExistenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(AddClubActivity.this,"Sport already exists.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("root").child("clubs").child(clubType);
                            Club newClub=new Club(clubName,clubType,clubGenre,clubLeadName,clubLeadContactNumber,clubDescription);
                            reference.child(clubName).setValue(newClub);
                            Intent intent = new Intent(AddClubActivity.this, ClubActivity.class);
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
        });
    }

    private void setupSpinner(){
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.clubs_spinner, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubsTypeSpinner.setAdapter(adapter1);
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

    private boolean validateClubName(){
        String clubName=editTextClubName.getText().toString().trim();
        if(clubName.isEmpty()) {
            editTextClubName.setError("Field cannot be empty");
            return false;
        }
        else{
            editTextClubName.setError(null);
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

    private boolean isConnected(AddClubActivity activity) {
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