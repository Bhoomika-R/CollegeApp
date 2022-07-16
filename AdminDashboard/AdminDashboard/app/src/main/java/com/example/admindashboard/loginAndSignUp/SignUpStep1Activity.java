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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admindashboard.R;


public class SignUpStep1Activity extends AppCompatActivity {
    private Spinner branchSpinner;
    private TextView tv_join,tv_us,tv_select_sem;
    private Button button_next,button_login;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step1);
        getSupportActionBar().hide();

        tv_join=findViewById(R.id.tv_join);
        tv_us=findViewById(R.id.tv_us);
//        tv_select_sem=findViewById(R.id.tv_select_sem);
        button_login=findViewById(R.id.button_login);
        button_next=findViewById(R.id.button_next);
        progressBar=findViewById(R.id.progressBar);

//        roleSpinner=findViewById(R.id.role_spinner);
        branchSpinner=findViewById(R.id.branch_spinner);
//        semSpinner=findViewById(R.id.sem_spinner);
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
        progressBar.setVisibility(View.VISIBLE);
        Intent intent=new Intent(SignUpStep1Activity.this,SignUpStep1_1Activity.class);
        intent.putExtra("Branch",branchSpinner.getSelectedItem().toString());
        Pair[] pairs=new Pair[4];
        pairs[0]=new Pair<View,String>(tv_join,"transition_join_tv");
        pairs[1]=new Pair<View,String>(tv_us,"transition_us_tv");
        pairs[2]=new Pair<View,String>(button_next,"transition_next_button");
        pairs[3]=new Pair<View,String>(button_login,"transition_login_button");

       if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
           ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpStep1Activity.this, pairs);
           startActivity(intent, options.toBundle());
           progressBar.setVisibility(View.GONE);
       }
       else {
           startActivity(intent);
           progressBar.setVisibility(View.GONE);
       }
    }

    public void onClickLogin(View view){
        Intent intent=new Intent(SignUpStep1Activity.this,LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private boolean isConnected(SignUpStep1Activity activity) {
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