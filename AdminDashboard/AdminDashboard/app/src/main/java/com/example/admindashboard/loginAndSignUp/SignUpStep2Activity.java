package com.example.admindashboard.loginAndSignUp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class SignUpStep2Activity extends AppCompatActivity {
    private TextView tv_join,tv_us;
    private Button button_next,button_login;
    private TextInputLayout etName,etEmail,etRegNo,etPassword,etReEnterPassword;
    private String password;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
        getSupportActionBar().hide();

        tv_join=findViewById(R.id.tv_join);
        tv_us=findViewById(R.id.tv_us);
        button_login=findViewById(R.id.button_login);
        button_next=findViewById(R.id.button_next);
        progressBar=findViewById(R.id.progressBar);

//        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
//        etRegNo=findViewById(R.id.etRegNo);
        etPassword=findViewById(R.id.etPassword);
        etReEnterPassword=findViewById(R.id.etReEnterPasword);


    }

    public void onClickNext(View view){
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validateEmail() | !validatePassword() | !validateReEnterPassword()){
           return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Intent previousActivityIntent=getIntent();
        Intent intent=new Intent(SignUpStep2Activity.this,SignUpStep3Activity.class);
        intent.putExtra("Email",etEmail.getEditText().getText().toString().trim());
        intent.putExtra("RegNo",previousActivityIntent.getStringExtra("RegNo"));
        intent.putExtra("Password",etPassword.getEditText().getText().toString().trim());
        intent.putExtra("Branch",previousActivityIntent.getStringExtra("Branch"));
        Pair[] pairs=new Pair[4];
        pairs[0]=new Pair<View,String>(tv_join,"transition_join_tv");
        pairs[1]=new Pair<View,String>(tv_us,"transition_us_tv");
        pairs[2]=new Pair<View,String>(button_next,"transition_next_button");
        pairs[3]=new Pair<View,String>(button_login,"transition_login_button");
        Query whetherRegistered= FirebaseDatabase.getInstance().getReference("admins").child(previousActivityIntent.getStringExtra("Branch")).orderByChild("regNo").equalTo(previousActivityIntent.getStringExtra("RegNo"));
        whetherRegistered.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child(previousActivityIntent.getStringExtra("RegNo")).child("name").getValue(String.class);
                    intent.putExtra("Name",name);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpStep2Activity.this, pairs);
                        startActivity(intent, options.toBundle());
                        progressBar.setVisibility(View.GONE);
                    } else {
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpStep2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


}


        public void onClickLogin(View view){
        Intent intent=new Intent(SignUpStep2Activity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

//    private boolean validateName(){
//        String name=etName.getEditText().getText().toString().trim();
//        if(name.isEmpty()) {
//          etName.setError("Field cannot be empty");
//          return false;
//        }
//        else{
//            etName.setError(null);
//            etName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
    private boolean validateEmail(){
        String email=etEmail.getEditText().getText().toString().trim();
        String checkEmail="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.isEmpty()) {
            etEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!email.matches(checkEmail)){
            etEmail.setError("Invalid Email");
            return false;
        }
        else{
            etEmail.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }

    }
//    private boolean validateRegNo(){
//        String regNo=etRegNo.getEditText().getText().toString().trim();
//        String checkSpaces="\\A\\w{1,20}\\z";
//        if(regNo.isEmpty()) {
//            etRegNo.setError("Field cannot be empty");
//            return false;
//        }
//        else if(regNo.length()!=10){
//            etRegNo.setError("Invalid ID");
//            return false;
//        }
//        else if(!regNo.matches(checkSpaces)){
//            etRegNo.setError("No whitespaces allowed");
//            return false;
//        }
//        else{
//            etRegNo.setError(null);
//            etRegNo.setErrorEnabled(false);
//            return true;
//        }
//
//    }
    private boolean validatePassword(){
        password=etPassword.getEditText().getText().toString().trim();
        String checkPassword="^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!\\-])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
//        String checkPassword= "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=()])(?=\\S+$).{8,20}$";
        if(password.isEmpty()) {
            etPassword.setError("Field cannot be empty");
            return false;
        }
        else if(password.length()<8){
            etPassword.setError("Need 8 or more characters");
            return false;
        }
        else if(!password.matches(checkPassword)){
            etPassword.setError("Password should contain at least 1 character, 1 special character(@#$%^&+=!-) and 1 number and length should be 8 or more");
            return false;
        }
        else{
            etPassword.setError(null);
            etPassword.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateReEnterPassword(){
        String reEnterPassword=etReEnterPassword.getEditText().getText().toString().trim();
        if(reEnterPassword.isEmpty()) {
            etReEnterPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!reEnterPassword.equals(password)) {
            etReEnterPassword.setError("Not matching above password");
            return false;
        }
        else{
            etReEnterPassword.setError(null);
            etReEnterPassword.setErrorEnabled(false);
            return true;
        }

    }
    private boolean isConnected(SignUpStep2Activity activity) {
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