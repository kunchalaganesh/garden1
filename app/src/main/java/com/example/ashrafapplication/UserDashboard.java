package com.example.ashrafapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserDashboard extends AppCompatActivity {
    ImageView backBtn, add;
    LinearLayout calender, p, din, hall, gar;
    Button upload;

    String url1 = "https://images.unsplash.com/photo-1617854818583-09e7f077a156?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
    String url2 = "https://images.unsplash.com/photo-1517404215738-15263e9f9178?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80";
    String url3 = "https://images.unsplash.com/photo-1598128558393-70ff21433be0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=489&q=80";

    private CalendarView calendarView;
    String allbook;
    ProgressDialog mdialog;
    List<String> imageUrls;
    RecyclerView recyclerView;
    ProgressDialog ldialog;
//    private List<LocalDate> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);
        backBtn = findViewById(R.id.dash_back_btn);
        //add = findViewById(R.id.dash_add_details);


        calendarView = findViewById(R.id.calendarView);
//        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        calender = findViewById(R.id.viewcal);
        ldialog = new ProgressDialog(this);
        ldialog.setTitle("Loading please wait...");

        p = findViewById(R.id.profile);
        mdialog = new ProgressDialog(this);
        din = findViewById(R.id.dbtn);
        hall = findViewById(R.id.hbtn);
        gar = findViewById(R.id.gbtn);
        imageUrls =  new ArrayList<>();
//        AndroidThreeTen.init(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Set the number of columns in the grid




        din.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUrls.clear();
                recyclerView.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference()
                        .child("images")
                        .child("dinning")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    for(DataSnapshot snap : snapshot.getChildren()){

                                        String im = snap.child("image").getValue(String.class);

                                        imageUrls.add(im);

                                    }
                                    ImageAdapter adapter = new ImageAdapter(UserDashboard.this, imageUrls);
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });

        hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ldialog.show();
                imageUrls.clear();
                recyclerView.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference()
                        .child("images")
                        .child("hall")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    for(DataSnapshot snap : snapshot.getChildren()){

                                        String im = snap.child("image").getValue(String.class);

                                        imageUrls.add(im);

                                    }
                                    ImageAdapter adapter = new ImageAdapter(UserDashboard.this, imageUrls);
                                    recyclerView.setAdapter(adapter);

                                }
                                ldialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });

        gar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ldialog.show();
                imageUrls.clear();
                recyclerView.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference()
                        .child("images")
                        .child("garden")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    for(DataSnapshot snap : snapshot.getChildren()){

                                        String im = snap.child("image").getValue(String.class);

                                        imageUrls.add(im);

                                    }
                                    ImageAdapter adapter = new ImageAdapter(UserDashboard.this, imageUrls);
                                    recyclerView.setAdapter(adapter);
                                }
                                ldialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });





        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent go = new Intent(UserDashboard.this, profile.class);
                startActivity(go);


            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    return;
                }

                recyclerView.setVisibility(View.GONE);
                calendarView.setVisibility(View.VISIBLE);


                List<EventDay> events = new ArrayList<>();
////                Calendar calendar = Calendar.getInstance();
                ArrayList<String> dates = new ArrayList<>();


                FirebaseDatabase.getInstance().getReference()
                        .child("calendar")
                        .child("alldates")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    ldialog.show();
                                    dates.clear();
                                    events.clear();
                                    for (DataSnapshot snap : snapshot.getChildren()) {

                                        String date1 = snap.child("date").getValue(String.class);
//                                        String date2 = snap.child("dataToDate").getValue(String.class);
                                        dates.add(date1);

//                                        String str_date = "13-9-2011";
//                                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                                        Date d1 = null, d2 = null;
//                                        try {
//                                            d1 = (Date) formatter.parse(date1);
//                                            d2 = (Date) formatter.parse(date2);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        Calendar start = Calendar.getInstance();
//                                        start.setTimeInMillis(d1.getTime());
//                                        Calendar end = Calendar.getInstance();
//                                        end.setTimeInMillis(d2.getTime());

//


// Highlight the dates between the start and end dates


                                    }

                                    for (String date : dates) {


                                        Calendar calendar = Calendar.getInstance(); // calendar must be here
                                        String[] items1 = date.split("-");
                                        int day = Integer.parseInt(items1[0]);
                                        int month = Integer.parseInt(items1[1]);
                                        int year = Integer.parseInt(items1[2]);

                                        calendar.set(year, month - 1, day);
                                        events.add(new EventDay(calendar, R.drawable.samplecircle));


//                                        calendar.set(Integer.parseInt(yr.format(date1)), Integer.parseInt(mn.format(date1)), Integer.parseInt(day.format(date1)));
//                                        events.add(new EventDay(calendar, R.drawable.samplecircle));


                                        Log.d("dates", "check1  " + events.size());

                                    }
                                    calendarView.setEvents(events);


                                }

                                ldialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                calendarView.setOnDayClickListener(new OnDayClickListener() {
                    @Override
                    public void onDayClick(EventDay eventDay) {
                        Calendar clickedDayCalendar = eventDay.getCalendar();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDate = sdf.format(clickedDayCalendar.getTime());

//                        Toast.makeText(UserDashboard.this, currentDate, Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("messages")
                                .child(currentDate)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        allbook = "";

                                        for (DataSnapshot snap : snapshot.getChildren()) {

                                            String mess = snap.child("message").getValue(String.class);

                                            allbook = mess + "\n" + allbook;
//                                            Toast.makeText(UserDashboard.this, allbook, Toast.LENGTH_SHORT).show();
                                        }
//                                        mdialog.setIndeterminate(false);
                                        mdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        mdialog.setMessage(allbook);
                                        mdialog.setMax(100);
//                                        mdialog.prog
                                        mdialog.show();
                                        int progress = 0;
                                        while (progress < 100) {
                                            // do some work
                                            progress += 10; // update progress value

                                            // update progress bar
                                            mdialog.setProgress(progress);

                                            // add a delay to simulate work being done
                                            try {
                                                Thread.sleep(1);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }
                });


            }
        });



        upload = findViewById(R.id.dash_book_garden);
        upload.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboard.this, EnterCustomerData.class);
            startActivity(intent);
        });


        slider();

    }

    private void slider() {

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.imageSlider);


        FirebaseDatabase.getInstance().getReference()
                .child("images")
                .child("auto")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            sliderDataArrayList.clear();

                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Log.d("checkauto ", "check1" + snap.child("image").getValue(String.class));

                                sliderDataArrayList.add(new SliderData(snap.child("image").getValue(String.class)));

                            }

                            // passing this array list inside our adapter class.
                            SliderAdapter adapter = new SliderAdapter(UserDashboard.this, sliderDataArrayList);

                            // below method is used to set auto cycle direction in left to
                            // right direction you can change according to requirement.
                            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                            // below method is used to
                            // setadapter to sliderview.
                            sliderView.setSliderAdapter(adapter);

                            // below method is use to set
                            // scroll time in seconds.
                            sliderView.setScrollTimeInSec(3);

                            // to set it scrollable automatically
                            // we use below method.
                            sliderView.setAutoCycle(true);

                            // to start autocycle below method is used.
                            sliderView.startAutoCycle();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if (sliderDataArrayList.size() < 0) {

        } else {


            // adding the urls inside array list
//        sliderDataArrayList.add(new SliderData(url1));
//        sliderDataArrayList.add(new SliderData(url2));
//        sliderDataArrayList.add(new SliderData(url3));


        }

    }


    @Override
    public void onBackPressed() {
        if (mdialog.isShowing()) {
            mdialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}