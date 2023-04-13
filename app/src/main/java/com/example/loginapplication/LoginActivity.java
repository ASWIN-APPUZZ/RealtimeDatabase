package com.example.loginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText email, psd;
    TextView forgot,register;
    Button login;

    String memail,mpsd;

    FirebaseAuth fauth;
    FirebaseDatabase fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fauth=FirebaseAuth.getInstance();

        email=findViewById(R.id.txt_email);
        psd=findViewById(R.id.txt_pwd);
        forgot=findViewById(R.id.tv_forgot);
        register=findViewById(R.id.tv_registernow);

        login=findViewById(R.id.btn_login);

//        forgot.setOnClickListener(new View().OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(MainActivity.this,forgotActivity.class);
//                startActivity(intent);
//            }
//        });

        //reirect to register page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memail = email.getText().toString();
                mpsd = psd.getText().toString();
                fauth.signInWithEmailAndPassword(memail,mpsd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        }
                    }
                });
            }
        });

        email.setText("");
        psd.setText("");

    }
}