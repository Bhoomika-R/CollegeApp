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

public class SignUpStep1_1Activity extends AppCompatActivity {
    private TextView tv_join,tv_us;
    private Button button_next,button_login;
    private TextInputLayout etRegNo;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step11);
        getSupportActionBar().hide();

        tv_join=findViewById(R.id.tv_join);
        tv_us=findViewById(R.id.tv_us);
        button_login=findViewById(R.id.button_login);
        button_next=findViewById(R.id.button_next);
        progressBar=findViewById(R.id.progressBar);

        etRegNo=findViewById(R.id.etRegNo);
    }

    public void onClickNext(View view){
        if(!isConnected(this)){
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Intent previousActivityIntent=getIntent();
        Intent intent=new Intent(SignUpStep1_1Activity.this,SignUpStep2Activity.class);
        intent.putExtra("RegNo",etRegNo.getEditText().getText().toString().trim());
        intent.putExtra("Branch",previousActivityIntent.getStringExtra("Branch"));
        Pair[] pairs=new Pair[4];
        pairs[0]=new Pair<View,String>(tv_join,"transition_join_tv");
        pairs[1]=new Pair<View,String>(tv_us,"transition_us_tv");
        pairs[2]=new Pair<View,String>(button_next,"transition_next_button");
        pairs[3]=new Pair<View,String>(button_login,"transition_login_button");
        String regNo=etRegNo.getEditText().getText().toString().trim();
        Query whetherRegistered= FirebaseDatabase.getInstance().getReference("admins").child(previousActivityIntent.getStringExtra("Branch")).orderByChild("regNo").equalTo(regNo);
        whetherRegistered.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean registered = snapshot.child(regNo).child("registered").getValue(Boolean.class);
                    if (registered) {
                        Toast.makeText(SignUpStep1_1Activity.this, "User already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpStep1_1Activity.this, pairs);
                            startActivity(intent, options.toBundle());
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                } else {
                    Toast.makeText(SignUpStep1_1Activity.this, "No such user added. Please contact admin.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpStep1_1Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private boolean isConnected(SignUpStep1_1Activity activity) {
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