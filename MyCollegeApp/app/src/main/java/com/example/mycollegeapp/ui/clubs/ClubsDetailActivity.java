package com.example.mycollegeapp.ui.clubs;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycollegeapp.R;

public class ClubsDetailActivity extends AppCompatActivity {
    private TextView tvClubName, tvClubGenre, tvClubLeadName, tvClubLeadContactNumber, tvClubDescription;

    private String clubName, clubType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_detail);

        tvClubName = findViewById(R.id.clubName);
        tvClubGenre = findViewById(R.id.clubGenre);
        tvClubLeadName = findViewById(R.id.clubLeadName);
        tvClubLeadContactNumber = findViewById(R.id.clubLeadContactNumber);
        tvClubDescription = findViewById(R.id.clubDescription);


        clubName=getIntent().getStringExtra("clubName").toString().trim();
        clubType=getIntent().getStringExtra("clubType").toString().trim();

        tvClubName.setText(clubName);
        tvClubGenre.setText(getIntent().getStringExtra("clubGenre").toString().trim());
        tvClubLeadName.setText(getIntent().getStringExtra("clubLeadName").toString().trim());
        tvClubLeadContactNumber.setText(getIntent().getStringExtra("clubLeadContactNumber").toString().trim());
        tvClubDescription.setText(getIntent().getStringExtra("clubDescription").toString().trim());
    }
}