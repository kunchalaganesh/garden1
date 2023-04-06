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
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ashrafapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class SignUp3 extends AppCompatActivity {
    ImageView backBtn;
    Button next, login;
    ScrollView scrollView;
    TextInputLayout phoneNo;
    CountryCodePicker countryCodePicker;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading please wait...");


        //Hooks for animation
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup3_next_button);
        login = findViewById(R.id.signup_login_button);

        //Hooks for animation
        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        phoneNo = findViewById(R.id.signup_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);

        next.setOnClickListener(view -> {
            dialog.show();
            if (!validatePhoneNumber()) {
                dialog.dismiss();
                return;

            }
            String _fullName = getIntent().getStringExtra("fullName");
            String _email = getIntent().getStringExtra("email");
            String _userName = getIntent().getStringExtra("userName");
            String _password = getIntent().getStringExtra("password");
            String _date = getIntent().getStringExtra("date");
            String _gender = getIntent().getStringExtra("gender");

            if(_fullName == null || _email == null || _userName == null
            || _password == null || _date == null || _gender == null){
                Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                return;
            }

            dialog.dismiss();

            String _getGetUserEnteredPhoneNumber = phoneNo.getEditText().getText().toString().trim();
            String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + _getGetUserEnteredPhoneNumber;

            FirebaseDatabase.getInstance().getReference()
                    .child("users").orderByChild("phonenumber").equalTo(_phoneNo)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(SignUp3.this, "phone number already registred", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                //Pass all fields to the next activity
                                intent.putExtra("fullName", _fullName);
                                intent.putExtra("email", _email);
                                intent.putExtra("userName", _userName);
                                intent.putExtra("password", _password);
                                intent.putExtra("date", _date);
                                intent.putExtra("gender", _gender);
                                intent.putExtra("phoneNo", _phoneNo);
                                intent.putExtra("whatToDOCreate", "createNewUser");
                                //Add Transition
                                Pair[] pairs = new Pair[3];
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



        });
        login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp3.this, Login.class);
            startActivity(intent);
        });
    }

    /*
        public void callVerifyOTPScreen(View view) {

            if (!validatePhoneNumber()){
                return;
            }
            String _fullName = getIntent().getStringExtra("fullName");
            String _email = getIntent().getStringExtra("email");
            String _userName = getIntent().getStringExtra("userName");
            String _password = getIntent().getStringExtra("password");
            String _date = getIntent().getStringExtra("date");
            String _gender = getIntent().getStringExtra("gender");

            String _getGetUserEnteredPhoneNumber = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();
            String _phoneNo = "+"+countryCodePicker.getFullNumber()+_getGetUserEnteredPhoneNumber;

            Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
            //Pass all fields to the next activity
            intent.putExtra("fullName", _fullName);
            intent.putExtra("email", _email);
            intent.putExtra("username", _userName);
            intent.putExtra("password", _password);
            intent.putExtra("date", _date);
            intent.putExtra("gender", _gender);
            intent.putExtra("phoneNo", _phoneNo);
            intent.putExtra("whatToDO", "createNewUser");
            //Add Transition
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View, String>(next, "transition_next_btn");
            pairs[2] = new Pair<View, String>(login, "transition_login_btn");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
        */
    private boolean validatePhoneNumber() {
        String val = Objects.requireNonNull(phoneNo.getEditText()).getText().toString().trim();
        String checkNumber = "[0-9]{10}";
        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkNumber)) {
            phoneNo.setError("Invalid Number");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }
}