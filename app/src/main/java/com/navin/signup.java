package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class signup extends AppCompatActivity {
    EditText editname, editemail, editpass, editphone;
    Button registerbtn, alreadybtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        editpass = findViewById(R.id.editpassword);
        editphone = findViewById(R.id.editphone);
        registerbtn = findViewById(R.id.registerbtn);
        alreadybtn = findViewById(R.id.alreadybtn);
        alreadybtn.setOnClickListener(alreadyclicked);
        registerbtn.setOnClickListener(registerclicked);


    }

    View.OnClickListener registerclicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (editname.getText().toString().equals("") || editpass.getText().toString().equals("") || editphone.getText().toString().equals("") || editemail.getText().toString().equals("")) {
                Toast.makeText(signup.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();


                return;
            }


            Thread network_thread = new Thread(new Runnable() {
                BufferedReader reader;
                BufferedWriter writer;
                HttpURLConnection c;

                @Override
                public void run() {
                    final StringBuilder response = new StringBuilder();
                    try {
                        URL url = new URL("http", "3.19.169.232", "create_account.php");
                        c = (HttpURLConnection) url.openConnection();
                        c.setRequestMethod("POST");
                        c.setDoOutput(true);
                        c.setDoInput(true);
                        c.setConnectTimeout(3000);
                        String param = "&NAME=" + URLEncoder.encode(editname.getText().toString()) + "&EMAIL=" + URLEncoder.encode(editemail.getText().toString()) + "&PASSWORD=" + URLEncoder.encode(editpass.getText().toString()) + "&PHONE=" + URLEncoder.encode(editphone.getText().toString());
                        writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                        writer.write(param);
                        writer.flush();

                        reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

                        String line;
                        while ((line = reader.readLine()) != null)
                            response.append(line);
                    } catch (Exception e) {
                        try {
                            Log.e("lol", e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast t = Toast.makeText(getApplicationContext(), "You may be offline !", Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                }
                            });
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
                    Log.e("lol", response.toString());
                    if (response.toString().equals("OK")) {
                        Intent i = new Intent(signup.this, MainActivity.class);
                        i.putExtra("USERNAME", editname.getText().toString());
                        startActivity(i);
                    }
                }
            });
            network_thread.start();
        }
    };

    View.OnClickListener alreadyclicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent j = new Intent(signup.this, login.class);
            startActivity(j);
        }
    };

}