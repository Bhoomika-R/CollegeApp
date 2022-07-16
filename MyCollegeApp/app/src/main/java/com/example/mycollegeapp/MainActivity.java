package com.example.mycollegeapp;

import static com.example.mycollegeapp.ui.profile.ProfileSharedPreferences.KEY_IMAGE_URL;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.example.mycollegeapp.databinding.ActivityMainBinding;
import com.example.mycollegeapp.ui.chatSection.ChatMainActivity;
import com.example.mycollegeapp.ui.chatSection.Model.Chat;
import com.example.mycollegeapp.ui.clubs.ClubsFragment;
import com.example.mycollegeapp.ui.home.HomeFragment;
import com.example.mycollegeapp.ui.loginAndSignup.LoginActivity;
import com.example.mycollegeapp.ui.navigationDrawer.aboutUs.AboutUsActivity;
import com.example.mycollegeapp.ui.navigationDrawer.collegeMessages.CollegeMessagesActivity;
import com.example.mycollegeapp.ui.navigationDrawer.topperslist.ToppersListActivity;
import com.example.mycollegeapp.ui.networks.NetworkChangeListener;
import com.example.mycollegeapp.ui.profile.ProfileFragment;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.example.mycollegeapp.ui.sports.SportsFragment;
import com.example.mycollegeapp.ui.teachers.TeachersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();

    private int unread;

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
//    private int height,width = 0;
//    private DisplayMetrics displayMetrics;
    private SharedPreferences sharedPreferences;
    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;
    private TextView textViewName, textViewDepartment;

    private Menu sideNavMenu;

    private CircleImageView imageNavBar;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        displayMetrics = new DisplayMetrics();
//        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//
//        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
//        height = Math.round(displayMetrics.heightPixels / displayMetrics.density);
//
//        width = Math.round(displayMetrics.widthPixels / displayMetrics.density);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        spProfile = new ProfileSharedPreferences(MainActivity.this);

        if (!spProfile.checkLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        sharedPreferences= getSharedPreferences("userLoginSession",MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        textViewName = binding.navView.getHeaderView(0).findViewById(R.id.textViewName);
        imageNavBar = binding.navView.getHeaderView(0).findViewById(R.id.navBarImageView);
        textViewDepartment = binding.navView.getHeaderView(0).findViewById(R.id.textViewDepartment);

        if(spProfile.checkLogin()) {
            profileData = spProfile.getUserProfile();
            Log.v("Testing",String.valueOf(spProfile.checkLogin()));
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase(Locale.ROOT)).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String regNo = snapshot.child("regNo").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);
                        String branch = snapshot.child("branch").getValue(String.class);
                        String sem = snapshot.child("sem").getValue(String.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        String designation = snapshot.child("designation").getValue(String.class);
                        String qualifications = snapshot.child("qualifications").getValue(String.class);
                        ProfileSharedPreferences profile = new ProfileSharedPreferences(MainActivity.this);
                        if(gender!=null){
                            if(role.equals("Student"))
                              profile.createLoginSessionForStudent(name, email, phone, gender, role, regNo, dob, branch, sem, getNameAbbr(name), imageUrl);
                            else
                              profile.createLoginSessionForFaculty(name, email, phone, gender, role, regNo, dob, branch, getNameAbbr(name), imageUrl,qualifications,designation);
                            profileData = profile.getUserProfile();
                            setupNavBar();}
                    } else {
                        spProfile.logoutUserFromSession();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        setSupportActionBar(binding.appBarMain.toolbar123);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarMain.toolbar123, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.outline_menu_black_36);
        navigationView.setNavigationItemSelectedListener(this);
        sideNavMenu=navigationView.getMenu();

        setupBottomNavigation();
        setupNavBar();
        if(spProfile.checkLogin()){
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

//                    if (unread == 0) {
//                        MenuItem chatSection=sideNavMenu.findItem(R.id.nav_chat_section);
//                        chatSection.setTitle("Chat Section");
//                    } else {
//                       MenuItem chatSection=sideNavMenu.findItem(R.id.nav_chat_section);
//                       chatSection.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_picture_as_pdf_24, 0, 0, 0);
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private String getNameAbbr(String name) {
        StringBuilder initials = new StringBuilder();
        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }
        String temp = initials.toString().toUpperCase(Locale.ROOT).trim();
        StringBuilder imageText = new StringBuilder();
        if (temp.length() == 1)
            imageText.append(temp.charAt(0));
        else {
            imageText.append(temp.charAt(0));
            imageText.append(temp.charAt(1));
        }

        return imageText.toString();
    }

    private void setupNavBar() {
        if(profileData!=null) {
            textViewName.setText(profileData.get(ProfileSharedPreferences.KEY_NAME));
            String branch = profileData.get(ProfileSharedPreferences.KEY_BRANCH);
            switch (branch) {
                case "CSE":
                    textViewDepartment.setText("Computer Science and Engineering");
                    break;
                case "ISE":
                    textViewDepartment.setText("Information Science and Engineering");
                    break;
                case "ECE":
                    textViewDepartment.setText("Electronics and Communication Engineering");
                    break;
                case "ME":
                    textViewDepartment.setText("Mechanical Engineering");
                    break;
                case "BT":
                    textViewDepartment.setText("Bio-Technology");
                    break;
                case "MT":
                    textViewDepartment.setText("Mechatronics");
                    break;
                case "EEE":
                    textViewDepartment.setText("Electrical and Electronics Engineering");
                    break;
                case "AU":
                    textViewDepartment.setText("Automobile Engineering");
                    break;
            }
            if (spProfile.checkLogin()) {
                Log.v("Testing12", String.valueOf(spProfile.checkLogin()));
                if (!profileData.get(KEY_IMAGE_URL).equals("default")) {
                    Glide.with(getApplicationContext()).load(profileData.get(KEY_IMAGE_URL)).into(imageNavBar);
                }
            }
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        temp = new HomeFragment();
                        break;
                    case R.id.teacher:
                        temp = new TeachersFragment();
                        break;
                    case R.id.club:
                        temp = new ClubsFragment();
                        break;
                    case R.id.sports:
                        temp = new SportsFragment();
                        break;
                    case R.id.profileImageView:
                        temp = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer, temp).commit();
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (bottomNavigationView.getSelectedItemId() == R.id.home) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit")
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pid = android.os.Process.myPid();
                                android.os.Process.killProcess(pid);
//                                MainActivity.super.onBackPressed();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else {
                bottomNavigationView.setSelectedItemId(R.id.home);
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_logout:
                spProfile.logoutUserFromSession();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_toppers_list:
                startActivity(new Intent(MainActivity.this, ToppersListActivity.class));
                break;

            case R.id.nav_college_messages:
                startActivity(new Intent(MainActivity.this, CollegeMessagesActivity.class));
                break;

            case R.id.nav_about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;

            case R.id.nav_chat_section:
                startActivity(new Intent(MainActivity.this, ChatMainActivity.class));
                break;

        }
        return false;
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(profileData.get(ProfileSharedPreferences.KEY_ROLE).toLowerCase()).child(profileData.get(ProfileSharedPreferences.KEY_REGNO));
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&spProfile.checkLogin()){
                  HashMap<String, Object> hashMap = new HashMap<>();
                  hashMap.put("status", status);
                  reference.updateChildren(hashMap);
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (spProfile.checkLogin()) {
            if (key.equals(KEY_IMAGE_URL)) {
//            Log.v("hello profile","entered changing");
                Glide.with(this).load(sharedPreferences.getString(KEY_IMAGE_URL, null)).into(imageNavBar);
            }
        }
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