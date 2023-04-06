package com.example.ashrafapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ashrafapplication.loginsignup.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    TextView pname, phone, email;
    Button admin, logout;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dialog = new ProgressDialog(profile.this);

        dialog.setTitle("Loading please wait...");

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        pname = findViewById(R.id.pname);
        phone = findViewById(R.id.pphone);
        email = findViewById(R.id.pemail);

        admin = findViewById(R.id.padmin);
        logout = findViewById(R.id.plogout);

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            dialog.dismiss();
                            pname.setText(snapshot.child("fullname").getValue(String.class));
                            phone.setText(snapshot.child("phonenumber").getValue(String.class));
                            email.setText(snapshot.child("email").getValue(String.class));


                        } else {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                    }
                });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(profile.this, adminpanel.class);
                startActivity(go);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent go = new Intent(profile.this, Login.class);
                startActivity(go);
                finish();
            }
        });


    }
}