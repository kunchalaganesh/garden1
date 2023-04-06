package com.example.ashrafapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenActivity extends AppCompatActivity {


    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Get a reference to the ImageView in the layout
        imageView = findViewById(R.id.image_view);

        // Load the image into the ImageView using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);


    }
}