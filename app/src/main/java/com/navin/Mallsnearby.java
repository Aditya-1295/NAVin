package com.navin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class Mallsnearby extends AppCompatActivity {
    SearchView search;
    ListView databaselist;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallsnearby);

        search = findViewById(R.id.search);
        databaselist = findViewById(R.id.databaselist);


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

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);

                return false;
            }
        });
    }



}
