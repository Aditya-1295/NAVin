package com.navin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import android.os.Bundle;

public class splashScreen extends AppCompatActivity {
    TextView flag;
    Context AppContext;
    int delay = 1000;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AppContext = this.getApplication();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //flag = findViewById(R.id.flag);
        //flag.setText("\uD83C\uDDEE\uD83C\uDDF3");

        SharedPreferences shrdPref = getSharedPreferences("NAVin",MODE_PRIVATE);
        String ID = shrdPref.getString("ID",null);
        if(ID==null)
            i = new Intent(this,login.class);
        else{
            ((BinderApplication)AppContext).self_id = ID;
            i = new Intent(this,MainActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
            }
        },delay);

    }
}
