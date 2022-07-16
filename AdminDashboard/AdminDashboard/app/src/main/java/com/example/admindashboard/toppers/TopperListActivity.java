package com.example.admindashboard.toppers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admindashboard.MainActivity;
import com.example.admindashboard.R;
import com.example.admindashboard.addDeleteEdit.AddDeleteUserActivity;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.students.AddStudentActivity;
import com.example.admindashboard.utils.SpaceItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TopperListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvRecyclerViewLabel;
    private FloatingActionButton fabAddButton;

    private ProfileSharedPreferences profile;
    private HashMap<String,String> profileData;
    private String branch;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topper_list);

        recyclerView = findViewById(R.id.toppers_recycler_view);
        tvRecyclerViewLabel = findViewById(R.id.tvRecyclerViewLabel);
        fabAddButton = findViewById(R.id.fabAddButton);

        profile=new ProfileSharedPreferences(TopperListActivity.this);
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
        ToppersAdapter toppersAdapter = new ToppersAdapter(this,list,heading);

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

        fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected(TopperListActivity.this)){
                    Toast.makeText(TopperListActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(TopperListActivity.this,AddTopperActivity.class);
                intent.putExtra("Year",year);
                intent.putExtra("TVHeading",heading);
                startActivity(intent);
            }
        });

    }

    private boolean isConnected(TopperListActivity activity) {
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
        Intent intent = new Intent(TopperListActivity.this, ToppersActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}