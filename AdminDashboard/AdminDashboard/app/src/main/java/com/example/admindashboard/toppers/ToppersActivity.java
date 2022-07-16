package com.example.admindashboard.toppers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.admindashboard.MainActivity;
import com.example.admindashboard.R;
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;

public class ToppersActivity extends AppCompatActivity {
    private CardView cv1Year,cv2Year,cv3Year,cv4Year ;
    private Intent i ;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toppers);

        cv1Year = findViewById(R.id.firstyear);
        i = new Intent(this, TopperListActivity.class);
        cv1Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 1");
                i.putExtra("TVHeading","Year 1 Toppers");
                startActivity(i);
            }
        });

        cv2Year = findViewById(R.id.secondyear);
        i = new Intent(this, TopperListActivity.class);
        cv2Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 2");
                i.putExtra("TVHeading","Year 2 Toppers");
                startActivity(i);
            }
        });

        cv3Year = findViewById(R.id.thirdyear);
        i = new Intent(this, TopperListActivity.class);
        cv3Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 3");
                i.putExtra("TVHeading","Year 3 Toppers");
                startActivity(i);
            }
        });

        cv4Year = findViewById(R.id.fourthyear);
        i = new Intent(this, TopperListActivity.class);
        cv4Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 4");
                i.putExtra("TVHeading","Year 4 Toppers");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ToppersActivity.this, MainActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}