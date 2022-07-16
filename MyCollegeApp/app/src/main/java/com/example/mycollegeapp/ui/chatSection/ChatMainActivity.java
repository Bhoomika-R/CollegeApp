package com.example.mycollegeapp.ui.chatSection;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.MainActivity;
import com.example.mycollegeapp.R;
import com.example.mycollegeapp.ui.chatSection.Fragments.ChatsFragment;
import com.example.mycollegeapp.ui.chatSection.Fragments.UsersFragment;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;
import com.example.mycollegeapp.ui.profile.ProfileClass;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMainActivity extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private CircleImageView profile_image;
    private TextView username;

    private DatabaseReference reference;

    private ProfileSharedPreferences spProfile;
    private HashMap<String,String> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        Toolbar toolbar = findViewById(R.id.toolbarMainActivity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spProfile = new ProfileSharedPreferences(getApplicationContext());
        profileData=spProfile.getUserProfile();

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernameMainActivity);

        username.setText(profileData.get(ProfileSharedPreferences.KEY_NAME));

        reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase()).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ProfileClass user = snapshot.getValue(ProfileClass.class);
                    if (user.getImageUrl().contains("default")) {
                        profile_image.setImageResource(R.drawable.ic_baseline_person_white_24);
                    } else {
                        Glide.with(getBaseContext()).load(user.getImageUrl()).into(profile_image);
                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);


        reference = FirebaseDatabase.getInstance().getReference("chat section").child("chats").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO)) && chat.getIsSeen() == 0) {
                        unread++;
                    }
                }

                if (unread == 0) {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "(" + unread + ") Chats");
                }


                viewPagerAdapter.addFragment(new UsersFragment(), "Users");
//                viewPagerAdapter.addFragment(new ProfileFragmentChat(), "Profile");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ChatMainActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
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