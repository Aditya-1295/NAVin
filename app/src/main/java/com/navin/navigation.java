package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.zoom.ZoomLayout;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class navigation extends AppCompatActivity {

    private static final int FILE = 11;
    private static final int RESULT_LOAD_DOCUMENT = 1;
    private Button getImage, navigate, sShop,dShop;
    static public ZoomLayout zl;
    public PinchZoomPan pzp;

    static float devWidth;
    static float devHeight;
    static Location[] endpoint = new Location[2];

    static ArrayList<Location> path = null;
    Button endn, swipe;
    static TextView currShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent i = getIntent();
        Bundle bb =  i.getBundleExtra("BUNDLE");
        PinchZoomPan.main_mall = (Mall) bb.getSerializable("MALL");

        currShopName = findViewById(R.id.currShopName);
        //getImage  = findViewById(R.id.getIm);
        pzp = findViewById(R.id.pinchZoomPan);
        navigate = findViewById(R.id.navigate);
        sShop = findViewById(R.id.sShop);
        dShop = findViewById(R.id.dShop);
        Log.e("LOL", "rfgsr");
        zl = findViewById(R.id.zoomlay);

        navigate = findViewById(R.id.navigate);
        swipe = findViewById(R.id.swipe);

        sShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endpoint[0]=PinchZoomPan.curr_s_node;
            }
        });

        dShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endpoint[1]= PinchZoomPan.curr_s_node;
            }
        });


        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(navigation.this, popnavi.class);
                startActivity(k);
            }
        });


        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigate.getText().equals("Start navigating")) {
                    if(endpoint[0]==null||endpoint[1]==null) {
                        Toast.makeText(navigation.this, "Source or destination not specified", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        navigate.setText("Stop Navigating");
                        PathFinder p = new PathFinder(endpoint[0], endpoint[1]);
                        path = p.final_path;
                    }
                }
                else if(navigate.getText().equals("Stop Navigating")){
                    path=null;
                    endpoint[0]=endpoint[1] = null;
                    navigate.setText("Start navigating");
                }


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_DOCUMENT) {

        }
        try {
            InputStream i = getContentResolver().openInputStream(data.getData());
            ObjectInputStream obj = new ObjectInputStream(i);
            PinchZoomPan.main_mall = (Mall) obj.readObject();
            devHeight = zl.getHeight();
            devWidth = zl.getWidth();
        } catch (Exception e) {
            Log.e("input Stream", "" + e);
        }
    }
}
