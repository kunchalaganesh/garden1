package com.example.ashrafapplication.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashrafapplication.R;

import java.util.Calendar;

public class SignUp2 extends AppCompatActivity {
    //Variables
    ImageView backBtn;
    Button next, login;
    TextView titleText, slideText;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2);

        //Hooks
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup2_next_button);
        login = findViewById(R.id.signup_login_button);

        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);

        next.setOnClickListener(view -> {
            if (!validateGender() | !validateAge()) {
                return;
            }
            String _fullName = getIntent().getStringExtra("fullName");
            String _email = getIntent().getStringExtra("email");
            String _userName = getIntent().getStringExtra("userName");
            String _password = getIntent().getStringExtra("password");

            if(_fullName == null || _email == null || _userName== null || _password == null){
                Toast.makeText(this, "failed to fetch name email usename and password please go back and register again", Toast.LENGTH_SHORT).show();

                return;
            }

            selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
            String _gender = selectedGender.getText().toString();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            String _date = day + "/" + month + "/" + year;

            Intent intent = new Intent(SignUp2.this, SignUp3.class);
            //Pass all fields to the next activity
            intent.putExtra("fullName", _fullName);
            intent.putExtra("email", _email);
            intent.putExtra("userName", _userName);
            intent.putExtra("password", _password);
            intent.putExtra("date", _date);
            intent.putExtra("gender", _gender);
            //Add Shared Animation
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View, String>(next, "transition_next_btn");
            pairs[2] = new Pair<View, String>(login, "transition_login_btn");

            startActivity(intent);
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp2.this, Login.class);
                startActivity(intent);
            }
        });

    }

    /*
        public void call3rdSigupScreen(View view) {
            if (!validateGender() | !validateAge()) {
                return;
            }
            String _fullName = getIntent().getStringExtra("fullName");
            String _email = getIntent().getStringExtra("email");
            String _userName = getIntent().getStringExtra("userName");
            String _password = getIntent().getStringExtra("password");

            selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
            String _gender = selectedGender.getText().toString();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String _date = day + "/" + month + "/" + year;

            Intent intent = new Intent(getApplicationContext(), SignUp3.class);
            //Pass all fields to the next activity
            intent.putExtra("fullName", _fullName);
            intent.putExtra("email", _email);
            intent.putExtra("username", _userName);
            intent.putExtra("password", _password);
            intent.putExtra("date", _date);
            intent.putExtra("gender", _gender);

            //Add Transition and call next activity
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View, String>(next, "transition_next_btn");
            pairs[2] = new Pair<View, String>(login, "transition_login_btn");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2.this, pairs);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }
    */
    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select the gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 18) {
            Toast.makeText(this, "The age should be above 18 years", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    public void call3rdSigupScreen(View view) {
    }
}