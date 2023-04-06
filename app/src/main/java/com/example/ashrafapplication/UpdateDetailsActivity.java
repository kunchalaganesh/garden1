package com.example.ashrafapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UpdateDetailsActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateDesc;
    TextInputLayout updateFullName, updatePhoneNumber, updateEmail, updateHowManyDays,
            updateTotalAmount, updateAdvPaid, updateRemainingBal, updateAdvReceivedBy, updateBalReceivedBy,
            updateDecorationAmount, updateElectricityBill, updateCleaningCharges, updateWaiterAmount,
            updateDrinkingWaterAmount, update_book_from_picker, update_book_to_picker, updateAcNonAc,
            updateGeneratorCharges, updateDamageCharges, updateGuestHouse, updateVIPDine, updateFunctionType,
            updateHallAmount, updateAddress, updatePeopleCapacity, uploadhall, uploadreferred;;
    String _FULLNAME, _PHONENUMBER, _EMAIL, _HOWMANYDAYS, _ACNONAC, _BOOK_FROM_PICKER, _BOOK_TO_PICKER, _TOTALAMOUNT, _ADVPAID,
            _REMAININGBAL, _ADVRECEIVEDBY, _BALRECEIVEDBY, _DECORATIONAMOUNT, _ELECTRICITYBILL, _CLEANINGCHARGES, _WAITERAMOUNT,
            _DRINKINGWATERAMOUNT, _DESC,
            _GENERATORCHARGES, _DAMAGECHARGES, _GUESTHOUSE, _VIPDINE, _FUNCTIONTYPE,
            _HALLAMOUNT, _ADDRESS, _PEOPLECAPACITY, _KEY, _IMAGE;
    /*String fullName, phoneNumber, email, howManyDays, acNonAc, book_from_picker, book_to_picker, totalAmount, advPaid,
            remainingBal, advReceivedBy, balReceivedBy, decorationAmount, electricityBill, cleaningCharges, waiterAmount,
            drinkingWaterAmount, desc;*/
    //RadioButton updateAcNonAc;
    RadioGroup update_booking_radio_group;
    //DatePicker update_book_from_picker, update_book_to_picker;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    RadioButton genderradioButton;
    RadioGroup radioGroup;
    TextView bookfrom, bookto;
    ImageView uploadImage;
    String imageURL, oldnum, oldsdate, oldtdate;

    int hallamt, decoramt, eleamt, clnamt, dmgamt, genamt, waiamt, drnamt;
    int a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        updateButton = findViewById(R.id.updateButton);
        //updateImage = findViewById(R.id.updateImage);
        updateFullName = findViewById(R.id.updateFullName);
        updatePhoneNumber = findViewById(R.id.updatePhoneNumber);
        updateEmail = findViewById(R.id.updateEmail);
        updateHowManyDays = findViewById(R.id.updateHowManyDays);
//        updateAcNonAc = findViewById(R.id.updateAcNonAc);
        //update_booking_radio_group = findViewById(R.id.update_booking_radio_group);
