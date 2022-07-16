package com.example.admindashboard.clubs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.admindashboard.MainActivity;
import com.example.admindashboard.R;
import com.example.admindashboard.sports.AddSportActivity;
import com.example.admindashboard.sports.Sport;
import com.example.admindashboard.sports.SportsActivity;
import com.example.admindashboard.sports.SportsAdapter;
import com.example.admindashboard.utils.SpaceItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClubActivity extends AppCompatActivity {
    private RecyclerView techRecyclerView;
    private RecyclerView nonTechRecyclerView;

    private FloatingActionButton fabAddClub;

    private DatabaseReference referenceTech,referenceNonTech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        techRecyclerView =  findViewById(R.id.techClubsRecyclerViewNew);
        nonTechRecyclerView = findViewById(R.id.nonTechClubsRecyclerViewNew);
        fabAddClub = findViewById(R.id.fabAddClub);

        fabAddClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(ClubActivity.this))
                    return;
                Intent intent=new Intent(ClubActivity.this, AddClubActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Club> techClubs = new ArrayList<Club>();
        ArrayList<Club> nonTechClubs = new ArrayList<Club>();

        ClubsAdapter techClubsAdapter = new ClubsAdapter(this, techClubs);
        ClubsAdapter nonTechClubsAdapter = new ClubsAdapter(this, nonTechClubs);

        referenceTech= FirebaseDatabase.getInstance().getReference("root").child("clubs").child("Tech");
        referenceTech.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.v("hello","entered on child added");
                techClubs.add(new Club(snapshot.child("clubName").getValue(String.class),snapshot.child("clubType").getValue(String.class), snapshot.child("clubGenre").getValue(String.class),snapshot.child("clubLeadName").getValue(String.class),snapshot.child("clubLeadContactNumber").getValue(String.class),snapshot.child("clubDescription").getValue(String.class)));
                techClubsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                techClubsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceNonTech= FirebaseDatabase.getInstance().getReference("root").child("clubs").child("Non Tech");
        referenceNonTech.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                nonTechClubs.add(new Club(snapshot.child("clubName").getValue(String.class),snapshot.child("clubType").getValue(String.class), snapshot.child("clubGenre").getValue(String.class),snapshot.child("clubLeadName").getValue(String.class),snapshot.child("clubLeadContactNumber").getValue(String.class),snapshot.child("clubDescription").getValue(String.class)));
                nonTechClubsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                nonTechClubsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManager1 =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        techRecyclerView.setLayoutManager(layoutManager1);
        nonTechRecyclerView.setLayoutManager(layoutManager2);

        techRecyclerView.setHasFixedSize(true);
        nonTechRecyclerView.setHasFixedSize(true);

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);

        techRecyclerView.addItemDecoration(itemDecorator);
        nonTechRecyclerView.addItemDecoration(itemDecorator);

        techRecyclerView.setAdapter(techClubsAdapter);
        nonTechRecyclerView.setAdapter(nonTechClubsAdapter);

    }

    private boolean isConnected(ClubActivity activity) {
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClubActivity.this, MainActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}