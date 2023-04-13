package com.example.loginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    TextView fname,lname,email;



    FirebaseDatabase fdb;

    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fdb = FirebaseDatabase.getInstance();
        fauth = FirebaseAuth.getInstance();

        fname=findViewById(R.id.tv_fname);
        lname=findViewById(R.id.tv_lname);
        email=findViewById(R.id.tv_email);

        String UserId = fauth.getCurrentUser().getUid();

        fdb.getReference("userpath").child(UserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(HomeActivity.this, "Data Exists", Toast.LENGTH_SHORT).show();
                    DataSnapshot dataSnapshot = task.getResult();
                    String firstname=String.valueOf(dataSnapshot.child("First Name").getValue());
                    fname.setText(firstname);
                    String lastname=String.valueOf(dataSnapshot.child("Last Name").getValue());
                    lname.setText(lastname);
                    String Email=String.valueOf(dataSnapshot.child("Email").getValue());
                    email.setText(Email);
                }else {
                    Toast.makeText(HomeActivity.this, "User Dosen't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}