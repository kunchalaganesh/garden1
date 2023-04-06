package com.example.ashrafapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    TextView detailTitle, detailFull_name, detailPhone_number, detailEmail, detailHow_many_day, detailAcNonAc,
            detailBook_from, detailBook_to, detailTotal_amount, detailAdv_paid, detailRemaining_bal,
            detailAdv_received_by, detailBal_received_by, detailDecoration_amount, detailElectricity_bill,
            detailCleaning_charges, detailWaiter_amount, detailDrinking_water, detailAdditional,
            detailGeneratorCharges, detailDamageCharges, detailGuestHouse, detailVIPDine, detailFunctionType,
            detailHallAmount, detailAddress, detailPeopleCapacity, createdby, creadtedtime, updatedby, updatedtime;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    LinearLayout totall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailFull_name = findViewById(R.id.detailFull_name);
        detailPhone_number = findViewById(R.id.detailPhone_number);
        detailEmail = findViewById(R.id.detailEmail);
        detailHow_many_day = findViewById(R.id.detailHow_many_day);
        detailAcNonAc = findViewById(R.id.detailAcNonAc);
        detailBook_from = findViewById(R.id.detailBook_from);
        detailBook_to = findViewById(R.id.detailBook_to);
        detailTotal_amount = findViewById(R.id.detailTotal_amount);
        detailAdv_paid = findViewById(R.id.detailAdv_paid);
        detailRemaining_bal = findViewById(R.id.detailRemaining_bal);
        detailAdv_received_by = findViewById(R.id.detailAdv_received_by);
        detailBal_received_by = findViewById(R.id.detailBal_received_by);
        detailDecoration_amount = findViewById(R.id.detailDecoration_amount);
        detailElectricity_bill = findViewById(R.id.detailElectricity_bill);
        detailCleaning_charges = findViewById(R.id.detailCleaning_charges);
        detailWaiter_amount = findViewById(R.id.detailWaiter_amount);
        detailDrinking_water = findViewById(R.id.detailDrinking_water);
        detailAdditional = findViewById(R.id.detailAdditional);

        detailGeneratorCharges = findViewById(R.id.detailGeneratorCharges);
        detailDamageCharges = findViewById(R.id.detailDamageCharges);
        detailGuestHouse = findViewById(R.id.detailGuestHouse);
        detailVIPDine = findViewById(R.id.detailVIPDine);
        detailFunctionType = findViewById(R.id.detailFunctionType);
        detailHallAmount = findViewById(R.id.detailHallAmount);
        detailAddress = findViewById(R.id.detailAddress);
        detailPeopleCapacity = findViewById(R.id.detailPeopleCapacity);

        createdby = findViewById(R.id.createdby);
        creadtedtime = findViewById(R.id.createdtime);
        updatedby = findViewById(R.id.updatedby);
        updatedtime = findViewById(R.id.updatetime);

        totall = findViewById(R.id.totall);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailTitle.setText(bundle.getString("full_name"));
            detailFull_name.setText(bundle.getString("full_name"));
            detailPhone_number.setText(bundle.getString("phone_number"));
            detailEmail.setText(bundle.getString("email"));
            detailHow_many_day.setText(bundle.getString("how_many_day"));
            detailAcNonAc.setText(bundle.getString("acNonAc"));
            detailBook_from.setText(bundle.getString("book_from"));
            detailBook_to.setText(bundle.getString("book_to"));
            detailDecoration_amount.setText(bundle.getString("decoration_amount"));
            detailElectricity_bill.setText(bundle.getString("electricity_bill"));
            detailCleaning_charges.setText(bundle.getString("cleaning_charges"));
            detailWaiter_amount.setText(bundle.getString("waiter_amount"));
            detailDrinking_water.setText(bundle.getString("drinking_water"));
            detailTotal_amount.setText(bundle.getString("total_amount"));
            detailAdv_paid.setText(bundle.getString("adv_paid"));
            detailRemaining_bal.setText(bundle.getString("remaining_bal"));
            detailAdv_received_by.setText(bundle.getString("adv_received_by"));
            detailBal_received_by.setText(bundle.getString("bal_received_by"));
            detailAdditional.setText(bundle.getString("additional"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");


            if(Integer.parseInt(detailTotal_amount.getText().toString())!= Integer.parseInt(detailRemaining_bal.getText().toString())
            +Integer.parseInt(detailAdv_paid.getText().toString())){
                totall.setBackgroundColor(getResources().getColor(R.color.red));
            }else{
                totall.setBackgroundColor(getResources().getColor(R.color.green));
            }



            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
            if(bundle.getString("createdtime") != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dateString = simpleDateFormat.format(Long.parseLong(bundle.getString("createdtime")));
                creadtedtime.setText(dateString);
            }

            if(bundle.getString("createdby")!= null){
                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(bundle.getString("createdby"))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String name = snapshot.child("fullname").getValue(String.class);
                                createdby.setText(name);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
if(bundle.getString("updatedtime") != null && !bundle.getString("updatedtime").matches("")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dateString = simpleDateFormat.format(Long.parseLong(bundle.getString("updatedtime")));
                updatedtime.setText(dateString);
            }

            Log.d("imagetag", bundle.getString("updatedby"));
            if(bundle.getString("updatedby")!= null){
                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(bundle.getString("updatedby"))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()) {
                                    String name = snapshot.child("fullname").getValue(String.class);
                                    updatedby.setText(name);

                                }else {

                                    updatedby.setText("No one updated");
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                updatedby.setText("No one updated");
                            }
                        });


            }else{
                updatedby.setText("No one updated");
            }


            detailGeneratorCharges.setText(bundle.getString("generator_charges"));
            detailDamageCharges.setText(bundle.getString("damage_charges"));
            detailGuestHouse.setText(bundle.getString("guest_house"));
            detailVIPDine.setText(bundle.getString("VIP_dine"));
            detailFunctionType.setText(bundle.getString("function_type"));
            detailHallAmount.setText(bundle.getString("hall_amount"));
            detailAddress.setText(bundle.getString("address"));
            detailPeopleCapacity.setText(bundle.getString("people_capacity"));
        }
        deleteButton.setOnClickListener(view -> {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bookings");

            if(imageUrl != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(unused -> {
                    reference.child(key).removeValue();

                    List<Date> dates = getDates(detailBook_from.getText().toString(), detailBook_to.getText().toString());
                    for (Date date : dates) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

// Format the Date object as a string in the desired format
                        String formattedDate = sdf.format(date);


                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("messages")
                                .child(formattedDate)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("calendar")
                                                    .child("alldates")
                                                    .child(formattedDate)
                                                    .removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("messages")
                                .child(formattedDate)
                                .child(detailPhone_number.getText().toString())
                                .removeValue();


                    }


                    Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), EnterCustomerData.class));
                    finish();
                });

            }else{

                reference.child(key).removeValue();

                List<Date> dates = getDates(detailBook_from.getText().toString(), detailBook_to.getText().toString());
                for (Date date : dates) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

// Format the Date object as a string in the desired format
                    String formattedDate = sdf.format(date);


                    FirebaseDatabase.getInstance().getReference()
                            .child("calendar")
                            .child("messages")
                            .child(formattedDate)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("calendar")
                                                .child("alldates")
                                                .child(formattedDate)
                                                .removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    FirebaseDatabase.getInstance().getReference()
                            .child("calendar")
                            .child("messages")
                            .child(formattedDate)
                            .child(detailPhone_number.getText().toString())
                            .removeValue();


                }


                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), EnterCustomerData.class));
                finish();


            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, UpdateDetailsActivity.class)
                        .putExtra("full_name", detailFull_name.getText().toString().trim())
                        .putExtra("phone_number", detailPhone_number.getText().toString())
                        .putExtra("email", detailEmail.getText().toString())
                        .putExtra("how_many_day", detailHow_many_day.getText().toString())
                        .putExtra("acNonAc", detailAcNonAc.getText().toString())
                        .putExtra("book_from", detailBook_from.getText().toString())
                        .putExtra("book_to", detailBook_to.getText().toString())
                        .putExtra("total_amount", detailTotal_amount.getText().toString())
                        .putExtra("adv_paid", detailAdv_paid.getText().toString())
                        .putExtra("remaining_bal", detailRemaining_bal.getText().toString())
                        .putExtra("adv_received_by", detailAdv_received_by.getText().toString())
                        .putExtra("bal_received_by", detailBal_received_by.getText().toString())
                        .putExtra("decoration_amount", detailDecoration_amount.getText().toString())
                        .putExtra("electricity_bill", detailElectricity_bill.getText().toString())
                        .putExtra("cleaning_charges", detailCleaning_charges.getText().toString())
                        .putExtra("waiter_amount", detailWaiter_amount.getText().toString())
                        .putExtra("drinking_water", detailDrinking_water.getText().toString())
                        .putExtra("additional", detailAdditional.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("generator_charges", detailGeneratorCharges.getText().toString())
                        .putExtra("damage_charges", detailDamageCharges.getText().toString())
                        .putExtra("guest_house", detailGuestHouse.getText().toString())
                        .putExtra("VIP_dine", detailVIPDine.getText().toString())
                        .putExtra("function_type", detailFunctionType.getText().toString())
                        .putExtra("hall_amount", detailHallAmount.getText().toString())
                        .putExtra("address", detailAddress.getText().toString())
                        .putExtra("people_capacity", detailPeopleCapacity.getText().toString())
                        .putExtra("key", key)
                        ;
                startActivity(intent);
            }
        });




    }


    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

}