package com.example.mycollegeapp.ui.sports;

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

public class SportsFragment extends Fragment {
    private RecyclerView menRecyclerView;
    private RecyclerView womenRecyclerView;

    private DatabaseReference referenceMenIndividual,referenceMenTeam,referenceWomenIndividual,referenceWomenTeam;

    public SportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sports, container, false);

        menRecyclerView =  view.findViewById(R.id.menSportsRecyclerViewNew);
        womenRecyclerView = view.findViewById(R.id.womenSportsRecyclerViewNew);


        ArrayList<Sport> menSports = new ArrayList<Sport>();
        ArrayList<Sport> womenSports = new ArrayList<Sport>();

        SportsAdapter menSportsAdapter = new SportsAdapter(getContext(), menSports);
        SportsAdapter womenSportsAdapter = new SportsAdapter(getContext(), womenSports);

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
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        menRecyclerView.setLayoutManager(layoutManager1);
        womenRecyclerView.setLayoutManager(layoutManager2);

        menRecyclerView.setHasFixedSize(true);
        womenRecyclerView.setHasFixedSize(true);

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);

        menRecyclerView.addItemDecoration(itemDecorator);
        womenRecyclerView.addItemDecoration(itemDecorator);

        menRecyclerView.setAdapter(menSportsAdapter);
        womenRecyclerView.setAdapter(womenSportsAdapter);

        return view;
    }
}