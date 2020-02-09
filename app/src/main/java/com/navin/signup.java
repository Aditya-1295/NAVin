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


import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    EditText editname,editemail,editpass,editphone;
    Button registerbtn,alreadybtn;
    FirebaseAuth fAuth;

    public static final String Firebase_Server_URL = "https://navin-dea57.firebaseio.com/";
    String nameholder,phoneholder;

    Firebase firebase;
    DatabaseReference databaseReference;

     public static final String Database_Path = "User_Detail_Database";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Firebase.setAndroidContext(signup.this);

        firebase = new Firebase(Firebase_Server_URL);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);





        editname = findViewById(R.id.editname);

        editemail = findViewById(R.id.editemail);
        editpass = findViewById(R.id.editpassword);
        editphone = findViewById(R.id.editphone);

        registerbtn = findViewById(R.id.registerbtn);
        alreadybtn = findViewById(R.id.alreadybtn);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userDetails userDetail = new userDetails();


                getDataFromEditText();

                userDetail.setUserName(nameholder);
                userDetail.setPhoneNumber(phoneholder);
                String UserRecordIDFromServer = databaseReference.push().getKey();
                databaseReference.child(UserRecordIDFromServer).setValue(userDetail);
                Toast.makeText(signup.this, "sent to database",Toast.LENGTH_LONG).show();


            }
        });

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

    private void getDataFromEditText() {
         nameholder = editname.getText().toString().trim();
         phoneholder = editphone.getText().toString().trim();



    }
}
