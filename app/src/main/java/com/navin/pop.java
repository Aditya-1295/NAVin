package com.navin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class pop extends FragmentActivity implements OnMapReadyCallback {
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code=101;
    Button navi;
    double longi,lati;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);


        navi = findViewById(R.id.navi);
        navi.setOnClickListener(navigate);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.7));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }



    View.OnClickListener navigate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent l = new Intent(pop.this,navigation.class);
            startActivity(l);
        }
    };
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},Request_Code);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    mlocation=location;
                    Toast.makeText(getApplicationContext(),mlocation.getLatitude()+""+mlocation.getLongitude(),
                            Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(pop.this);

                    longi = mlocation.getLongitude();
                    lati = mlocation.getLatitude();

                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("You Are Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        googleMap.addMarker(markerOptions);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_Code:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLastLocation();
                }
                break;

        }
    }
}
