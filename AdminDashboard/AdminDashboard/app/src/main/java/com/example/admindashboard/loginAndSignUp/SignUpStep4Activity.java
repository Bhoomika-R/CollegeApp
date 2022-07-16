package com.example.admindashboard.loginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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


public class SignUpStep4Activity extends AppCompatActivity {
    private CountryCodePicker ccpPhone;
    private TextInputLayout edPhone;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step4);
        getSupportActionBar().hide();

        ccpPhone=findViewById(R.id.ccpNumber);
        edPhone=findViewById(R.id.edPhone);
        progressBar=findViewById(R.id.progressBar);

    }

    public void onClickNext(View view){
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validatePhone()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Intent intentPreviousActivity=getIntent();
        String branch=intentPreviousActivity.getStringExtra("Branch");
        String name=intentPreviousActivity.getStringExtra("Name");
        String email=intentPreviousActivity.getStringExtra("Email");
        String regNo=intentPreviousActivity.getStringExtra("RegNo");
        String password=intentPreviousActivity.getStringExtra("Password");
        String dob=intentPreviousActivity.getStringExtra("Dob");
        String gender=intentPreviousActivity.getStringExtra("Gender");

        String userEnteredPhone=edPhone.getEditText().getText().toString().trim();
        String phone="+"+ccpPhone.getSelectedCountryCode()+userEnteredPhone;

        Intent intent=new Intent(SignUpStep4Activity.this,VerifyOtpActivity.class);
        intent.putExtra("Dob",dob);
        intent.putExtra("Branch",branch);
        intent.putExtra("Name",name);
        intent.putExtra("Email",email);
        intent.putExtra("RegNo",regNo);
        intent.putExtra("Password",password);
        intent.putExtra("Gender",gender);
        intent.putExtra("Phone",phone);
        Query phoneMatching= FirebaseDatabase.getInstance().getReference("admins").child(branch).orderByChild("regNo").equalTo(regNo);
        phoneMatching.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String systemPhone = snapshot.child(regNo).child("phone").getValue(String.class);
                    if (systemPhone.equals(phone)) {
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(SignUpStep4Activity.this, "This phone number is not linked to your account.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpStep4Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onClickLogin(View view){
        Intent intent=new Intent(SignUpStep4Activity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validatePhone(){
        String val = edPhone.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            edPhone.setError("Enter valid phone number");
            return false;
        }
        else if(val.length()!=10){
            edPhone.setError("Enter valid phone number");
            return false;
        }
        else if(!val.matches("[0-9]+")){
            edPhone.setError("Enter valid phone number");
            return false;
        }
        else {
            edPhone.setError(null);
            edPhone.setErrorEnabled(false);
            return true;
        }

    }
    private boolean isConnected(SignUpStep4Activity activity) {
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