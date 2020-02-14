package com.navin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class navigation extends AppCompatActivity {

    Button navigate, endn,swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        navigate = findViewById(R.id.navigate);
        endn = findViewById(R.id.endn);
        swipe = findViewById(R.id.swipe);


        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(navigation.this,popnavi.class);
                startActivity(k);
            }
        });


        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigate.setVisibility(View.INVISIBLE);



                endn.setVisibility(View.VISIBLE);
            }
        });


        endn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                navigate.setVisibility(View.VISIBLE);



                endn.setVisibility(View.INVISIBLE);

            }

        });
    }
}