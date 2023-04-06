package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.ashrafapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class forgetotp extends AppCompatActivity {


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
        setContentView(R.layout.activity_forgetotp);


        pinFromUser = findViewById(R.id.pin_view);
        verify = findViewById(R.id.verifyCode);
        String _phoneNo = getIntent().getStringExtra("phoneNo");
        otpdialog = new ProgressDialog(this);
        otpdialog.setTitle("Sending Otp Please wait...");
        otpdialog.show();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Sending otp please wait...");

//        email = getIntent().getStringExtra("email");
//        userName = getIntent().getStringExtra("userName");
        phoneNo = getIntent().getStringExtra("phoneNo");


        sendVerificationCodeToUser(phoneNo);

        verify.setOnClickListener(view -> {
            String code = pinFromUser.getText().toString();
            if (!code.isEmpty() && !Objects.equals(codeBySystem, "")) {
                dialog.show();
                verifyCode(code);
            } else {
                Toast.makeText(this, "Failed to verify otp please re-send after 1 minute", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void sendVerificationCodeToUser(String phoneNo) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
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
                    Toast.makeText(forgetotp.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                    otpdialog.dismiss();
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);

                        Toast.makeText(forgetotp.this, "check " + "credential", Toast.LENGTH_LONG).show();
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    otpdialog.dismiss();
                    Toast.makeText(forgetotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };


    private void verifyCode(String code) {


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);

        Log.d("checkauth  ", codeBySystem + "  " + code + "  " + credential.getSmsCode() + "  " + credential.getSignInMethod());


        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            dialog.dismiss();

                            Intent go = new Intent(forgetotp.this, SetNewPassword.class);
                            go.putExtra("phone", phoneNo);
                            startActivity(go);
                            finish();

                        }else{
                            dialog.dismiss();
                            Toast.makeText(forgetotp.this, "failed to verify otp", Toast.LENGTH_SHORT).show();
                        }

                        }
                });

    }

}