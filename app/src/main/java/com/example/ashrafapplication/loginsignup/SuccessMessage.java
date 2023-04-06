package com.example.ashrafapplication.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ashrafapplication.R;

public class SuccessMessage extends AppCompatActivity {

    Button successLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_message);

        successLogin = findViewById(R.id.success_login);

        successLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessMessage.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//
//        super.onBackPressed();

        Intent intent = new Intent(SuccessMessage.this, Login.class);
        startActivity(intent);
        finish();

    }
}