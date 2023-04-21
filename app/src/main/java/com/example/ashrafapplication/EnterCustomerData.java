package com.example.ashrafapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnterCustomerData extends AppCompatActivity {

    ImageView backBtn, add, upload;
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    SearchView searchView;
    Button previous, upcoming;
    String sdate = "";
    ArrayList<DataClass> ndatalist, sdatalist;
    TextView total, advance, bal, discount;
    public static int sttotal = 0;
    public static int stbal = 0;
    public static int stadv = 0;
    ImageView delete;
    public static String type = "read";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_customer_data);
        //backBtn = findViewById(R.id.dash_back_btn);
        //add = findViewById(R.id.dash_add_details);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        previous = findViewById(R.id.previous);
        upcoming = findViewById(R.id.upcoming);
        ndatalist = new ArrayList<>();
        sdatalist = new ArrayList<>();

        total = findViewById(R.id.totalamount);
        advance = findViewById(R.id.totalpaid);
        bal = findViewById(R.id.balance);
        discount = findViewById(R.id.discount);
        delete = findViewById(R.id.delete);




        delete.setBackground(ContextCompat.getDrawable(EnterCustomerData.this, R.drawable.ic_baseline_delete));

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                previous.setBackgroundColor(getResources().getColor(R.color.gray));
                upcoming.setBackgroundColor(getResources().getColor(R.color.black));
                delete.setBackground(ContextCompat.getDrawable(EnterCustomerData.this, R.drawable.ic_baseline_delete));

                type = "read";
                sttotal = 0;
                stbal = 0;
                stadv = 0;
                sdate = "previous";


//                ArrayList<DataClass> searchList = new ArrayList<>();
////                searchList.addAll(dataList);
//                ndatalist.clear();
//                ndatalist.addAll(dataList);
                adapter.searchDataList(ndatalist, sdate);
//                adapter.searchDataList(ndatalist);
//                adapter = new MyAdapter(EnterCustomerData.this, ndatalist, sdate);
                adapter.notifyDataSetChanged();


                for (int i = 0; i < ndatalist.size(); i++) {


                    if(ndatalist.get(i).getDataTotalAmount().matches("")){
                        sttotal = 0;
                    }else {

                        sttotal = sttotal + Integer.parseInt(ndatalist.get(i).getDataTotalAmount());
                    }
                    if(ndatalist.get(i).getDataRemainingBal() == null || ndatalist.get(i).getDataRemainingBal().matches("")){
                        stbal = 0;
                    }else {
                        stbal = stbal + Integer.parseInt(ndatalist.get(i).getDataRemainingBal());

                    }


                    if(ndatalist.get(i).getDataAdvPaid().matches("")){
                        stadv = 0;
                    }else {
                        stadv = stadv + Integer.parseInt(ndatalist.get(i).getDataAdvPaid());
                    }

//                    sttotal = sttotal + Integer.parseInt(ndatalist.get(i).getDataTotalAmount());
//                    stbal = stbal + Integer.parseInt(ndatalist.get(i).getDataRemainingBal());
//                    stadv = stadv + Integer.parseInt(ndatalist.get(i).getDataAdvPaid());


                    Log.d("prevbal", ndatalist.get(i).getDataRemainingBal()+"  "+stbal);
                }


                discount.setText("Discount "+String.valueOf(sttotal - (stadv+stbal)));
                total.setText("Total amount " + String.valueOf(sttotal));
                bal.setText("Balance amount " + String.valueOf(stbal));
                advance.setText("Advance amount " + String.valueOf(stadv));


            }
        });
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                previous.setBackgroundColor(getResources().getColor(R.color.black));
                upcoming.setBackgroundColor(getResources().getColor(R.color.gray));
                delete.setBackground(ContextCompat.getDrawable(EnterCustomerData.this, R.drawable.ic_baseline_delete));

                type = "read";
                sdate = "upcoming";
                sttotal = 0;
                stbal = 0;
                stadv = 0;
//                adapter.searchDataList(ndatalist);
                adapter.searchDataList(sdatalist, sdate);
