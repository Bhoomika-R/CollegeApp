package com.example.admindashboard.sports;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admindashboard.R;
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.faculties.EditFacultyActivity;
import com.example.admindashboard.profile.UserProfileActivity;
import com.example.admindashboard.students.EditStudentActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SportsDetailActivity extends AppCompatActivity {
    private TextView tvSportName, tvSportType, teamLegacy, captain, captainPhone, captainLabel,captainPhoneLabel;

    private String sportName,sportGender, sportType;

    private FloatingActionButton fabDelete,fabEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_detail);

        tvSportName = findViewById(R.id.sportName);
        tvSportType = findViewById(R.id.sportType);
        teamLegacy = findViewById(R.id.teamLegacy);
        captain = findViewById(R.id.captain);
        captainPhone = findViewById(R.id.captainPhone);
        captainPhoneLabel=findViewById(R.id.captainPhoneLabel);
        captainLabel = findViewById(R.id.captainLabel);
        fabDelete=findViewById(R.id.fabDelete);
        fabEdit=findViewById(R.id.fabEdit);

        sportName=getIntent().getStringExtra("sportName").toString().trim();
        sportGender=getIntent().getStringExtra("sportGender").toString().trim();
        sportType=getIntent().getStringExtra("sportType").toString().trim();

        tvSportName.setText(sportName);
        tvSportType.setText(sportType);
        teamLegacy.setText(getIntent().getStringExtra("teamLegacy").toString().trim());
        captain.setText(getIntent().getStringExtra("captain").toString().trim());
        captainPhone.setText(getIntent().getStringExtra("captainPhone").toString().trim());
        if (getIntent().getStringExtra("sportType").toString().equals("Individual")) {
            captainPhoneLabel.setText("Sport Incharge Contact Number");
            captainLabel.setText("Sport Incharge");
        }

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SportsDetailActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("sports").child(sportGender).child(sportType).child(sportName);
                                reference.removeValue();
                                Intent getBack=new Intent(SportsDetailActivity.this, SportsActivity.class);
                                getBack.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(getBack);
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEdit=new Intent(SportsDetailActivity.this, EditSportActivity.class);
                goToEdit.putExtra("sportName",sportName);
                goToEdit.putExtra("sportGender",sportGender);
                goToEdit.putExtra("sportType", sportType);
                startActivity(goToEdit);
            }
        });

    }
}