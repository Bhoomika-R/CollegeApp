package com.example.mycollegeapp.ui.teachers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.SpaceItemDecorator;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TeachersFragment extends Fragment {

    private RecyclerView teacherRecyclerView;
    private TeachersAdapter teachersAdapterAdapter;
    private Set<ProfileClassFaculty> mUsers;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;

    public TeachersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teachers, container, false);

        spProfile = new ProfileSharedPreferences(getContext());
        profileData=spProfile.getUserProfile();

        teacherRecyclerView = view.findViewById(R.id.users_recycler_view);
        teacherRecyclerView.setHasFixedSize(true);
        teacherRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new HashSet<>();

        readUsers();



        LinearLayoutManager layoutManager1 =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        teacherRecyclerView.setLayoutManager(layoutManager1);
        teacherRecyclerView.setHasFixedSize(true);

        SpaceItemDecorator itemDecorator = new SpaceItemDecorator(10);

        teacherRecyclerView.addItemDecoration(itemDecorator);

        return view;
    }

    public void readUsers() {
        DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("faculty");
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProfileClassFaculty user = snapshot.getValue(ProfileClassFaculty.class);
                        mUsers.add(user);
                    }

                teachersAdapterAdapter = new TeachersAdapter(getContext(),profileData, mUsers);
                teacherRecyclerView.setAdapter(teachersAdapterAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}