package com.example.admindashboard;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.clubs.ClubActivity;
import com.example.admindashboard.loginAndSignUp.LoginActivity;
import com.example.admindashboard.profile.AdminProfileActivity;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.sports.SportsActivity;
import com.example.admindashboard.toppers.ToppersActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private CardView studentCard ;
    private CardView teacherCard ;
    private CardView sportsCard ;
    private CardView clubsCard ;
    private CardView toppersCard ;
    private CardView profileCard;
    private Intent i ;
    private SharedPreferences sharedPreferences;
    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spProfile = new ProfileSharedPreferences(MainActivity.this);

        if (!spProfile.checkLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        studentCard = findViewById(R.id.editStudent);

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, AddDeleteUserActivity.class);
                i.putExtra("pageHeading","Add Student");
                i.putExtra("userType","student");
                i.putExtra("recyclerViewLabel","Students");
                startActivity(i);
            }
        });

        teacherCard = findViewById(R.id.addDeleteStudent);
        teacherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, AddDeleteUserActivity.class);
                i.putExtra("pageHeading","Add Faculty");
                i.putExtra("userType","faculty");
                i.putExtra("recyclerViewLabel","Faculties");
                startActivity(i);
            }
        });

        sportsCard = (CardView) findViewById(R.id.sportsCardView);
        sportsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, SportsActivity.class);
                startActivity(i);
            }
        });

        clubsCard = findViewById(R.id.clubsCardView);
        clubsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, ClubActivity.class);
                startActivity(i);
            }
        });

        toppersCard = findViewById(R.id.toppersCardView);
        toppersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, ToppersActivity.class);
                startActivity(i);
            }
        });

        profileCard = findViewById(R.id.cardProfile);
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                i = new Intent(MainActivity.this, AdminProfileActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout){
            spProfile.logoutUserFromSession();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private boolean isConnected(MainActivity activity) {
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