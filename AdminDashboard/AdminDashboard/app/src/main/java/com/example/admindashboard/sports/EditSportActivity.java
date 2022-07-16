package com.example.admindashboard.sports;

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
import com.example.admindashboard.students.EditStudentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSportActivity extends AppCompatActivity {
    private EditText etCaptainName,etCaptainPhone,etTeamLegacy;
    private TextView tvSportName;
    private Button editButton;

    private String sportName,sportGender, sportType,captain,captainContactNumber,teamLegacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sport);

        etCaptainName=findViewById(R.id.editTextCaptainName);
        etCaptainPhone=findViewById(R.id.editTextCaptainContact);
        etTeamLegacy=findViewById(R.id.editTextClubDescription);
        editButton=findViewById(R.id.editSportButton);
        tvSportName=findViewById(R.id.tvSportName);

        Intent previousIntent=getIntent();
        sportName=previousIntent.getStringExtra("sportName").toString().trim();
        sportGender=previousIntent.getStringExtra("sportGender").toString().trim();
        sportType=previousIntent.getStringExtra("sportType").toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("sports").child(sportGender).child(sportType).child(sportName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    captain=snapshot.child("captain").getValue(String.class);
                    captainContactNumber=snapshot.child("captainContactNumber").getValue(String.class);
                    teamLegacy=snapshot.child("teamLegacy").getValue(String.class);

                    tvSportName.setText(sportName);
                    etCaptainName.setText(captain);
                    etCaptainPhone.setText(captainContactNumber);
                    etTeamLegacy.setText(teamLegacy);
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isConnected(EditSportActivity.this)){
                                Toast.makeText(EditSportActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!validatePhone()|!validateCaptainName()|!validateTeamLegacy())
                                return;

                            captain=etCaptainName.getText().toString().trim();
                            teamLegacy=etTeamLegacy.getText().toString().trim();
                            captainContactNumber=etCaptainPhone.getText().toString().trim();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("root").child("sports").child(sportGender).child(sportType);
                            Sport newSport=new Sport(sportName,sportGender,sportType,captain,captainContactNumber,teamLegacy);
                            reference.child(sportName).setValue(newSport);
                            Intent intent = new Intent(EditSportActivity.this, SportsActivity.class);
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

    private boolean isConnected(EditSportActivity activity) {
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