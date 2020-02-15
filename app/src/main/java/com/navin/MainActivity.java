package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
    double lati;
    double longi;
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code = 101;
    ArrayList<String> malls_nearby = new ArrayList<String>();


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
        named.setText("WELCOME " + NAME.toUpperCase());

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

        runtimePermission();
    }

    private void doFurther() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location l = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        getMAll(l.getLatitude(), l.getLongitude());
    }

    private ArrayList<String> getMAll(double lati, double longi) {
        final ArrayList<String> final_malls = new ArrayList<String>();
        final double X = longi;
        final double Y = lati;
        Thread network_thread = new Thread(new Runnable() {
            BufferedReader reader;
            BufferedWriter writer;
            HttpURLConnection c;

            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL("http", "3.19.169.232", "getMalls.php");
                    c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("POST");
                    c.setDoOutput(true);
                    c.setDoInput(true);
                    c.setConnectTimeout(3000);
                    String param = "X=" + URLEncoder.encode(String.valueOf(X)) + "&Y=" + URLEncoder.encode(String.valueOf(Y));
                    writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    writer.write(param);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null)
                        response.append(line);

                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        JSONArray mall_array = obj.getJSONArray("Malls");
                        for (int i = 0; i < mall_array.length(); i++) {
                            JSONObject temp = new JSONObject(mall_array.get(i).toString());
                            final_malls.add(temp.getString("Name"));
                        }
                        if (final_malls.size() == 0) return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, pop.class);
                                Bundle b = new Bundle();
                                b.putSerializable("Malls", final_malls);
                                intent.putExtra("BUNDLE", b);
                                startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                    }
                } catch (Exception e) {
                    try {
                        Toast t = Toast.makeText(getApplicationContext(), "Cannot connect to server", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } catch (Exception e1) {
                    }
                } finally {
                    try {
                        if (writer != null && reader != null) {
                            writer.close();
                            reader.close();
                        }
                        c.disconnect();
                    } catch (Exception e) {
                    }
                }
            }
        });
        network_thread.start();
        return null;
    }

    private void runtimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, Request_Code);
        }
        doFurther();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doFurther();

                }
                break;
        }
    }
}