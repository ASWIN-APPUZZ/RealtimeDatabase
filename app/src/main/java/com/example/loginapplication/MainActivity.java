package com.example.loginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText fname, lname, email, pwd, cnfrmpwd;
    Button register;
    TextView registered;
    FirebaseAuth fAuth;
    FirebaseDatabase fdb;

    String mfname, mlname, memail, mpwd, mcnfrm;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fname= findViewById(R.id.txt_firstname);
        lname=findViewById(R.id.txt_lastname);
        email=findViewById(R.id.txt_email);
        pwd=findViewById(R.id.txt_pwd);
        cnfrmpwd=findViewById(R.id.txt_comfirm);

        fAuth=FirebaseAuth.getInstance();

        fdb=FirebaseDatabase.getInstance();

        registered=findViewById(R.id.tv_registernow);

        register=findViewById(R.id.btn_register);

        //REGISTER
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mfname=fname.getText().toString().trim();
                    mlname=lname.getText().toString().trim();
                    memail=email.getText().toString().trim();
                    mpwd=pwd.getText().toString().trim();
                    mcnfrm=cnfrmpwd.getText().toString().trim();

                    if (TextUtils.isEmpty(memail)){
                        email.setError("Enter Data");
                    } else if (TextUtils.isEmpty(mpwd)) {
                        pwd.setError("EnterData");
                    }else {
                        fAuth.createUserWithEmailAndPassword(memail,mpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    //Realtime Database
                                    FirebaseUser fuser=fAuth.getCurrentUser();
                                    UserId = fAuth.getCurrentUser().getUid();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("First Name",mfname);
                                    user.put("Last Name",mlname);
                                    user.put("Email",memail);
                                    user.put("Password",mpwd);
                                    user.put("Confirm Password",mcnfrm);
                                    fdb.getReference("userpath").child(UserId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainActivity.this, "Data Entered", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "Data not Stored", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    }



            }
        });


        //REDIRECT TO LOGIN PAGE
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        fname.setText("");
        lname.setText("");
        email.setText("");
        pwd.setText("");
        cnfrmpwd.setText("");
    }
}
