package com.example.admindashboard.clubs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admindashboard.R;
import com.example.admindashboard.sports.EditSportActivity;
import com.example.admindashboard.sports.SportsActivity;
import com.example.admindashboard.sports.SportsDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClubsDetailActivity extends AppCompatActivity {
    private TextView tvClubName, tvClubGenre, tvClubLeadName, tvClubLeadContactNumber, tvClubDescription;

    private String clubName, clubType;

    private FloatingActionButton fabDelete,fabEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_detail);

        tvClubName = findViewById(R.id.clubName);
        tvClubGenre = findViewById(R.id.clubGenre);
        tvClubLeadName = findViewById(R.id.clubLeadName);
        tvClubLeadContactNumber = findViewById(R.id.clubLeadContactNumber);
        tvClubDescription = findViewById(R.id.clubDescription);
        fabDelete=findViewById(R.id.fabDelete);
        fabEdit=findViewById(R.id.fabEdit);

        clubName=getIntent().getStringExtra("clubName").toString().trim();
        clubType=getIntent().getStringExtra("clubType").toString().trim();

        tvClubName.setText(clubName);
        tvClubGenre.setText(getIntent().getStringExtra("clubGenre").toString().trim());
        tvClubLeadName.setText(getIntent().getStringExtra("clubLeadName").toString().trim());
        tvClubLeadContactNumber.setText(getIntent().getStringExtra("clubLeadContactNumber").toString().trim());
        tvClubDescription.setText(getIntent().getStringExtra("clubDescription").toString().trim());

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ClubsDetailActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("root").child("clubs").child(clubType).child(clubName);
                                reference.removeValue();
                                Intent getBack=new Intent(ClubsDetailActivity.this, ClubActivity.class);
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
                Intent goToEdit=new Intent(ClubsDetailActivity.this, EditClubActivity.class);
                goToEdit.putExtra("clubName",clubName);
                goToEdit.putExtra("clubType", clubType);
                startActivity(goToEdit);
            }
        });
    }
}