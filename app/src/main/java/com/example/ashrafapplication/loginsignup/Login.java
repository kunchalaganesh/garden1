package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashrafapplication.R;
import com.example.ashrafapplication.UserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class Login extends AppCompatActivity {

    //Variables
    ImageView backBtn;
    Button login, createAccount, forgetPassword;
    TextInputLayout phoneNumber, password;
    CountryCodePicker countryCodePicker;
    //RelativeLayout progressBar;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        //progressBar = findViewById(R.id.login_progress_bar);
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        backBtn = findViewById(R.id.login_back_button);
        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.letTheUserLogIn);
        createAccount = findViewById(R.id.letTheUserCreateAccount);
        forgetPassword = findViewById(R.id.login_forget_password);

        forgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, ForgetPassword.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            dialog.show();

            if (!validatePhoneNumber() | !validatePassword()) {
                dialog.dismiss();
                return;
            }
            //progressBar.setVisibility(View.VISIBLE);
            //get data
            String _phoneNumber = phoneNumber.getEditText().getText().toString();
            final String _password = password.getEditText().getText().toString();

            if (_phoneNumber.charAt(0) == '0') {
                _phoneNumber = _phoneNumber.substring(1);
            }
            final String _completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;

            //Query
            Query checkUser = FirebaseDatabase.getInstance().getReference("password").child(_completePhoneNumber);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        dialog.dismiss();
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);

                        String systemPassword = snapshot.child("password").getValue(String.class);
                        String semail = snapshot.child("email").getValue(String.class);


                        //assert systemPassword != null;
                        if (systemPassword.equals(_password) && semail != null) {
                            password.setError(null);
                            password.setErrorEnabled(false);
                            //progressBar.setVisibility(View.GONE);

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(semail, _password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(Login.this, UserDashboard.class);
                                                startActivity(intent);
                                                finish();

                                            } else {
                                                Toast.makeText(Login.this, "Failed to login " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
//                            String _fullName = snapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
//                            String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
//                            String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
//                            String _dataOfBirth = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);
//                            //progressBar.setVisibility(View.VISIBLE);
//                            Toast.makeText(Login.this, _fullName + "\n" + _email + "\n" + _phoneNo + "\n" + _dataOfBirth, Toast.LENGTH_SHORT).show();

                        } else {

                            dialog.dismiss();
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Password does not match.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "No such user exist.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
    }



/*
    //Check Internet Connection
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the internet to proceed further.")
        .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                });
    }


    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn!=null && wifiConn.isConnected())||(mobileConn!=null&&mobileConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }
*/


    //validation
    private boolean validatePhoneNumber() {
        String val = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString().trim();
        String checkNumber = "[0-9]{10}";
        if (val.isEmpty()) {
            phoneNumber.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkNumber)) {
            phoneNumber.setError("Invalid Number");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

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

}