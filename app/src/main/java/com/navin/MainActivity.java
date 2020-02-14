package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {

    Button mall;
    Button logout;
    Button discount;
    TextView named;
    Double lati;
    Double longi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mall = findViewById(R.id.mall);
        logout = findViewById(R.id.logout);
        discount = findViewById(R.id.discount);
        named = findViewById(R.id.named);


        Intent i = getIntent();
        String NAME = i.getStringExtra("USERNAME");
        named.setText(NAME);












        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ac = new Intent(MainActivity.this, login.class);
                startActivity(ac);
            }
        });

        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,Mallsnearby.class);
                startActivity(in);
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ui = new Intent(MainActivity.this,Discount.class);
                startActivity(ui);
            }
        });



    }


    String[] getMAll(Double lati, Double longi){

        if (){

        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,pop.class);
                    startActivity(intent);
                }
            },1000);

        }

    }




}