package com.example.ashrafapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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

public class UploadActivity extends AppCompatActivity {

    //Variables
    ImageView uploadImage;
    Button saveButton;
    EditText uploadDesc;
    TextInputLayout uploadFullName, uploadPhoneNumber, uploadEmail, uploadHowManyDays, uploadTotalAmount, uploadAdvPaid, uploadRemainingBal,
            uploadAdvReceivedBy, uploadBalReceivedBy, uploadDecorationAmount, uploadElectricityBill, uploadCleaningCharges,
            uploadWaiterAmount, uploadDrinkingWaterAmount, book_from_picker, book_to_picker, uploadAcNonAc,
            uploadGeneratorCharges, uploadDamageCharges, uploadGuestHouse, uploadVIPDine, uploadFunctionType,
            uploadHallAmount, uploadAddress, uploadPeopleCapacity, uploadhall, uploadreferred;


    RadioButton genderradioButton;
    RadioGroup radioGroup;

    //DatePicker book_from_picker, book_to_picker;
    String imageURL;
    TextView bookfrom, bookto;
    Uri uri;
    boolean imageuploaded = false;
    int hallamt, decoramt, eleamt, clnamt, dmgamt, genamt, waiamt, drnamt;
    int a, b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadFullName = findViewById(R.id.uploadFullName);
        uploadPhoneNumber = findViewById(R.id.uploadPhoneNumber);
        uploadEmail = findViewById(R.id.uploadEmail);
        uploadHowManyDays = findViewById(R.id.uploadHowManyDays);
        uploadDecorationAmount = findViewById(R.id.uploadDecorationAmount);
        uploadElectricityBill = findViewById(R.id.uploadElectricityBill);
        uploadCleaningCharges = findViewById(R.id.uploadCleaningCharges);
        uploadWaiterAmount = findViewById(R.id.uploadWaiterAmount);
        uploadDrinkingWaterAmount = findViewById(R.id.uploadDrinkingWaterAmount);
        uploadTotalAmount = findViewById(R.id.uploadTotalAmount);
        uploadAdvPaid = findViewById(R.id.uploadAdvPaid);
        uploadRemainingBal = findViewById(R.id.uploadRemainingBal);
        uploadAdvReceivedBy = findViewById(R.id.uploadAdvReceivedBy);
        uploadBalReceivedBy = findViewById(R.id.uploadBalReceivedBy);
        uploadDesc = findViewById(R.id.uploadDesc);
//        uploadAcNonAc = findViewById(R.id.uploadAcNonAc);
        // radioGroup = findViewById(R.id.booking_radio_group);
//        book_to_picker = findViewById(R.id.book_to_picker);
        uploadGeneratorCharges = findViewById(R.id.uploadGeneratorCharges);
        uploadDamageCharges = findViewById(R.id.uploadDamageCharges);
        uploadGuestHouse = findViewById(R.id.uploadGuestHouse);
        uploadVIPDine = findViewById(R.id.uploadVIPDine);
        uploadFunctionType = findViewById(R.id.uploadFunctionType);
        uploadHallAmount = findViewById(R.id.uploadHallAmount);
        uploadAddress = findViewById(R.id.uploadAddress);
        uploadPeopleCapacity = findViewById(R.id.uploadPeopleCapacity);

        uploadImage = findViewById(R.id.uploadImage);

        saveButton = findViewById(R.id.saveButton);

        bookfrom = findViewById(R.id.bookfrom);
        bookto = findViewById(R.id.bookto);

        uploadhall = findViewById(R.id.uploadhallamount);
        uploadreferred = findViewById(R.id.uploadreferredby);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
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
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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
                        UploadActivity.this,
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
                        UploadActivity.this,
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

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = radioGroup.getCheckedRadioButtonId();
                genderradioButton = (RadioButton) findViewById(selectedId);

