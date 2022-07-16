package com.example.mycollegeapp.ui.clubs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.SpaceItemDecorator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ClubsFragment extends Fragment {

    private RecyclerView techClubsRecyclerView;
    private RecyclerView nonTechClubsRecyclerView;

    private DatabaseReference referenceNonTechClub,referenceTechClub;

    public ClubsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clubs, container, false);

        techClubsRecyclerView =  view.findViewById(R.id.techClubsRecyclerViewNew);
        nonTechClubsRecyclerView =  view.findViewById(R.id.nonTechClubsRecyclerViewNew);

        ArrayList<Club> techClubs = new ArrayList<>();
        ArrayList<Club> nonTechClubs = new ArrayList<>();

        Context mContext = getContext();

        ClubsAdapter techClubsAdapter = new ClubsAdapter(mContext, techClubs);
        ClubsAdapter nonTechClubsAdapter = new ClubsAdapter(mContext, nonTechClubs);

        referenceNonTechClub= FirebaseDatabase.getInstance().getReference("root").child("clubs").child("Non Tech");
        referenceNonTechClub.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.v("hello","entered on child added");
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

        referenceTechClub= FirebaseDatabase.getInstance().getReference("root").child("clubs").child("Tech");
        referenceTechClub.addChildEventListener(new ChildEventListener() {
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

        LinearLayoutManager layoutManager1 =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        techClubsRecyclerView.setLayoutManager(layoutManager1);
        nonTechClubsRecyclerView.setLayoutManager(layoutManager2);

        techClubsRecyclerView.setHasFixedSize(true);
        nonTechClubsRecyclerView.setHasFixedSize(true);

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);

        techClubsRecyclerView.addItemDecoration(itemDecorator);
        nonTechClubsRecyclerView.addItemDecoration(itemDecorator);

        nonTechClubsRecyclerView.setAdapter(nonTechClubsAdapter);
        techClubsRecyclerView.setAdapter(techClubsAdapter);

        return view;
    }


}