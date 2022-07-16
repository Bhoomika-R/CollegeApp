package com.example.admindashboard.loginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.admindashboard.MainActivity;
import com.example.admindashboard.R;
import com.example.admindashboard.profile.ProfileSharedPreferences;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private TextView forgotPassword, signUp;
    private TextInputLayout edRegNo, edPassword;
    private ProgressBar progressBar;
    private String name, role, gender, dob, branch, phone, email, sem,imageUrl;
    private Spinner branchSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBar);
        edRegNo = findViewById(R.id.edRegNo);
        edPassword = findViewById(R.id.edPassword);
        forgotPassword = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvRegisterHere);
        branchSpinner=findViewById(R.id.branch_spinner);
        setupSpinner();
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                Intent i = new Intent(LoginActivity.this, SignUpStep1Activity.class);
                startActivity(i);
            }
        });
    }

    private void setupSpinner(){
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.branch_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapter2);
    }

    public void onClickSignIn(View view) {

        if (!isConnected(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;

        }

        if (!validateFields()) {
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        String regNo = edRegNo.getEditText().getText().toString().trim();
        String password = edPassword.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("admins").child(branchSpinner.getSelectedItem().toString()).orderByChild("regNo").equalTo(regNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    edRegNo.setError(null);
                    edRegNo.setErrorEnabled(false);

                    String systemPassword = snapshot.child(regNo).child("password").getValue(String.class);
                    if (systemPassword.equals(password)) {
                        edPassword.setError(null);
                        edPassword.setErrorEnabled(false);

                        name = snapshot.child(regNo).child("name").getValue(String.class);
                        email = snapshot.child(regNo).child("email").getValue(String.class);
                        dob = snapshot.child(regNo).child("dob").getValue(String.class);
                        gender = snapshot.child(regNo).child("gender").getValue(String.class);
                        phone = snapshot.child(regNo).child("phone").getValue(String.class);
                        role = snapshot.child(regNo).child("role").getValue(String.class);
                        branch = snapshot.child(regNo).child("branch").getValue(String.class);
//                        sem = snapshot.child(regNo).child("sem").getValue(String.class);
                        imageUrl = snapshot.child(regNo).child("imageUrl").getValue(String.class);

                        ProfileSharedPreferences profile = new ProfileSharedPreferences(LoginActivity.this);
                        profile.createLoginSession(name, email, phone, gender, regNo, dob, branch,role, getNameAbbr(name),imageUrl);

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager manager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mob = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mob != null && mob.isConnected())) {
            return true;
        } else {
            return false;
        }
    }


    private boolean validateFields() {
        String regNo = edRegNo.getEditText().getText().toString().trim();
        String password = edPassword.getEditText().getText().toString().trim();
        if (regNo.isEmpty()) {
            edRegNo.setError("Field cannot be empty");
            edRegNo.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            edPassword.setError("Field cannot be empty");
            edPassword.requestFocus();
            return false;
        } else {
            edPassword.setError(null);
            edRegNo.setError(null);
            edPassword.setErrorEnabled(false);
            edRegNo.setErrorEnabled(false);
            return true;
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

}