package com.example.ashrafapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminpanel extends AppCompatActivity {

    Button auto, dinn, hall, garden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);


        auto = findViewById(R.id.autoscroll);
        dinn = findViewById(R.id.dinning);
        hall = findViewById(R.id.hall);
        garden = findViewById(R.id.garden);

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(adminpanel.this, adminimages.class);
                go.putExtra("key", "auto");
                startActivity(go);

            }
        });

        dinn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(adminpanel.this, adminimages.class);
                go.putExtra("key", "dinning");
                startActivity(go);

            }
        });

        hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(adminpanel.this, adminimages.class);
                go.putExtra("key", "hall");
                startActivity(go);

            }
        });

        garden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go = new Intent(adminpanel.this, adminimages.class);
                go.putExtra("key", "garden");
                startActivity(go);

            }
        });


    }

    public void gototal(View view) {
        Intent go = new Intent(adminpanel.this, totalamount.class);
        startActivity(go);
    }
}