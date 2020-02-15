package com.navin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class pop extends Activity {

    ListView mallnear;
    ArrayList<String> List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop);

        Intent i = getIntent();
        Bundle b = i.getBundleExtra("BUNDLE");

        final ArrayList<String> malls = (ArrayList<String>) b.getSerializable("Malls");
        final ArrayList<String> files = (ArrayList<String>) b.getSerializable("Files");

        mallnear = findViewById(R.id.mallnear);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, malls) {

            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setTextColor(Color.BLACK);

                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(pop.this,"Downloading please wait",Toast.LENGTH_LONG).show();
                        final Mall mall = null;

                        Thread net_thd = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    BufferedInputStream in = new BufferedInputStream(new URL("http://3.19.169.232/MAPS/"+files.get(position)).openStream());
                                    ObjectInputStream obji = new ObjectInputStream(in);
                                    final Mall mall= (Mall) obji.readObject();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(pop.this, navigation.class);
                                            i.putExtra("MallName",malls.get(position));
                                            Bundle b = new Bundle();
                                            b.putSerializable("MALL",mall);
                                            i.putExtra("BUNDLE",b);
                                            startActivity(i);

                                        }
                                    });

                                } catch (Exception e) { Log.e("df",e.toString());}
                            }
                        });
                        net_thd.start();


                    }
                });
                return view;
            }
        };


        mallnear.setAdapter((adapter));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }


    View.OnClickListener navigate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent l = new Intent(pop.this, navigation.class);
            startActivity(l);
        }
    };


}