                if (selectedId < 0) {
                    Toast.makeText(UploadActivity.this, "Please select Ac or Non-Ac", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (bookfrom.getText().toString().toLowerCase(Locale.ROOT).matches("book from")) {
                    Toast.makeText(UploadActivity.this, "Please choose book from date", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (bookto.getText().toString().toLowerCase(Locale.ROOT).matches("book to")) {
                    Toast.makeText(UploadActivity.this, "Please choose book to date", Toast.LENGTH_SHORT).show();

                    return;
                }


//                if(imageURL != null) {
//                    if(imageuploaded){
                uploadData();
//                    }else {
//                        saveData();
//                    }
//                }else {
//                    Toast.makeText(UploadActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        uploadTotalAmount.getEditText()
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        if(!uploadTotalAmount.getEditText().getText().toString().matches("")){
                           a =  Integer.parseInt(uploadTotalAmount.getEditText().getText().toString());
                        }else{
                            a =0;
                        }

                        if (!uploadAdvPaid.getEditText().getText().toString().matches("")) {
                            b =
                                    Integer.parseInt(uploadAdvPaid.getEditText().getText().toString());
                        }else {

                            b= 0;
                        }
                        if(!uploadRemainingBal.getEditText().getText().toString().matches(""))
                        {
                            c = Integer.parseInt(uploadRemainingBal.getEditText().getText().toString());
                        }else{
                            c =0;
                        }
                            uploadRemainingBal.getEditText().setText(String.valueOf(a-b));


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

        uploadAdvPaid.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!uploadTotalAmount.getEditText().getText().toString().matches("")){
                    a =  Integer.parseInt(uploadTotalAmount.getEditText().getText().toString());
                }else{
                    a =0;
                }

                if (!uploadAdvPaid.getEditText().getText().toString().matches("")) {
                    b =
                            Integer.parseInt(uploadAdvPaid.getEditText().getText().toString());
                }else {

                    b= 0;
                }
                if(!uploadRemainingBal.getEditText().getText().toString().matches(""))
                {
                    c = Integer.parseInt(uploadRemainingBal.getEditText().getText().toString());
                }else{
                    c =0;
                }
                uploadRemainingBal.getEditText().setText(String.valueOf(a-b));


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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadDecorationAmount.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadElectricityBill.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadCleaningCharges.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadDamageCharges.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadGeneratorCharges.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadWaiterAmount.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadDrinkingWaterAmount.getEditText().addTextChangedListener(new TextWatcher() {
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
                (!uploadDecorationAmount.getEditText().getText().toString().matches("")) {
                    decoramt = Integer.parseInt(Objects.requireNonNull(uploadDecorationAmount.getEditText()).getText().toString());

                } else{
                    decoramt = 0;
                }

                if (
                        !uploadElectricityBill.getEditText().getText().toString().matches("")) {
                    eleamt = Integer.parseInt(Objects.requireNonNull(uploadElectricityBill.getEditText()).getText().toString());

                }else {
                    eleamt = 0;
                }

                if
                (!uploadCleaningCharges.getEditText().getText().toString().matches("")) {
                    clnamt = Integer.parseInt(Objects.requireNonNull(uploadCleaningCharges.getEditText()).getText().toString());

                }else{
                    clnamt = 0;
                }
                if (
                        !uploadDamageCharges.getEditText().getText().toString().matches("")) {
                    dmgamt = Integer.parseInt(Objects.requireNonNull(uploadDamageCharges.getEditText()).getText().toString());

                } else {
                    dmgamt = 0;
                }

                if (
                        !uploadGeneratorCharges.getEditText().getText().toString().matches("")) {
                    genamt = Integer.parseInt(Objects.requireNonNull(uploadGeneratorCharges.getEditText()).getText().toString());

                }else {
                    genamt = 0;
                }

                if (
                        !uploadWaiterAmount.getEditText().getText().toString().matches("")) {
                    waiamt = Integer.parseInt(Objects.requireNonNull(uploadWaiterAmount.getEditText()).getText().toString());

                }else{
                    waiamt = 0;
                }
                if (
                        !uploadDrinkingWaterAmount.getEditText().getText().toString().matches("")) {


                    drnamt = Integer.parseInt(Objects.requireNonNull(uploadDrinkingWaterAmount.getEditText()).getText().toString());

                }else{
                    drnamt = 0;
                }
                int a = hallamt + decoramt + eleamt + clnamt + dmgamt + genamt + waiamt + drnamt;


                uploadTotalAmount.getEditText().setText(String.valueOf(a));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//        checktotal();


    }

    private void checktotal() {


    }

    public void saveData() {

    }

    public void uploadData() {


        //Need to add radio buttons data and Data picker data
        String _uploadFullName = uploadFullName.getEditText().getText().toString();
        String _uploadPhoneNumber = uploadPhoneNumber.getEditText().getText().toString();
        String _uploadEmail = uploadEmail.getEditText().getText().toString();
        String _uploadHowManyDays = uploadHowManyDays.getEditText().getText().toString();
        String _radioGroup = genderradioButton.getText().toString();//uploadAcNonAc.getEditText().getText().toString();
        String _uploadFromDate = bookfrom.getText().toString();//book_from_picker.getEditText().getText().toString();
        String _uploadToDate = bookto.getText().toString();//book_to_picker.getEditText().getText().toString();
        String _uploadTotalAmount = uploadTotalAmount.getEditText().getText().toString();
        String _uploadAdvPaid = uploadAdvPaid.getEditText().getText().toString();
        String _uploadRemainingBal = uploadRemainingBal.getEditText().getText().toString();
        String _uploadAdvReceivedBy = uploadAdvReceivedBy.getEditText().getText().toString();
        String _uploadBalReceivedBy = uploadBalReceivedBy.getEditText().getText().toString();
        String _uploadDecorationAmount = uploadDecorationAmount.getEditText().getText().toString();
        String _uploadElectricityBill = uploadElectricityBill.getEditText().getText().toString();
        String _uploadCleaningCharges = uploadCleaningCharges.getEditText().getText().toString();
        String _uploadWaiterAmount = uploadWaiterAmount.getEditText().getText().toString();
        String _uploadDrinkingWaterAmount = uploadDrinkingWaterAmount.getEditText().getText().toString();
        String _uploadDesc = uploadDesc.getText().toString();

        String _uploadGeneratorCharges = uploadGeneratorCharges.getEditText().getText().toString();
        String _uploadDamageCharges = uploadDamageCharges.getEditText().getText().toString();
        String _uploadGuestHouse = uploadGuestHouse.getEditText().getText().toString();
        String _uploadVIPDine = uploadVIPDine.getEditText().getText().toString();
        String _uploadFunctionType = uploadFunctionType.getEditText().getText().toString();
        String _uploadHallAmount = uploadHallAmount.getEditText().getText().toString();
        String _uploadAddress = uploadAddress.getEditText().getText().toString();
        String _uploadPeopleCapacity = uploadPeopleCapacity.getEditText().getText().toString();
        String _hallamount = uploadhall.getEditText().getText().toString();
        String _referredby = uploadreferred.getEditText().getText().toString();


        if (_uploadFullName.matches("") || _uploadPhoneNumber.matches("") || _uploadPhoneNumber.length() != 10 || _uploadHowManyDays.matches("")
                || _uploadTotalAmount.matches("") || _uploadAdvPaid.matches("") || _uploadRemainingBal.matches("") || _uploadAdvReceivedBy.matches("")
        ) {
            Toast.makeText(this, "Please enter all values and phone number should be 10 digits", Toast.LENGTH_SHORT).show();

            return;
        }


        DataClass dataClass = new DataClass(_uploadFullName, _uploadPhoneNumber, _uploadEmail,
                _uploadHowManyDays, _radioGroup, _uploadFromDate, _uploadToDate, _uploadTotalAmount, _uploadAdvPaid,
                _uploadRemainingBal, _uploadAdvReceivedBy, _uploadBalReceivedBy, _uploadDecorationAmount,
                _uploadElectricityBill, _uploadCleaningCharges, _uploadWaiterAmount, _uploadDrinkingWaterAmount,
                _uploadDesc, imageURL, _uploadGeneratorCharges, _uploadDamageCharges, _uploadGuestHouse, _uploadVIPDine, _uploadFunctionType,
                _uploadHallAmount, _uploadAddress,
                _uploadPeopleCapacity, _hallamount, _referredby, FirebaseAuth.getInstance().getUid(), String.valueOf(System.currentTimeMillis()), "", "");
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("bookings").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            List<Date> dates = getDates(_uploadFromDate, _uploadToDate);
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
                                        .child(_uploadPhoneNumber)
                                        .child("message")
                                        .setValue(_uploadFullName + " " + _uploadPhoneNumber);


//                                System.out.println(formattedDate);
                            }


                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static List<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
}