//                adapter.searchDataList(ndatalist);
//                adapter = new MyAdapter(EnterCustomerData.this, ndatalist, sdate);
                adapter.notifyDataSetChanged();


                for (int i = 0; i < sdatalist.size(); i++) {


                    if(sdatalist.get(i).getDataTotalAmount().matches("")){
                        sttotal = 0;
                    }else {

                        sttotal = sttotal + Integer.parseInt(sdatalist.get(i).getDataTotalAmount());
                    }
                    if(sdatalist.get(i).getDataRemainingBal().matches("")){
                        stbal = 0;
                    }else {
                        stbal = stbal + Integer.parseInt(sdatalist.get(i).getDataRemainingBal());
                    }
                    if(sdatalist.get(i).getDataAdvPaid().matches("")){
                        stadv = 0;
                    }else {
                        stadv = stadv + Integer.parseInt(sdatalist.get(i).getDataAdvPaid());
                    }

//                    sttotal = sttotal + Integer.parseInt(sdatalist.get(i).getDataTotalAmount());
//                    stbal = stbal + Integer.parseInt(sdatalist.get(i).getDataRemainingBal());
//                    stadv = stadv + Integer.parseInt(sdatalist.get(i).getDataAdvPaid());


                }

                discount.setText("Discount "+String.valueOf(sttotal - (stadv+stbal)));
                total.setText("Total amount " + String.valueOf(sttotal));
                bal.setText("Balance amount " + String.valueOf(stbal));
                advance.setText("Advance amount " + String.valueOf(stadv));



            }
        });


        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EnterCustomerData.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(EnterCustomerData.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dataList = new ArrayList<>();
        adapter = new MyAdapter(EnterCustomerData.this, dataList, sdate, type);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        dialog.show();

        eventListener =

                databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                ndatalist.clear();
                sdatalist.clear();
//                sttotal = 0;
                sttotal = 0;
                stbal = 0;
                stadv = 0;
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    //assert dataClass != null;//Check Here Auto generated

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = dateFormat.parse(dataClass.getDataFromDate());
                        long timestamp = date.getTime();

                        long currentTimestamp = System.currentTimeMillis();
                        if (currentTimestamp > timestamp) {

                            dataClass.setKey(itemSnapshot.getKey());
                            ndatalist.add(dataClass);
                        } else {
                            dataClass.setKey(itemSnapshot.getKey());
                            sdatalist.add(dataClass);
                        }
                    } catch (Exception e) {

                    }
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);


                    if(dataClass.getDataTotalAmount().matches("")){
                        sttotal = 0;
                    }else {

                        sttotal = sttotal + Integer.parseInt(dataClass.getDataTotalAmount());
                    }
                    if(dataClass.getDataRemainingBal().matches("")){
                        stbal = 0;
                    }else {
                        stbal = stbal + Integer.parseInt(dataClass.getDataRemainingBal());
                    }
                    if(dataClass.getDataAdvPaid().matches("")){
                        stadv = 0;
                    }else {
                        stadv = stadv + Integer.parseInt(dataClass.getDataAdvPaid());
                    }

//                    sttotal = sttotal + Integer.parseInt(dataClass.getDataTotalAmount());
//                    stbal = stbal + Integer.parseInt(dataClass.getDataRemainingBal());
//                    stadv = stadv + Integer.parseInt(dataClass.getDataAdvPaid());


                }

                discount.setText("Discount "+String.valueOf(sttotal - (stadv+stbal)));
                total.setText("Total amount " + String.valueOf(sttotal));
                bal.setText("Balance amount " + String.valueOf(stbal));
                advance.setText("Advance amount " + String.valueOf(stadv));


//                ndatalist.addAll(dataList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });



        //delete click
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                previous.setBackgroundColor(getResources().getColor(R.color.black));
                upcoming.setBackgroundColor(getResources().getColor(R.color.black));
                delete.setBackground(ContextCompat.getDrawable(EnterCustomerData.this, R.drawable.ic_baseline_delete1));


                databaseReference = FirebaseDatabase.getInstance().getReference("deleted");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataList.clear();
                        ndatalist.clear();
                        sdatalist.clear();
//                sttotal = 0;
                        sttotal = 0;
                        stbal = 0;
                        stadv = 0;
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                            //assert dataClass != null;//Check Here Auto generated

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date date = dateFormat.parse(dataClass.getDataFromDate());
                                long timestamp = date.getTime();

                                long currentTimestamp = System.currentTimeMillis();
                                if (currentTimestamp > timestamp) {

                                    dataClass.setKey(itemSnapshot.getKey());
                                    ndatalist.add(dataClass);
                                } else {
                                    dataClass.setKey(itemSnapshot.getKey());
                                    sdatalist.add(dataClass);
                                }
                            } catch (Exception e) {

                            }
                            dataClass.setKey(itemSnapshot.getKey());
                            dataList.add(dataClass);


                            if(dataClass.getDataTotalAmount().matches("")){
                                sttotal = 0;
                            }else {

                                sttotal = sttotal + Integer.parseInt(dataClass.getDataTotalAmount());
                            }
                            if(dataClass.getDataRemainingBal().matches("")){
                                stbal = 0;
                            }else {
                                stbal = stbal + Integer.parseInt(dataClass.getDataRemainingBal());
                            }
                            if(dataClass.getDataAdvPaid().matches("")){
                                stadv = 0;
                            }else {
                                stadv = stadv + Integer.parseInt(dataClass.getDataAdvPaid());
                            }

//                    sttotal = sttotal + Integer.parseInt(dataClass.getDataTotalAmount());
//                    stbal = stbal + Integer.parseInt(dataClass.getDataRemainingBal());
//                    stadv = stadv + Integer.parseInt(dataClass.getDataAdvPaid());


                        }

                        discount.setText("Discount "+String.valueOf(sttotal - (stadv+stbal)));
                        total.setText("Total amount " + String.valueOf(sttotal));
                        bal.setText("Balance amount " + String.valueOf(stbal));
                        advance.setText("Advance amount " + String.valueOf(stadv));

                        type = "delete";
//                        adapter = new MyAdapter(EnterCustomerData.this, dataList, sdate, type);

//                ndatalist.addAll(dataList);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                    }
                });



            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(EnterCustomerData.this, UploadActivity.class);
            startActivity(intent);
        });

    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        searchList.clear();

        if (sdate.matches("")) {


            for (DataClass dataClass : dataList) {
                if (dataClass.getDataFullName().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(dataClass);
                }
            }
            adapter.searchDataList(searchList, sdate);

        } else if (sdate.matches("previous")) {
            for (DataClass dataClass : ndatalist) {
                if (dataClass.getDataFullName().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(dataClass);
                }
            }
            adapter.searchDataList(searchList, sdate);
        } else {
            for (DataClass dataClass : sdatalist) {
                if (dataClass.getDataFullName().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(dataClass);
                }
            }
            adapter.searchDataList(searchList, sdate);
        }
    }
}