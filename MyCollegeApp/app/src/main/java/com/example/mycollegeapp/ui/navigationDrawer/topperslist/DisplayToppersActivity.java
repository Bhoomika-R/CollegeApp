package com.example.mycollegeapp.ui.navigationDrawer.topperslist;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.SpaceItemDecorator;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DisplayToppersActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private RecyclerView recyclerView;
    private TextView tvRecyclerViewLabel;

    private ProfileSharedPreferences profile;
    private HashMap<String,String> profileData;
    private String branch;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.toppers_recycler_view);
        tvRecyclerViewLabel = findViewById(R.id.tvRecyclerViewLabel);

        profile=new ProfileSharedPreferences(DisplayToppersActivity.this);
        profileData=profile.getUserProfile();
        branch=profileData.get(ProfileSharedPreferences.KEY_BRANCH);

        String heading=getIntent().getStringExtra("TVHeading");
        tvRecyclerViewLabel.setText(heading);

        String year=getIntent().getStringExtra("Year");
        switch (year) {
            case "Year 1":
                reference = FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child("Year 1");
                break;
            case "Year 2":
                reference = FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child("Year 2");
                break;
            case "Year 3":
                reference = FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child("Year 3");
                break;
            case "Year 4":
                reference = FirebaseDatabase.getInstance().getReference("toppers list").child(branch).child("Year 4");
                break;
        }

        ArrayList<Topper> list=new ArrayList<>();
        ToppersAdapter toppersAdapter = new ToppersAdapter(this,list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    list.add(new Topper(dataSnapshot.child("name").getValue(String.class),dataSnapshot.child("branch").getValue(String.class),dataSnapshot.child("year").getValue(String.class),dataSnapshot.child("regNo").getValue(String.class),dataSnapshot.child("cgpa").getValue(String.class)));
                    Collections.sort(list, (o1, o2) -> Float.parseFloat(o1.getCgpa())>Float.parseFloat(o2.getCgpa())?-1:1);
                }
                toppersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);
        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setAdapter(toppersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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