//        update_book_from_picker = findViewById(R.id.update_book_from_picker);
//        update_book_to_picker = findViewById(R.id.update_book_to_picker);
        updateDecorationAmount = findViewById(R.id.updateDecorationAmount);
        updateElectricityBill = findViewById(R.id.updateElectricityBill);
        updateCleaningCharges = findViewById(R.id.updateCleaningCharges);
        updateWaiterAmount = findViewById(R.id.updateWaiterAmount);
        updateDrinkingWaterAmount = findViewById(R.id.updateDrinkingWaterAmount);
        updateTotalAmount = findViewById(R.id.updateTotalAmount);
        updateAdvPaid = findViewById(R.id.updateAdvPaid);
        updateRemainingBal = findViewById(R.id.updateRemainingBal);
        updateAdvReceivedBy = findViewById(R.id.updateAdvReceivedBy);
        updateBalReceivedBy = findViewById(R.id.updateBalReceivedBy);
        updateDesc = findViewById(R.id.updateDesc);

        updateGeneratorCharges = findViewById(R.id.updateGeneratorCharges);
        updateDamageCharges = findViewById(R.id.updateDamageCharges);
        updateGuestHouse = findViewById(R.id.updateGuestHouse);
        updateVIPDine = findViewById(R.id.updateVIPDine);
        updateFunctionType = findViewById(R.id.updateFunctionType);
        updateHallAmount = findViewById(R.id.updateHallAmount);
        updateAddress = findViewById(R.id.updateAddress);
        updatePeopleCapacity = findViewById(R.id.updatePeopleCapacity);
        key = getIntent().getStringExtra("key");

        bookfrom = findViewById(R.id.bookfrom);
        bookto = findViewById(R.id.bookto);

        uploadhall = findViewById(R.id.uploadhallamount);
        uploadreferred = findViewById(R.id.uploadreferredby);
        uploadImage = findViewById(R.id.uploadImage);


        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        if (getIntent().getStringExtra("key") != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("bookings");//.child(getIntent().getStringExtra("key"));
        }

        //Show All User Data
        showAllUserData();

        bookfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        UpdateDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                bookfrom.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();

            }
        });

        bookto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        UpdateDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                bookto.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();


            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("bookimages")
                                    .child(FirebaseAuth.getInstance().getUid().toString()).child(String.valueOf(System.currentTimeMillis()));
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDetailsActivity.this);
                            builder.setCancelable(false);
                            builder.setView(R.layout.progress_layout);
                            AlertDialog dialog = builder.create();
//        TextView pt = dialog.findViewById(R.id.progresstext);
//        pt.setText("Uploading image please wait");
                            dialog.show();
                            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isComplete()) ;
                                    Uri urlImage = uriTask.getResult();
                                    imageURL = urlImage.toString();
