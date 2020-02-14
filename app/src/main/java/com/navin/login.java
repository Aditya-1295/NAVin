package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class login extends AppCompatActivity {
    EditText lemail, lpass;
    Button login, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lemail = findViewById(R.id.lemail);
        lpass = findViewById(R.id.lpass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lemail.getText().toString().trim();
                String password = lpass.getText().toString().trim();
                Login(email, password);


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ut = new Intent(com.navin.login.this, signup.class);
                startActivity(ut);
            }
        });

    }

    private void Login(final String a, final String b) {
        Thread network_thread = new Thread(new Runnable() {
            BufferedReader reader;
            BufferedWriter writer;
            HttpURLConnection c;

            @Override
            public void run() {

                final StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL("http", "3.19.169.232", "user_login.php");
                    c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("POST");
                    c.getPermission();
                    c.setDoOutput(true);
                    c.setDoInput(true);
                    c.setConnectTimeout(3000);
                    String param = "ID=" + URLEncoder.encode(a) + "&PWD=" + URLEncoder.encode(b);
                    writer = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    writer.write(param);
                    writer.flush();

                    reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null)
                        response.append(line);

                    if (response.toString().substring(0, 3).equals("OK:")) {
                        Intent i = new Intent(login.this, MainActivity.class);
                        i.putExtra("USERNAME", response.substring(3));
                        startActivity(i);
                    } else if (response.toString().equals("Failure")) runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = Toast.makeText(getApplicationContext(), "Cannot connect to server", Toast.LENGTH_SHORT);
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
            }
        });
        network_thread.start();
    }
}
