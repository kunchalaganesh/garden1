package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashrafapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class ForgetPassword extends AppCompatActivity {
    // Variables
    private ImageView screenIcon;
    private TextView title, description;
    private TextInputLayout phoneNumberTextField;
    private CountryCodePicker countryCodePicker;
    private Button nextBtn;
    private Animation animation;
    //RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //Hooks
        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);
        nextBtn = findViewById(R.id.forget_password_next_btn);
        //progressBar = findViewById(R.id.forget_password_progress_bar);

        //animation = AnimationUtils.loadAnimation(this,R.anim.slideA)

        nextBtn.setOnClickListener(view -> {
            if (!validatePhoneNumber()) {
                return;
            }
            //progressBar.setVisibility(View.VISIBLE);
            //Get Data from fields
            String _phoneNumber = phoneNumberTextField.getEditText().getText().toString();

            if (_phoneNumber.charAt(0) == '0') {
                _phoneNumber = _phoneNumber.substring(1);
            }
            final String _completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;
            Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phonenumber").equalTo(_completePhoneNumber);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        phoneNumberTextField.setError(null);
                        phoneNumberTextField.setErrorEnabled(false);

                        Intent intent = new Intent(ForgetPassword.this, forgetotp.class);
                        intent.putExtra("phoneNo",_completePhoneNumber);
                        intent.putExtra("whatToDo","updateData");
//                        intent.putExtra()
                        startActivity(intent);
                        finish();
                        //progressBar.setVisibility(View.GONE);
                    }
                    else {
                        //progressBar.setVisibility(View.GONE);
                        phoneNumberTextField.setError("No such user exist.");
                        phoneNumberTextField.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }
    //validation
    private boolean validatePhoneNumber() {
        String val = Objects.requireNonNull(phoneNumberTextField.getEditText()).getText().toString().trim();
        String checkNumber = "[0-9]{10}";
        if (val.isEmpty()) {
            phoneNumberTextField.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkNumber)) {
            phoneNumberTextField.setError("Invalid Number");
            return false;
        } else {
            phoneNumberTextField.setError(null);
            phoneNumberTextField.setErrorEnabled(false);
            return true;
        }
    }
}