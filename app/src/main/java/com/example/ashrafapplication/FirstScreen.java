package com.example.ashrafapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class FirstScreen extends AppCompatActivity {
    ImageView backBtn;
    Button book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        backBtn = findViewById(R.id.firstScreenBack_Btn);
        book = findViewById(R.id.firstScreenBook_Btn);


    }
}