package com.navin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Mallsnearby extends AppCompatActivity {
    EditText search;
    ListView databaselist;
    Button navin;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallsnearby);

        search = findViewById(R.id.search);
        databaselist = findViewById(R.id.databaselist);
        navin = findViewById(R.id.navin);


        list = new ArrayList<String>();


        list.add("BU A BLOCK");
        list.add("Test Mall 2");
        list.add("Test Mall 3");
        list.add("Test Mall 4");
        list.add("Test Mall 5");
        list.add("Test Mall 6");
        list.add("Test Mall 7");
        list.add("Test Mall 8");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        databaselist.setAdapter((adapter));

        databaselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent j = new Intent(Mallsnearby.this, navigation.class);
                    startActivity(j);
                }
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            private Timer timer = new Timer();
            private final long DELAY = 500; // milliseconds

            @Override
            public void afterTextChanged(final Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.getFilter().filter(search.getText());
                            }
                        });
                    }
                }, DELAY);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navin.setVisibility(View.VISIBLE);

            }
        });

    }


}
