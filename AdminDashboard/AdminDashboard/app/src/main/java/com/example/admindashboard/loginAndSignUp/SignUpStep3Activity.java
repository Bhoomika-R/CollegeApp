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
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admindashboard.R;

import java.util.Calendar;

public class SignUpStep3Activity extends AppCompatActivity {
    private TextView tv_join, tv_us;
    private Button button_next, button_login;
    private RadioGroup rgGender;
    private RadioButton selectedGender;
    private DatePicker dpDob;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);
        getSupportActionBar().hide();

        tv_join = findViewById(R.id.tv_join);
        tv_us = findViewById(R.id.tv_us);
        button_login = findViewById(R.id.button_login);
        button_next = findViewById(R.id.button_next);
        progressBar=findViewById(R.id.progressBar);

        rgGender = findViewById(R.id.rgGender);
        dpDob = findViewById(R.id.dpDob);
    }

    public void onClickNext(View view) {
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!validateGender() | !validateAge()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        selectedGender=findViewById(rgGender.getCheckedRadioButtonId());
        String gender=selectedGender.getText().toString();
        int day= dpDob.getDayOfMonth();
        int month=dpDob.getMonth();
        int year=dpDob.getYear();
        String dob=day+"/"+month+"/"+year;
        Intent previousActivityIntent=getIntent();
        Intent intent = new Intent(SignUpStep3Activity.this, SignUpStep4Activity.class);
        intent.putExtra("Dob",dob);
        intent.putExtra("Gender",gender);
        intent.putExtra("Branch",previousActivityIntent.getStringExtra("Branch"));
        intent.putExtra("Name",previousActivityIntent.getStringExtra("Name"));
        intent.putExtra("Email",previousActivityIntent.getStringExtra("Email"));
        intent.putExtra("RegNo",previousActivityIntent.getStringExtra("RegNo"));
        intent.putExtra("Password",previousActivityIntent.getStringExtra("Password"));
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(tv_join, "transition_join_tv");
        pairs[1] = new Pair<View, String>(tv_us, "transition_us_tv");
        pairs[2] = new Pair<View, String>(button_next, "transition_next_button");
        pairs[3] = new Pair<View, String>(button_login, "transition_login_button");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpStep3Activity.this, pairs);
            startActivity(intent, options.toBundle());
            progressBar.setVisibility(View.GONE);
        }
        else {
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }
    }
    private boolean isConnected(SignUpStep3Activity activity) {
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


    public void onClickLogin(View view) {
        Intent intent = new Intent(SignUpStep3Activity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateGender() {
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        int userAge=currentYear-dpDob.getYear();
        if(userAge<15){
            Toast.makeText(this,"Minimum age required is 15",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
}