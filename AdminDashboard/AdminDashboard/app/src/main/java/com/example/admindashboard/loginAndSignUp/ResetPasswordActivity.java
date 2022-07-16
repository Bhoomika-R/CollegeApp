package com.example.admindashboard.loginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admindashboard.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputLayout etPassword,etReEnterPassword;
    private String password,regNo,branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        etPassword=findViewById(R.id.etPassword);
        etReEnterPassword=findViewById(R.id.etReEnterPasword);
        regNo=getIntent().getStringExtra("RegNo");
        branch=getIntent().getStringExtra("Branch");
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onClickReset(View view){
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validatePassword() | !validateReEnterPassword()){
            return;
        }
        FirebaseDatabase rootNode=FirebaseDatabase.getInstance();
        DatabaseReference reference=rootNode.getReference("admins").child(branch).child(regNo);

        reference.child("password").setValue(password);
        startActivity(new Intent(getApplicationContext(),PasswordSuccessMessageActivity.class));
    }

    private boolean isConnected(ResetPasswordActivity activity) {
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

    private boolean validatePassword(){
        password=etPassword.getEditText().getText().toString().trim();
        String checkPassword="^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+!=\\-])" +    //at least 1 special character
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
            etPassword.setError("Password should contain at least 1 character, 1 special character(@#$%^&+=!-) and 1 number length should be 8 or more");
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
}