//                if(imageURL!= null){
//                    imageuploaded = true;
//                }
//                                    uploadData();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                }
                            });






                        } else {
                            Toast.makeText(UpdateDetailsActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });



        updateButton.setOnClickListener(view -> {
            if (isFullNameChanged() | isPhoneNumberChanged() | isEmailChanged() | isHowManyDaysChanged() | isAcNonAcChanged() |
                    isFromDateChanged() | isToDateChanged() |
                    isDecorationAmountChanged() | isElectricityBillChanged() | isCleaningChargesChanged() |
                    isWaiterAmountChanged() | isDrinkingAmountChanged() | isTotalAmountChanged() | isAdvPaidChanged() | isRemainingBalChanged() |
                    isAdvReceivedByChanged() | BalReceivedByChanged() | isDescChanged() | isGeneratorCharges() |
                    isDamageCharges() | isGuestHouse() | isVIPDine() | isFunctionType() | isHallAmount() | isAddress() |
                    isPeopleCapacity() | isImage()
                //||isImageChanged()
            ) {
                Toast.makeText(UpdateDetailsActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();

                databaseReference.child(key).child("updatedby").setValue(FirebaseAuth.getInstance().getUid());

                databaseReference.child(key).child("updatedtime").setValue(String.valueOf(System.currentTimeMillis()));

                if(!oldsdate.matches(bookfrom.getText().toString()) || !oldtdate.matches(bookto.getText().toString())) {



                    List<Date> dates1 = getDates(oldsdate, oldtdate);
                    for (Date date : dates1) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

// Format the Date object as a string in the desired format
                        String formattedDate = sdf.format(date);

//                        FirebaseDatabase.getInstance().getReference()
//                                .child("calendar")
//                                .child("alldates")
//                                .child(formattedDate)
//                                .child("date")
//                                .setValue(formattedDate);

                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("messages")
                                .child(formattedDate)
                                .child(oldnum)
                                .removeValue();



//                        Log.d("updatedate", formattedDate);
                    }

                    List<Date> dates = getDates(bookfrom.getText().toString(), bookto.getText().toString());
                    for (Date date : dates) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

// Format the Date object as a string in the desired format
                        String formattedDate = sdf.format(date);

                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("alldates")
                                .child(formattedDate)
                                .child("date")
                                .setValue(formattedDate);

                        FirebaseDatabase.getInstance().getReference()
                                .child("calendar")
                                .child("messages")
                                .child(formattedDate)
                                .child(updatePhoneNumber.getEditText().getText().toString())
                                .child("message")
                                .setValue(updateFullName.getEditText().getText().toString()+" "+updatePhoneNumber.getEditText().getText().toString());



//                        Log.d("updatedate", formattedDate);
                    }

                    for (Date date : dates1) {
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
                                        if(!snapshot.exists()){
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


                    }


                }








            } else {
                Toast.makeText(UpdateDetailsActivity.this, "Data is same and cannot be updated", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(UpdateDetailsActivity.this, EnterCustomerData.class);
            startActivity(intent);
            finish();
        });

        /**

        updateTotalAmount.getEditText()
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if(!updateTotalAmount.getEditText().getText().toString().matches("")
                                && !updateAdvPaid.getEditText().getText().toString().matches("")){
                            int a = Integer.parseInt(updateTotalAmount.getEditText().getText().toString())
                                    - Integer.parseInt(updateAdvPaid.getEditText().getText().toString());
                            updateRemainingBal.getEditText().setText(String.valueOf(a));


                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {




                    }
                });

        updateAdvPaid.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!updateTotalAmount.getEditText().getText().toString().matches("")
                        && !updateAdvPaid.getEditText().getText().toString().matches("")){
                    int a = Integer.parseInt(updateTotalAmount.getEditText().getText().toString())
                            - Integer.parseInt(updateAdvPaid.getEditText().getText().toString());
                    updateRemainingBal.getEditText().setText(String.valueOf(a));


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

         **/

        updateTotalAmount.getEditText()
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        if(!updateTotalAmount.getEditText().getText().toString().matches("")){
                            a =  Integer.parseInt(updateTotalAmount.getEditText().getText().toString());
                        }else{
                            a =0;
                        }

                        if (!updateAdvPaid.getEditText().getText().toString().matches("")) {
                            b =
                                    Integer.parseInt(updateAdvPaid.getEditText().getText().toString());
                        }else {

                            b= 0;
                        }
                        if(!updateRemainingBal.getEditText().getText().toString().matches(""))
                        {
                            c = Integer.parseInt(updateRemainingBal.getEditText().getText().toString());
                        }else{
                            c =0;
                        }
                        updateRemainingBal.getEditText().setText(String.valueOf(a-b));


//                            if(Integer.parseInt(uploadTotalAmount.getEditText().getText().toString()) ==
//                                    Integer.parseInt(uploadAdvPaid.getEditText().getText().toString())+
//                                            Integer.parseInt(uploadRemainingBal.getEditText().getText().toString())){
//
//
//                                uploadTotalAmount.setBoxBackgroundColor(ContextCompat.getColor(UploadActivity.this, R.color.green));
//
//
//                            }else{
//                                uploadTotalAmount.setBoxBackgroundColor(ContextCompat.getColor(UploadActivity.this, R.color.red));
//                            }



//                        if(upload)


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                    }
                });

        updateAdvPaid.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!updateTotalAmount.getEditText().getText().toString().matches("")){
                    a =  Integer.parseInt(updateTotalAmount.getEditText().getText().toString());
                }else{
                    a =0;
                }

                if (!updateAdvPaid.getEditText().getText().toString().matches("")) {
                    b =
                            Integer.parseInt(updateAdvPaid.getEditText().getText().toString());
                }else {

                    b= 0;
                }
                if(!updateRemainingBal.getEditText().getText().toString().matches(""))
                {
                    c = Integer.parseInt(updateRemainingBal.getEditText().getText().toString());
                }else{
                    c =0;
                }
                updateRemainingBal.getEditText().setText(String.valueOf(a-b));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadhall.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateDecorationAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateElectricityBill.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateCleaningCharges.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateDamageCharges.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateGeneratorCharges.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateWaiterAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateDrinkingWaterAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!uploadhall.getEditText().getText().toString().matches("")) {
                    hallamt = Integer.parseInt(Objects.requireNonNull(uploadhall.getEditText()).getText().toString());

                }else{
                    hallamt = 0;
                }



                if
                (!updateDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(updateDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !updateElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(updateElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!updateCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(updateCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !updateDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(updateDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !updateGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(updateGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !updateWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(updateWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !updateDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(updateDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                updateTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    private boolean isImage() {


        if(imageURL!= null ) {
                databaseReference.child(key).child("dataImage").setValue(imageURL);
                _IMAGE = imageURL;
                return true;

        }else{
            return false;
        }


    }


    private void showAllUserData() {
        Intent intent = getIntent();
        _FULLNAME = intent.getStringExtra("full_name");
        _PHONENUMBER = intent.getStringExtra("phone_number");
        oldnum = _PHONENUMBER;
        _EMAIL = intent.getStringExtra("email");
        _HOWMANYDAYS = intent.getStringExtra("how_many_day");
        _ACNONAC = intent.getStringExtra("acNonAc");
        _BOOK_FROM_PICKER = intent.getStringExtra("book_from");
        _BOOK_TO_PICKER = intent.getStringExtra("book_to");
        oldsdate = _BOOK_FROM_PICKER;
        oldtdate = _BOOK_TO_PICKER;
        _TOTALAMOUNT = intent.getStringExtra("total_amount");
        _ADVPAID = intent.getStringExtra("adv_paid");
        _REMAININGBAL = intent.getStringExtra("remaining_bal");
        _ADVRECEIVEDBY = intent.getStringExtra("adv_received_by");
        _BALRECEIVEDBY = intent.getStringExtra("bal_received_by");
        _DECORATIONAMOUNT = intent.getStringExtra("decoration_amount");
        _ELECTRICITYBILL = intent.getStringExtra("electricity_bill");
        _CLEANINGCHARGES = intent.getStringExtra("cleaning_charges");
        _WAITERAMOUNT = intent.getStringExtra("waiter_amount");
        _DRINKINGWATERAMOUNT = intent.getStringExtra("drinking_water");
        _DESC = intent.getStringExtra("additional");
        //imageUrl = intent.getStringExtra("image");
        _GENERATORCHARGES = intent.getStringExtra("generator_charges");
        _DAMAGECHARGES = intent.getStringExtra("damage_charges");
        _GUESTHOUSE = intent.getStringExtra("guest_house");
        _VIPDINE = intent.getStringExtra("VIP_dine");
        _FUNCTIONTYPE = intent.getStringExtra("function_type");
        _HALLAMOUNT = intent.getStringExtra("hall_amount");
        _ADDRESS = intent.getStringExtra("address");
        _PEOPLECAPACITY = intent.getStringExtra("people_capacity");
        _KEY = intent.getStringExtra("key");
        _IMAGE = intent.getStringExtra("Image");

        if(_ACNONAC.toLowerCase(Locale.ROOT).matches("ac")){
            radioGroup.check(R.id.acid);
        }else{
            radioGroup.check(R.id.nonacid);
        }

        bookfrom.setText(_BOOK_FROM_PICKER);
        bookto.setText(_BOOK_TO_PICKER);

        updateFullName.getEditText().setText(_FULLNAME);
        updatePhoneNumber.getEditText().setText(_PHONENUMBER);
        updateEmail.getEditText().setText(_EMAIL);
        updateHowManyDays.getEditText().setText(_HOWMANYDAYS);
//        updateAcNonAc.getEditText().setText(_ACNONAC);
//        update_book_from_picker.getEditText().setText(_BOOK_FROM_PICKER);
//        update_book_to_picker.getEditText().setText(_BOOK_TO_PICKER);
        updateTotalAmount.getEditText().setText(_TOTALAMOUNT);
        updateAdvPaid.getEditText().setText(_ADVPAID);
        updateRemainingBal.getEditText().setText(_REMAININGBAL);
        updateAdvReceivedBy.getEditText().setText(_ADVRECEIVEDBY);
        updateBalReceivedBy.getEditText().setText(_BALRECEIVEDBY);
        updateDecorationAmount.getEditText().setText(_DECORATIONAMOUNT);
        updateElectricityBill.getEditText().setText(_ELECTRICITYBILL);
        updateCleaningCharges.getEditText().setText(_CLEANINGCHARGES);
        updateWaiterAmount.getEditText().setText(_WAITERAMOUNT);
        updateDrinkingWaterAmount.getEditText().setText(_DRINKINGWATERAMOUNT);
        updateDesc.setText(_DESC);
        //Glide.with(UpdateDetailsActivity.this).load(getIntent().getExtras().getString("Image")).into(updateImage);
        updateGeneratorCharges.getEditText().setText(_GENERATORCHARGES);
        updateDamageCharges.getEditText().setText(_DAMAGECHARGES);
        updateGuestHouse.getEditText().setText(_GUESTHOUSE);
        updateVIPDine.getEditText().setText(_VIPDINE);
        updateFunctionType.getEditText().setText(_FUNCTIONTYPE);
        updateHallAmount.getEditText().setText(_HALLAMOUNT);
        updateAddress.getEditText().setText(_ADDRESS);
        updatePeopleCapacity.getEditText().setText(_PEOPLECAPACITY);


    }

    /*
            private boolean isImageChanged() {
                if (!imageUrl.equals(updateImage.toString())) {
                    storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    databaseReference.child(_PHONENUMBER).child("dataImage").setValue(updateImage.toString());
                    imageUrl = updateImage.toString();
                    return true;
                } else {
                    return false;
                }
            }
    */
    private boolean isPeopleCapacity() {
        if (!_PEOPLECAPACITY.equals(updatePeopleCapacity.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataPeopleCapacity").setValue(updatePeopleCapacity.getEditText().getText().toString());
            _PEOPLECAPACITY = updatePeopleCapacity.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isAddress() {
        if (!_ADDRESS.equals(updateAddress.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataAddress").setValue(updateAddress.getEditText().getText().toString());
            _ADDRESS = updateAddress.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isGeneratorCharges() {
        if (!_GENERATORCHARGES.equals(updateGeneratorCharges.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataGeneratorCharges").setValue(updateGeneratorCharges.getEditText().getText().toString());
            _GENERATORCHARGES = updateGeneratorCharges.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isHallAmount() {
        if (!_HALLAMOUNT.equals(updateHallAmount.getEditText().getText().toString())) {
            databaseReference.child(key).child("hallamount").setValue(updateHallAmount.getEditText().getText().toString());
            _HALLAMOUNT = updateHallAmount.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isFunctionType() {
        if (!_FUNCTIONTYPE.equals(updateFunctionType.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataFunctionType").setValue(updateFunctionType.getEditText().getText().toString());
            _FUNCTIONTYPE = updateFunctionType.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isVIPDine() {
        if (!_VIPDINE.equals(updateVIPDine.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataVIPDine").setValue(updateVIPDine.getEditText().getText().toString());
            _VIPDINE = updateVIPDine.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isGuestHouse() {
        if (!_GUESTHOUSE.equals(updateGuestHouse.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataGuestHouse").setValue(updateGuestHouse.getEditText().getText().toString());
            _GUESTHOUSE = updateGuestHouse.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDamageCharges() {
        if (!_DAMAGECHARGES.equals(updateDamageCharges.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataDamageCharges").setValue(updateDamageCharges.getEditText().getText().toString());
            _DAMAGECHARGES = updateDamageCharges.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDescChanged() {
        if (!_DESC.equals(updateDesc.getText().toString())) {
            databaseReference.child(key).child("dataDesc").setValue(updateDesc.getText().toString());
            _DESC = updateDesc.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean BalReceivedByChanged() {
        if (!_BALRECEIVEDBY.equals(updateBalReceivedBy.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataBalReceivedBy").setValue(updateBalReceivedBy.getEditText().getText().toString());
            _BALRECEIVEDBY = updateBalReceivedBy.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isAdvReceivedByChanged() {
        if (!_ADVRECEIVEDBY.equals(updateAdvReceivedBy.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataAdvReceivedBy").setValue(updateAdvReceivedBy.getEditText().getText().toString());
            _ADVRECEIVEDBY = updateAdvReceivedBy.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isRemainingBalChanged() {
        if (!_REMAININGBAL.equals(updateRemainingBal.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataRemainingBal").setValue(updateRemainingBal.getEditText().getText().toString());
            _REMAININGBAL = updateRemainingBal.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isAdvPaidChanged() {
        if (!_ADVPAID.equals(updateAdvPaid.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataAdvPaid").setValue(updateAdvPaid.getEditText().getText().toString());
            _ADVPAID = updateAdvPaid.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isTotalAmountChanged() {
        if (!_TOTALAMOUNT.equals(updateTotalAmount.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataTotalAmount").setValue(updateTotalAmount.getEditText().getText().toString());
            _TOTALAMOUNT = updateTotalAmount.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDrinkingAmountChanged() {
        if (!_DRINKINGWATERAMOUNT.equals(updateDrinkingWaterAmount.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataDrinkingWater").setValue(updateDrinkingWaterAmount.getEditText().getText().toString());
            _DRINKINGWATERAMOUNT = updateDrinkingWaterAmount.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isWaiterAmountChanged() {
        if (!_WAITERAMOUNT.equals(updateWaiterAmount.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataWaiterAmount").setValue(updateWaiterAmount.getEditText().getText().toString());
            _WAITERAMOUNT = updateWaiterAmount.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isCleaningChargesChanged() {
        if (!_CLEANINGCHARGES.equals(updateCleaningCharges.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataCleaningCharges").setValue(updateCleaningCharges.getEditText().getText().toString());
            _CLEANINGCHARGES = updateCleaningCharges.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isElectricityBillChanged() {
        if (!_ELECTRICITYBILL.equals(updateElectricityBill.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataElectricityBill").setValue(updateElectricityBill.getEditText().getText().toString());
            _ELECTRICITYBILL = updateElectricityBill.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDecorationAmountChanged() {
        if (!_DECORATIONAMOUNT.equals(updateDecorationAmount.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataDecorationAmount").setValue(updateDecorationAmount.getEditText().getText().toString());
            _DECORATIONAMOUNT = updateDecorationAmount.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isToDateChanged() {
        if (!_BOOK_TO_PICKER.equals(bookto.getText().toString())) {
            databaseReference.child(key).child("dataToDate").setValue(bookto.getText().toString());
            _BOOK_TO_PICKER = bookto.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isFromDateChanged() {
        if (!_BOOK_FROM_PICKER.equals(bookfrom.getText().toString())) {
            databaseReference.child(key).child("dataFromDate").setValue(bookfrom.getText().toString());
            _BOOK_FROM_PICKER = bookfrom.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isAcNonAcChanged() {


        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if(selectedId>0) {


            if (!_ACNONAC.equals(genderradioButton.getText().toString())) {
                databaseReference.child(key).child("dataAcNonAc").setValue(genderradioButton.getText().toString());
                _ACNONAC = genderradioButton.getText().toString();
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }

    }

    private boolean isHowManyDaysChanged() {
        if (!_HOWMANYDAYS.equals(updateHowManyDays.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataDays").setValue(updateHowManyDays.getEditText().getText().toString());
            _HOWMANYDAYS = updateHowManyDays.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!_EMAIL.equals(updateEmail.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataEmail").setValue(updateEmail.getEditText().getText().toString());
            _EMAIL = updateEmail.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneNumberChanged() {
        if (!_PHONENUMBER.equals(updatePhoneNumber.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataFullName").setValue(updatePhoneNumber.getEditText().getText().toString());
            _PHONENUMBER = updatePhoneNumber.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isFullNameChanged() {
        if (!_FULLNAME.equals(updateFullName.getEditText().getText().toString())) {
            databaseReference.child(key).child("dataFullName").setValue(updateFullName.getEditText().getText().toString());
            _FULLNAME = updateFullName.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }
}