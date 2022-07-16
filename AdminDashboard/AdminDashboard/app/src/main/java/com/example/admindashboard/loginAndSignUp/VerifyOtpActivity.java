package com.example.admindashboard.loginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import com.example.admindashboard.R;
import com.example.admindashboard.profile.ProfileClassAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {
    private PinView pvOtp;
    private TextView tvPhone;
    private String codeBySystem;
    private FirebaseAuth mAuth;
    private String name, email, regNo, password, gender, dob, branch, phone, whatToDo,search,status,imageUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        tvPhone = findViewById(R.id.tvPhone);
        pvOtp = findViewById(R.id.pvOtp);
        progressBar=findViewById(R.id.progressBar);


        phone = getIntent().getStringExtra("Phone");
        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        regNo = getIntent().getStringExtra("RegNo");
        password = getIntent().getStringExtra("Password");
        gender = getIntent().getStringExtra("Gender");
        dob = getIntent().getStringExtra("Dob");
        branch = getIntent().getStringExtra("Branch");
        whatToDo = getIntent().getStringExtra("WhatToDo");

        imageUrl="default";
        status="offline";
        if(name!=null)
            search=name.toLowerCase();

        tvPhone.setText(phone);


        sendOtpToUser(phone);
    }

    private void sendOtpToUser(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pvOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(VerifyOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
//            Toast.makeText(VerifyOtpActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onClickVerifyCode(View view) {
        if (!isConnected(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        String code = pvOtp.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
        else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No code is entered", Toast.LENGTH_SHORT).show();
            pvOtp.requestFocus();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (whatToDo != null) {
                                if (whatToDo.equals("ResetPassword")) {
                                    resetPassword();
                                }
                            }
                            else {
                                storeNewUserData();
                                Toast.makeText(VerifyOtpActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyOtpActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyOtpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }


                });
    }

    private void resetPassword() {
        Intent i = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
        i.putExtra("RegNo", regNo);
        i.putExtra("Branch", branch);
        startActivity(i);
        progressBar.setVisibility(View.GONE);
        finish();
    }

    private void storeNewUserData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        ProfileClassAdmin profile= new ProfileClassAdmin(name, regNo, branch, phone, email, gender, dob, password,imageUrl,status,search,"Admin",true);
        DatabaseReference reference = rootNode.getReference("admins").child(branch);
        reference.child(regNo).setValue(profile);
    }

    private boolean isConnected(VerifyOtpActivity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mob = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mob != null && mob.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

}