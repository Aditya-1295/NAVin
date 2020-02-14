package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


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
                Intent in = new Intent(MainActivity.this, Mallsnearby.class);
                startActivity(in);
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ui = new Intent(MainActivity.this, Discount.class);
                startActivity(ui);
            }
        });

        final ArrayList<String> malls_nearby = getMAll(lati,longi);
        if(malls_nearby.size()==0)return;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, pop.class);
                startActivity(intent);
                intent.putExtra("Malls",malls_nearby);
            }
        }, 1000);
    }

    private ArrayList<String> getMAll(double lati, double longi){
        final ArrayList<String> final_malls = new ArrayList<String>();
        final double X = longi;
        final double Y =lati;
        Thread network_thread = new Thread(new Runnable() {
            BufferedReader reader;
            BufferedWriter writer;
            HttpURLConnection c;

            @Override
            public void run() {
                final StringBuilder response= new StringBuilder();
                try {
                    URL url = new URL("http", "3.19.169.232", "getMalls.php");
                    c =  (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("POST");
                    c.setDoOutput(true);
                    c.setDoInput(true);
                    c.setConnectTimeout(3000);
                    String param = "X="+ URLEncoder.encode(String.valueOf(X)) + "&Y="+ URLEncoder.encode(String.valueOf(Y));
                    writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    writer.write(param);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) !=null)
                        response.append(line);

                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        JSONArray hospital_array = obj.getJSONArray("Malls");
                        for(int i = 0;i<hospital_array.length();i++)
                            final_malls.add(hospital_array.get(i).toString());
                    }
                    catch(JSONException e){}
                }
                catch(Exception e) {
                    try {
                        Toast t = Toast.makeText(getApplicationContext(), "Cannot connect to server", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } catch(Exception e1){}
                }
                finally {
                    try {
                        if(writer!=null && reader !=null){
                            writer.close();
                            reader.close();}
                        c.disconnect();
                    }
                    catch (Exception e){}
                }
            }
        });
        network_thread.start();
        return final_malls;
    }
}