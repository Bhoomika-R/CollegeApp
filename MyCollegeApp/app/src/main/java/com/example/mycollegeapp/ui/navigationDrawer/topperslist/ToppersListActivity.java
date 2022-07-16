package com.example.mycollegeapp.ui.navigationDrawer.topperslist;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;

public class ToppersListActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private CardView cv1Year,cv2Year,cv3Year,cv4Year ;
    private Intent i ;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toppers_list);

        ll = (LinearLayout) findViewById(R.id.ll);
        cv1Year = (CardView) findViewById(R.id.firstyear);
        i = new Intent(this, DisplayToppersActivity.class);
        cv1Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 1");
                i.putExtra("TVHeading","Year 1 Toppers");
                startActivity(i);
            }
        });

        cv2Year = (CardView) findViewById(R.id.secondyear);
        i = new Intent(this, DisplayToppersActivity.class);
        cv2Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 2");
                i.putExtra("TVHeading","Year 2 Toppers");
                startActivity(i);
            }
        });

        cv3Year = (CardView) findViewById(R.id.thirdyear);
        i = new Intent(this, DisplayToppersActivity.class);
        cv3Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("Year","Year 3");
                i.putExtra("TVHeading","Year 3 Toppers");
                startActivity(i);
            }
        });

        cv4Year = (CardView) findViewById(R.id.fourthyear);
        i = new Intent(this, DisplayToppersActivity.class);
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
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}