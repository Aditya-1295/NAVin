package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AtomicFile;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.otaliastudios.zoom.ZoomLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class navigation extends AppCompatActivity {

    static HashMap<String,String[]> mappings = null;

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
    static EditText tagbox;

    String curr_mall = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        tagbox = findViewById(R.id.tagbox);
        Intent i = getIntent();
        curr_mall = i.getStringExtra("MallName");

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

        tagbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(tagbox.getText().toString().equals(""))mappings=null;
                else mappings = new HashMap<String,String[]>();
                filter(tagbox.getText().toString());
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


    public void filter(String a){

        Thread network_thread = new Thread(new Runnable() {
            BufferedReader reader;
            BufferedWriter writer;
            HttpURLConnection c;

            @Override
            public void run() {
                final StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL("http", "3.19.169.232", "getMapping.php");
                    c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("POST");
                    c.setDoOutput(true);
                    c.setDoInput(true);
                    c.setConnectTimeout(3000);
                    String param = "Name=" + URLEncoder.encode(curr_mall);
                    writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    writer.write(param);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null)
                        response.append(line);

                    Log.e("RESPONSE",response.toString());

                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        JSONArray map_arr = obj.getJSONArray("Map");
                        for (int i = 0; i < map_arr.length(); i++) {
                            JSONObject temp = new JSONObject(map_arr.get(i).toString());
                            mappings.put(temp.getString("Shop"),temp.getString("Tag").split("\\|"));
                        }

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
