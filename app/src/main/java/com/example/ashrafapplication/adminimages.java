package com.example.ashrafapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class adminimages extends AppCompatActivity {

    CropImageView image;
    Uri uri, croppedImageUri;
    Button upload, addimage;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminimages);


        image = findViewById(R.id.cropImageView);
        upload = findViewById(R.id.uploadbtn);
        key = getIntent().getStringExtra("key");
        addimage = findViewById(R.id.addimage);

//        image.setImageResource(R.drawable.uploading);

        if(key== null){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();


                            if(key.matches("auto")) {
                                CropImage.activity(uri)
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .setAspectRatio(9, 7)
                                        .start(adminimages.this);
                            }else{
                                CropImage.activity(uri)
                                        .setGuidelines(CropImageView.Guidelines.ON)
//                                        .setAspectRatio(9, 7)
                                        .start(adminimages.this);
                            }

//                            uploadImage.setImageURI(uri);

//                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("bookimages")
//                                    .child(FirebaseAuth.getInstance().getUid().toString()).child(String.valueOf(System.currentTimeMillis()));
//                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
//                            builder.setCancelable(false);
//                            builder.setView(R.layout.progress_layout);
//                            AlertDialog dialog = builder.create();
////        TextView pt = dialog.findViewById(R.id.progresstext);
////        pt.setText("Uploading image please wait");
//                            dialog.show();
//                            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                    while (!uriTask.isComplete()) ;
//                                    Uri urlImage = uriTask.getResult();
//                                    imageURL = urlImage.toString();
////                if(imageURL!= null){
////                    imageuploaded = true;
////                }
////                                    uploadData();
//                                    dialog.dismiss();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    dialog.dismiss();
//                                }
//                            });






                        } else {
                            Toast.makeText(adminimages.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(croppedImageUri != null && !croppedImageUri.equals("")){

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("appimages")
                            .child(FirebaseAuth.getInstance().getUid().toString()).child(String.valueOf(System.currentTimeMillis()));
                    AlertDialog.Builder builder = new AlertDialog.Builder(adminimages.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.progress_layout);
                    AlertDialog dialog = builder.create();
//        TextView pt = dialog.findViewById(R.id.progresstext);
//        pt.setText("Uploading image please wait");
                    dialog.show();
                    storageReference.putFile(croppedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            Uri urlImage = uriTask.getResult();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("images").child(key).push()
                                            .child("image").setValue(urlImage.toString());

                            image.clearImage();

//                            imageURL = urlImage.toString();
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

                }
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent photoPicker = new Intent(Intent.ACTION_PICK);
                        photoPicker.setType("image/*");
                        activityResultLauncher.launch(photoPicker);


            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                croppedImageUri = result.getUri();

                image.setImageUriAsync(croppedImageUri);



                // Do something with the cropped image URI
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                // Handle cropping error
            }
        }
    }


}
