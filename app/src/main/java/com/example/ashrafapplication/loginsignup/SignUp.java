package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashrafapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    //Variables
    ImageView backBtn;
    Button next, login;
    TextInputLayout fullName, userName, email, password;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        dialog = new ProgressDialog(this);

        dialog.setTitle("Loading please wait...");

        //Hooks for animation
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);

        fullName = findViewById(R.id.signup_fullname);
        userName = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                if (!validateFullName()|!validateEmail()|!validateUserName()|!validatePassword()){
                    dialog.dismiss();
                    return;

                }






                String _fullName = fullName.getEditText().getText().toString();
                String _email = email.getEditText().getText().toString();
                String _userName = userName.getEditText().getText().toString();
                String _password = password.getEditText().getText().toString();

                FirebaseDatabase.getInstance().getReference()
                        .child("users").orderByChild("email").equalTo(_email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    dialog.dismiss();
                                    Toast.makeText(SignUp.this, "email already registred", Toast.LENGTH_SHORT).show();
                                }else{
                                    dialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), SignUp2.class);
                                    //Pass all fields to the next activity
                                    intent.putExtra("fullName", _fullName);
                                    intent.putExtra("email", _email);
                                    intent.putExtra("userName", _userName);
                                    intent.putExtra("password", _password);
                                    //Add Shared Animation
                                    Pair[] pairs = new Pair[5];
                                    pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
                                    pairs[1] = new Pair<View, String>(next, "transition_next_btn");
                                    pairs[2] = new Pair<View, String>(login, "transition_login_btn");
                                    startActivity(intent);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                dialog.dismiss();
                            }
                        });



            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });


    }//End onCreate

    private boolean validatePassword() {
        String val = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        String checkPassword = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,20}$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = userName.getEditText().getText().toString().trim();
        String checkSpaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            userName.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 20) {
            userName.setError("User Name is too large");
            return false;
        } else if (!val.matches(checkSpaces)) {
            userName.setError("No white spaces are allowed");
            return false;
        } else {
            userName.setError(null);
            userName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFullName() {
        String val = fullName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }
}

