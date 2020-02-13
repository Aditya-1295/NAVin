package com.navin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class navigation extends AppCompatActivity {

    Button navigate, endn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        navigate = findViewById(R.id.navigate);
        endn = findViewById(R.id.endn);


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