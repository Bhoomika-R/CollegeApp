package com.example.admindashboard.addDeleteEdit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admindashboard.MainActivity;
import com.example.admindashboard.R;
import com.example.admindashboard.profile.ProfileClassUser;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.example.admindashboard.students.AddStudentActivity;
import com.example.admindashboard.faculties.AddFacultyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AddDeleteUserActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private Set<ProfileClassUser> mUsers;

    private ProfileSharedPreferences spProfile;
    private HashMap<String, String> profileData;

    private String pageHeading,userType,recyclerViewLabel;

    private EditText search_users;
    private FloatingActionButton fab;
    private TextView tvRecyclerViewLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delete_user);

        fab=findViewById(R.id.fabAddButton);
        tvRecyclerViewLabel=findViewById(R.id.tvRecyclerViewLabel);

        Intent previousActivityIntent=getIntent();
        pageHeading=previousActivityIntent.getStringExtra("pageHeading");
        userType=previousActivityIntent.getStringExtra("userType");
        recyclerViewLabel=previousActivityIntent.getStringExtra("recyclerViewLabel");

        tvRecyclerViewLabel.setText(recyclerViewLabel);

        spProfile = new ProfileSharedPreferences(this);
        profileData=spProfile.getUserProfile();

        recyclerViewUsers = findViewById(R.id.users_recycler_view);
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new HashSet<>();

        readUsers();

        search_users = findViewById(R.id.searchUserEdittext);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(AddDeleteUserActivity.this))
                    return;
                Intent intent;
                if(userType.equals("student"))
                    intent=new Intent(AddDeleteUserActivity.this, AddStudentActivity.class);
                else
                    intent=new Intent(AddDeleteUserActivity.this, AddFacultyActivity.class);
                intent.putExtra("pageHeading",pageHeading);
                intent.putExtra("userType",userType);
                startActivity(intent);
            }
        });
    }

    private void searchUsers(String string) {
        if(!isConnected(AddDeleteUserActivity.this))
            return;
        Query searchQuery = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userType).orderByChild("search")
                .startAt(string)
                .endAt(string + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProfileClassUser user = snapshot.getValue(ProfileClassUser.class);

                    if (!user.getRegNo().equals(profileData.get(ProfileSharedPreferences.KEY_REGNO))) {
                        mUsers.add(user);
                    }
                }
                userAdapter = new UserAdapter(AddDeleteUserActivity.this,profileData, mUsers,pageHeading,userType);
                recyclerViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readUsers() {
        if(!isConnected(AddDeleteUserActivity.this))
            return;
        DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("root").child("profiles").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH)).child(userType);
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProfileClassUser user = snapshot.getValue(ProfileClassUser.class);
                            mUsers.add(user);
                    }

                    userAdapter = new UserAdapter(AddDeleteUserActivity.this,profileData, mUsers,pageHeading,userType);
                    recyclerViewUsers.setAdapter(userAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isConnected(AddDeleteUserActivity activity) {
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
        Intent intent = new Intent(AddDeleteUserActivity.this, MainActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}