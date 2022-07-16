package com.example.admindashboard.sports;

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
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.faculties.AddFacultyActivity;
import com.example.admindashboard.students.AddStudentActivity;
import com.example.admindashboard.utils.SpaceItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SportsActivity extends AppCompatActivity {
    private RecyclerView menRecyclerView;
    private RecyclerView womenRecyclerView;

    private FloatingActionButton fabAddSport;

    private DatabaseReference referenceMenIndividual,referenceMenTeam,referenceWomenIndividual,referenceWomenTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        menRecyclerView =  findViewById(R.id.menSportsRecyclerViewNew);
        womenRecyclerView = findViewById(R.id.womenSportsRecyclerViewNew);
        fabAddSport = findViewById(R.id.fabAddSport);

        fabAddSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(SportsActivity.this))
                    return;
                Intent intent=new Intent(SportsActivity.this, AddSportActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Sport> menSports = new ArrayList<Sport>();
        ArrayList<Sport> womenSports = new ArrayList<Sport>();

        SportsAdapter menSportsAdapter = new SportsAdapter(this, menSports);
        SportsAdapter womenSportsAdapter = new SportsAdapter(this, womenSports);

        referenceMenIndividual= FirebaseDatabase.getInstance().getReference("root").child("sports").child("Men").child("Individual");
        referenceMenIndividual.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.v("hello","entered on child added");
                menSports.add(new Sport(snapshot.child("sportName").getValue(String.class),snapshot.child("sportGender").getValue(String.class), snapshot.child("sportType").getValue(String.class),snapshot.child("captain").getValue(String.class),snapshot.child("captainContactNumber").getValue(String.class),snapshot.child("teamLegacy").getValue(String.class)));
                menSportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                menSportsAdapter.notifyDataSetChanged();
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

        referenceMenTeam= FirebaseDatabase.getInstance().getReference("root").child("sports").child("Men").child("Team");
        referenceMenTeam.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                menSports.add(new Sport(snapshot.child("sportName").getValue(String.class),snapshot.child("sportGender").getValue(String.class), snapshot.child("sportType").getValue(String.class),snapshot.child("captain").getValue(String.class),snapshot.child("captainContactNumber").getValue(String.class),snapshot.child("teamLegacy").getValue(String.class)));
                menSportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                menSportsAdapter.notifyDataSetChanged();
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

        referenceWomenIndividual= FirebaseDatabase.getInstance().getReference("root").child("sports").child("Women").child("Individual");
        referenceWomenIndividual.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                womenSports.add(new Sport(snapshot.child("sportName").getValue(String.class),snapshot.child("sportGender").getValue(String.class), snapshot.child("sportType").getValue(String.class),snapshot.child("captain").getValue(String.class),snapshot.child("captainContactNumber").getValue(String.class),snapshot.child("teamLegacy").getValue(String.class)));
                womenSportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                womenSportsAdapter.notifyDataSetChanged();
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


        referenceWomenTeam= FirebaseDatabase.getInstance().getReference("root").child("sports").child("Women").child("Team");
        referenceWomenTeam.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                womenSports.add(new Sport(snapshot.child("sportName").getValue(String.class),snapshot.child("sportGender").getValue(String.class), snapshot.child("sportType").getValue(String.class),snapshot.child("captain").getValue(String.class),snapshot.child("captainContactNumber").getValue(String.class),snapshot.child("teamLegacy").getValue(String.class)));
                womenSportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                womenSportsAdapter.notifyDataSetChanged();
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

        menRecyclerView.setLayoutManager(layoutManager1);
        womenRecyclerView.setLayoutManager(layoutManager2);

        menRecyclerView.setHasFixedSize(true);
        womenRecyclerView.setHasFixedSize(true);

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);

        menRecyclerView.addItemDecoration(itemDecorator);
        womenRecyclerView.addItemDecoration(itemDecorator);

        menRecyclerView.setAdapter(menSportsAdapter);
        womenRecyclerView.setAdapter(womenSportsAdapter);
    }

    private boolean isConnected(SportsActivity activity) {
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
        Intent intent = new Intent(SportsActivity.this, MainActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}