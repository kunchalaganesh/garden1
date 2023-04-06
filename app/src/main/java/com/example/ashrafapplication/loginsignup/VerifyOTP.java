package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.ashrafapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    Button verify;
    PinView pinFromUser;
    String codeBySystem;
    TextView otpDescriptionText;
    String fullName, phoneNo, email, userName, password, date, gender, whatToDO, whatToDOCreate;
    private FirebaseAuth mAuth;

    ProgressDialog otpdialog, dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_otp);
        pinFromUser = findViewById(R.id.pin_view);
        verify = findViewById(R.id.verifyCode);
        String _phoneNo = getIntent().getStringExtra("phoneNo");
        otpdialog = new ProgressDialog(this);
        otpdialog.setTitle("Sending Otp Please wait...");
        otpdialog.show();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating account please wait...");

        otpDescriptionText = findViewById(R.id.otp_description_text);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Get all the data from Intent
        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        phoneNo = getIntent().getStringExtra("phoneNo");
        date = getIntent().getStringExtra("date");
        gender = getIntent().getStringExtra("gender");
        whatToDO = getIntent().getStringExtra("whatToDO");
        whatToDOCreate = getIntent().getStringExtra("whatToDOCreate");
        otpDescriptionText.setText("Enter One Time Password Sent On" + phoneNo);
        sendVerificationCodeToUser(phoneNo);

        verify.setOnClickListener(view -> {
            String code = pinFromUser.getText().toString();
            if (!code.isEmpty() && !Objects.equals(codeBySystem, "")) {
                dialog.show();
                verifyCode(code);
            }else{
                Toast.makeText(this, "Failed to verify otp please re-send after 1 minute", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(VerifyOTP.this, SuccessMessage.class);
//            startActivity(intent);

        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                    Toast.makeText(VerifyOTP.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                    otpdialog.dismiss();
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);

                        Toast.makeText(VerifyOTP.this, "check "+"credential", Toast.LENGTH_LONG).show();
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    otpdialog.dismiss();
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {



        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);

        Log.d("checkauth  ", codeBySystem+"  "+code+"  "+credential.getSmsCode()+"  "+credential.getSignInMethod());


        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        dialog.dismiss();

                        AuthCredential emailPasswordCredential = EmailAuthProvider.getCredential(email, password);

                        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(emailPasswordCredential)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                
                                                if(task.isSuccessful()){
//                                                    Toast.makeText(VerifyOTP.this, "lined accounts", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(VerifyOTP.this, "Verification Completed As New User. ", Toast.LENGTH_SHORT).show();
                                            storeNewUserData();
                                                }else{
                                                    Toast.makeText(VerifyOTP.this, "failed to link", Toast.LENGTH_SHORT).show();
                                                }
                                                
                                            }
                                        });

//
                    }
                    /* This is for forget password otp
                    else if (task.isSuccessful()) {
                        if (whatToDO != null && whatToDO.equals("updateData")) {
                            Toast.makeText(VerifyOTP.this, "Verification Completed For Forget Password. ", Toast.LENGTH_SHORT).show();
                            updateOldUsersData();
                        }
                    }*/
                     else {

                         dialog.dismiss();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(VerifyOTP.this, "Verification not completed! Try Again ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateOldUsersData() {

        Intent intent = new Intent(VerifyOTP.this, SetNewPassword.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
        finish();
    }

    private void storeNewUserData() {
        Intent intent = new Intent(VerifyOTP.this, SuccessMessage.class);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference().child("users");
//        UserHelperClass addNewUser = new UserHelperClass(fullName, userName, email, phoneNo, password, date, gender);
//        fullName, userName, email, phoneNo, password, date, gender
        HashMap<String, String> addNewUser = new HashMap<>();
        addNewUser.put("fullname", fullName);
        addNewUser.put("username", userName);
        addNewUser.put("email", email);
        addNewUser.put("phonenumber", phoneNo);
        addNewUser.put("password", password);
        addNewUser.put("dataofbirth", date);
        addNewUser.put("gender", gender);
        addNewUser.put("createdat", String.valueOf(System.currentTimeMillis()));

        FirebaseDatabase.getInstance().getReference()
                        .child("password")
                                .child(phoneNo)
                                        .child("password").setValue(password);
        FirebaseDatabase.getInstance().getReference().child("password")
                                .child(phoneNo)
                                        .child("email").setValue(email);


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).setValue(addNewUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(VerifyOTP.this, "account created successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(VerifyOTP.this, "Failed to create account"+task.getException().toString(), Toast.LENGTH_SHORT).show();

                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).removeValue();
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                }
                            }
                        });

    }

}