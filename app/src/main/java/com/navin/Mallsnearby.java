package com.navin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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


        list.add("laude");
        list.add("jhaat");
        list.add("benchod");
        list.add("lae");
        list.add("chut");
        list.add("bhoote");
        list.add("bosada");
        list.add("jhatoora");

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);

        databaselist.setAdapter((adapter));


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

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
