package com.navin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {
    EditText editname,editemail,editpass,editphone;
    Button registerbtn,alreadybtn;
    FirebaseAuth fAuth;



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

        fAuth = FirebaseAuth.getInstance();


        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString().trim();
                String password = editpass.getText().toString().trim();

                if (TextUtils.isEmpty(password)){
                    editemail.setError("Please Enter Password");
                    return;

                }

                if (TextUtils.isEmpty(email)){
                    editpass.setError("This field cannot be left empty");
                    return;
                }

                if (password.length()<6){
                    editpass.setError("Password too short");
                    return;
                }

                // registering the user to firebase;

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(signup.this, "Error please Try again "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        alreadybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });



    }
}
