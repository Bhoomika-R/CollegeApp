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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admindashboard.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPasswordActivity extends AppCompatActivity {
    private CountryCodePicker ccpPhone;
    private TextInputLayout etPhone,etRegNo;
    private ProgressBar progressBar;
    private Spinner branchSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        ccpPhone=findViewById(R.id.ccpNumber);
        etPhone=findViewById(R.id.edPhone);
        etRegNo=findViewById(R.id.edRegNo);
        progressBar=findViewById(R.id.progressBar);
        branchSpinner=findViewById(R.id.branch_spinner);

        setupSpinner();
    }

    private void setupSpinner(){
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.branch_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapter2);

    }

    public void onClickNext(View view){
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validatePhone()|| !validateRegNo()){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String userEnteredPhone=etPhone.getEditText().getText().toString().trim();
        String phone="+"+ccpPhone.getSelectedCountryCode()+userEnteredPhone;
        String regNo=etRegNo.getEditText().getText().toString().trim();

        Query whetherRegistered= FirebaseDatabase.getInstance().getReference("admins").child(branchSpinner.getSelectedItem().toString()).orderByChild("regNo").equalTo(regNo);
        whetherRegistered.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    etPhone.setError(null);
                    etRegNo.setError(null);
                    etPhone.setErrorEnabled(false);
                    etRegNo.setErrorEnabled(false);

                    String systemPhone=snapshot.child(regNo).child("phone").getValue(String.class);
                    if(phone.equals(systemPhone)) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("Phone", phone);
                        intent.putExtra("Branch", branchSpinner.getSelectedItem().toString());
                        intent.putExtra("RegNo",regNo);
                        intent.putExtra("WhatToDo", "ResetPassword");
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this,"Reg.ID and phone are not matching",Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateRegNo() {
        String regNo = etRegNo.getEditText().getText().toString().trim();
        String checkSpaces = "\\A\\w{1,20}\\z";
        if (regNo.isEmpty()) {
            etRegNo.setError("Field cannot be empty");
            return false;
        } else if (regNo.length() != 10) {
            etRegNo.setError("Invalid ID");
            return false;
        } else if (!regNo.matches(checkSpaces)) {
            etRegNo.setError("No whitespaces allowed");
            return false;
        } else {
            etRegNo.setError(null);
            etRegNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone() {
        String val = etPhone.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            etPhone.setError("Enter valid phone number");
            return false;
        } else if (val.length() != 10) {
            etPhone.setError("Enter valid phone number");
            return false;
        } else if (!val.matches("[0-9]+")) {
            etPhone.setError("Enter valid phone number");
            return false;
        } else {
            etPhone.setError(null);
            etPhone.setErrorEnabled(false);
            return true;
        }
    }
        private boolean isConnected(ForgotPasswordActivity activity) {
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
}