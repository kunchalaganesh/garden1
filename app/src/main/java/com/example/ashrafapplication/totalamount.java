package com.example.ashrafapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class totalamount extends AppCompatActivity {

    TextView total, advance, bal;
    int t=0;
    int a=0;
    int b=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalamount);


        total = findViewById(R.id.totalamount);
        advance = findViewById(R.id.totalpaid);
        bal = findViewById(R.id.balance);

        FirebaseDatabase.getInstance().getReference()
                .child("bookings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot snap : snapshot.getChildren()){

                                t = t+Integer.parseInt(Objects.requireNonNull(snap.child("dataTotalAmount").getValue(String.class)));
                                b = b+Integer.parseInt(Objects.requireNonNull(snap.child("dataRemainingBal").getValue(String.class)));
                                a = a+Integer.parseInt(Objects.requireNonNull(snap.child("dataAdvPaid").getValue(String.class)));


                            }

                            total.setText("Total amount of all bookings\n"+String.valueOf(t));

                            advance.setText("Advance amount of all bookings\n"+String.valueOf(a));

                            bal.setText("Balance amount of all bookings\n" + String.valueOf(b));



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







    }
}