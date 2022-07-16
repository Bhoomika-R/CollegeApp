package com.example.admindashboard.sports;

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
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.profile.AddUserClass;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.students.AddStudentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddSportActivity extends AppCompatActivity {
    private Spinner sportsGenderSpinner,sportsTeamSpinner;
    private EditText etSportName,etCaptainName,etCaptainPhone,etTeamLegacy;
    private Button addButton;

    private String sportGender,sportType,sportName,captainContactNumber,captain,teamLegacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);


        sportsGenderSpinner=findViewById(R.id.sportGenderSpinner);
        sportsTeamSpinner=findViewById(R.id.sportTeamSpinner);
        etSportName=findViewById(R.id.editTextSportName);
        etCaptainName=findViewById(R.id.editTextCaptainName);
        etCaptainPhone=findViewById(R.id.editTextCaptainContact);
        etTeamLegacy=findViewById(R.id.editTextClubDescription);
        addButton=findViewById(R.id.addSportButton);

        setupSpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddSportActivity.this)){
                    Toast.makeText(AddSportActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePhone()|!validateSportName()|!validateCaptainName()|!validateTeamLegacy())
                    return;
                sportName=etSportName.getText().toString().trim();
                captain=etCaptainName.getText().toString().trim();
                teamLegacy=etTeamLegacy.getText().toString().trim();
                captainContactNumber=etCaptainPhone.getText().toString().trim();
                sportGender=sportsGenderSpinner.getSelectedItem().toString();
                sportType=sportsTeamSpinner.getSelectedItem().toString();

                Query checkExistenceQuery = FirebaseDatabase.getInstance().getReference("root").child("sports").child(sportGender).child(sportType).orderByChild("sportName").equalTo(sportName);
                checkExistenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(AddSportActivity.this,"Sport already exists.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("root").child("sports").child(sportGender).child(sportType);
                            Sport newSport=new Sport(sportName,sportGender,sportType,captain,captainContactNumber,teamLegacy);
                            reference.child(sportName).setValue(newSport);
                            Intent intent = new Intent(AddSportActivity.this, SportsActivity.class);
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
                R.array.sports_gender_spinner, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsGenderSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.sports_type_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsTeamSpinner.setAdapter(adapter2);
    }

    private boolean validatePhone(){
        String phone = etCaptainPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            etCaptainPhone.setError("Enter valid phone number");
            return false;
        }
        else if(phone.length()!=10){
            etCaptainPhone.setError("Enter valid phone number");
            return false;
        }
        else if(!phone.matches("[0-9]+")){
            etCaptainPhone.setError("Enter valid phone number");
            return false;
        }
        else {
            etCaptainPhone.setError(null);
            return true;
        }
    }

    private boolean validateSportName(){
        String sportName=etSportName.getText().toString().trim();
        if(sportName.isEmpty()) {
            etSportName.setError("Field cannot be empty");
            return false;
        }
        else{
            etSportName.setError(null);
            return true;
        }
    }

    private boolean validateCaptainName(){
        String captainName=etCaptainName.getText().toString().trim();
        if(captainName.isEmpty()) {
            etCaptainName.setError("Field cannot be empty");
            return false;
        }
        else{
            etCaptainName.setError(null);
            return true;
        }
    }

    private boolean validateTeamLegacy(){
        String teamLegacy=etTeamLegacy.getText().toString().trim();
        if(teamLegacy.isEmpty()) {
            etTeamLegacy.setError("Field cannot be empty");
            return false;
        }
        else{
            etTeamLegacy.setError(null);
            return true;
        }
    }

    private boolean isConnected(AddSportActivity activity) {
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

