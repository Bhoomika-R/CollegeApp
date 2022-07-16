package com.example.mycollegeapp.ui.chatSection.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.Adapter.UserAdapter;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.chatSection.Model.ChatList;
import com.example.mycollegeapp.ui.profile.ProfileClass;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerViewStudents,recyclerViewFaculty;

    private UserAdapter userAdapter;
    private Set<ProfileClass> mUsersStudent, mUsersFaculty;
    private int unread;

    private DatabaseReference reference;

    private List<ChatList> usersList;

    private TextView unread1;
    private TextView unread2;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("chats","in oncreate");

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        unread1 = view.findViewById(R.id.text_unread);
        unread2 = view.findViewById(R.id.text_unread2);

        spProfile = new ProfileSharedPreferences(getContext());
        profileData = spProfile.getUserProfile();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("chat section").child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO)) && chat.getIsSeen() == 0) {
                        unread++;
                    }
                }


                if (unread == 0) {
                    unread1.setVisibility(View.GONE);
                    unread2.setVisibility(View.GONE);
                } else {
                    unread1.setVisibility(View.VISIBLE);
                    unread2.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerViewStudents = view.findViewById(R.id.chats_fragment_recycler_view_students);
        recyclerViewStudents.setHasFixedSize(true);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewFaculty = view.findViewById(R.id.chats_fragment_recycler_view_faculty);
        recyclerViewFaculty.setHasFixedSize(true);
        recyclerViewFaculty.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chat section").child("chatlist").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("chats","in ondatachange");
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    usersList.add(chatList);
                }
                Log.v("chats",String.valueOf(usersList.size()));
//                Toast.makeText(getContext(),String.valueOf(usersList.size()),Toast.LENGTH_SHORT).show();
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void chatList() {
        mUsersFaculty = new LinkedHashSet<>();
        mUsersStudent = new LinkedHashSet<>();
        DatabaseReference referenceStudent = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("student");
        DatabaseReference referenceFaculty = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child("faculty");
        referenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersStudent.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileClass user = snapshot.getValue(ProfileClass.class);
                    for (ChatList chatList : usersList) {
                        assert user != null;
                        if (user.getRegNo().equals(chatList.getId())) {
                            mUsersStudent.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),profileData, mUsersStudent, true);
                recyclerViewStudents.setAdapter(userAdapter);
                Log.v("hello student",String.valueOf(mUsersStudent.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Log.v("hello",String.valueOf(mUsersStudent.size()));
        referenceFaculty.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersFaculty.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileClass user = snapshot.getValue(ProfileClass.class);
                    for (ChatList chatList : usersList) {
                        assert user != null;
                        if (user.getRegNo().equals(chatList.getId())) {
                            mUsersFaculty.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),profileData, mUsersFaculty, true);
                recyclerViewFaculty.setAdapter(userAdapter);
                Log.v("hello faculty",String.valueOf(mUsersFaculty.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}