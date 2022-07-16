package com.example.mycollegeapp.ui.chatSection.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.Adapter.UserAdapter;
import com.example.mycollegeapp.ui.profile.ProfileClass;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerViewStudent,recyclerViewFaculty;
    private UserAdapter userAdapterStudent,userAdapterFaculty;
    private Set<ProfileClass> mUsersFaculty,mUsersStudent;

    private EditText search_users;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        spProfile = new ProfileSharedPreferences(getContext());
        profileData=spProfile.getUserProfile();

        recyclerViewStudent = view.findViewById(R.id.users_fragment_recycler_view_student);
        recyclerViewStudent.setHasFixedSize(true);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewFaculty = view.findViewById(R.id.users_fragment_recycler_view_faculty);
        recyclerViewFaculty.setHasFixedSize(true);
        recyclerViewFaculty.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsersFaculty = new HashSet<>();
        mUsersStudent = new HashSet<>();

        readUsers();

        search_users = view.findViewById(R.id.search_user_fragment_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    private void searchUsers(String string) {
        Query queryFaculty = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("faculty").orderByChild("search")
                .startAt(string)
                .endAt(string + "\uf8ff");
        Query queryStudent = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("student").orderByChild("search")
                .startAt(string)
                .endAt(string + "\uf8ff");

        queryFaculty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersFaculty.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileClass user = snapshot.getValue(ProfileClass.class);

                    if (!user.getRegNo().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
                        mUsersFaculty.add(user);
                    }
                }
                userAdapterFaculty = new UserAdapter(getContext(),profileData, mUsersFaculty, false);
                recyclerViewFaculty.setAdapter(userAdapterFaculty);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        queryStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersStudent.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileClass user = snapshot.getValue(ProfileClass.class);

                    if (!user.getRegNo().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
                        mUsersStudent.add(user);
                    }
                }
                userAdapterStudent = new UserAdapter(getContext(),profileData, mUsersStudent, false);
                recyclerViewStudent.setAdapter(userAdapterStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void readUsers() {
        DatabaseReference referenceFaculty = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("faculty");
        DatabaseReference referenceStudent = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("student");

        referenceFaculty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (search_users.getText().toString().equals("")) {
                    mUsersFaculty.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProfileClass user = snapshot.getValue(ProfileClass.class);

                        if (!user.getRegNo().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
                            mUsersFaculty.add(user);
                        }
                    }

                    userAdapterFaculty = new UserAdapter(getContext(),profileData, mUsersFaculty, false);
                    recyclerViewFaculty.setAdapter(userAdapterFaculty);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (search_users.getText().toString().equals("")) {
                    mUsersStudent.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProfileClass user = snapshot.getValue(ProfileClass.class);

                        if (!user.getRegNo().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
                            mUsersStudent.add(user);
                        }
                    }

                    userAdapterStudent = new UserAdapter(getContext(),profileData, mUsersStudent, false);
                    recyclerViewStudent.setAdapter(userAdapterStudent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}