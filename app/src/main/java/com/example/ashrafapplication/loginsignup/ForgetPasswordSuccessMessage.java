package com.example.ashrafapplication.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ashrafapplication.R;

public class ForgetPasswordSuccessMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_success_message);
    }

    public void calllogin(View view) {

        Intent go = new Intent(ForgetPasswordSuccessMessage.this, Login.class);
        startActivity(go);
        finish();
    }
}