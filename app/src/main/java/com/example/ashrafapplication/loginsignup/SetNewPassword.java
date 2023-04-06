package com.example.ashrafapplication.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashrafapplication.R;
import com.example.ashrafapplication.loginsignup.ForgetPasswordSuccessMessage;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SetNewPassword extends AppCompatActivity {

    //Variables
    private TextInputLayout newPassword, confirmPassword;
    private Button oKBtn;
    String phone;
    //RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //Hooks
        oKBtn = findViewById(R.id.set_new_password_btn);
        newPassword = findViewById(R.id.set_new_password);
        confirmPassword = findViewById(R.id.set_confirm_password);
        phone = getIntent().getStringExtra("phone");
        //progressBar = findViewById(R.id.forget_password_progress_bar);

        oKBtn.setOnClickListener(view -> {

            //Validate Password
            if (!validatePassword() | !validateConfirmPassword()) {
                return;
            }
            //progressBar.setVisibility(View.VISIBLE);
            //Get Data from fields


            FirebaseDatabase.getInstance().getReference()
                    .child("password")
                    .child(phone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                                String email = snapshot.child("email").getValue(String.class);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                String _newPassword = newPassword.getEditText().getText().toString().trim();
                                String _phoneNumber = getIntent().getStringExtra("phoneNo");

                                //Update data in firebase and in sessions

//            reference.child(_phoneNumber).child("password").setValue(_newPassword);
//                                reference.child(FirebaseAuth.getInstance().getUid()).child("password").setValue(_newPassword);


                                user.updatePassword(_newPassword);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                                reference.child(FirebaseAuth.getInstance().getUid())
                                        .child("password").setValue(_newPassword);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("password").child(phone)
                                        .child("password").setValue(_newPassword);

                                startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class));
                                finish();




                            }else{
                                Toast.makeText(SetNewPassword.this, "failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





        });
    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(newPassword.getEditText()).getText().toString().trim();
        String checkPassword = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            newPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            newPassword.setError("Password should contain 4 characters");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String val = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString().trim();
        String checkPassword = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (val.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            confirmPassword.setError("Password should contain 4 characters");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }
}