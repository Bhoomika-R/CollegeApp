package com.example.mycollegeapp.ui.sports;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycollegeapp.R;

public class SportsDetailActivity extends AppCompatActivity {
    private TextView tvSportName, tvSportType, teamLegacy, captain, captainPhone, captainLabel,captainPhoneLabel;

    private String sportName,sportGender, sportType;
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

    }
}