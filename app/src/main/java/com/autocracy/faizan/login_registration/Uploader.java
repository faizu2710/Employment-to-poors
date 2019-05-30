package com.autocracy.faizan.login_registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Uploader extends AppCompatActivity implements View.OnClickListener , LocationListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST =2;
    private ProgressDialog mProgressDialog;
    EditText filename,desc;
    private DatabaseReference mDatabase;
    private static final int PERMISSIONS_REQUEST = 100;
    private GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    double latitude;
    double longitude;
    ImageView imageView;
    private StorageReference mStorageRef;
    private Uri filePath;
    String fname,userId,description;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("users/poors/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId))
                {
                    startActivity(new Intent(Uploader.this,HomepageUser.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(Uploader.this,Homepage.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentuser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child("uploaders").child(userId);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Images");
        getLocation();
        findViewById(R.id.idbtn).setOnClickListener(this);

        findViewById(R.id.selectImage).setOnClickListener(this);
        filename=findViewById(R.id.imagename);
        desc = findViewById(R.id.desc);
        //mSelectImage.setOnClickListener(this);
        imageView = (ImageView) this.findViewById(R.id.imageView);

        mProgressDialog = new ProgressDialog(this);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            final com.autocracy.faizan.login_registration.Location location=new com.autocracy.faizan.login_registration.Location(latitude,longitude);
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(mphoto);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data1 = baos.toByteArray();
            UploadTask uploadTask=mStorageRef.child(fname).putBytes(data1);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Uploader.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();

                    //CODE FOR LOCATION STORE ON FIREBASE
                    mDatabase.child(fname).child("description").setValue(description);
                    mDatabase.child(fname).setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialog.dismiss();
                                new SweetAlertDialog(Uploader.this)
                                        .setTitleText("Great !! ")
                                        .setContentText("Image Uploaded Successfully")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                finish();
                                            }

//
                                        })
                                        .show();
//                                Toast.makeText(Uploader.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();


                            }else {
                                Toast.makeText(Uploader.this, "Failed", Toast.LENGTH_SHORT).show();

                                mProgressDialog.dismiss();


                            }

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Uploader.this, "Failed", Toast.LENGTH_SHORT).show();

                    mProgressDialog.dismiss();

                }
            });


        }
        else if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK  && data != null && data.getData() != null ){


            {
                mProgressDialog.setMessage("Uploading...");
                mProgressDialog.show();
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    imageView.setImageBitmap(bitmap);
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data1 = baos.toByteArray();
                    UploadTask uploadTask=mStorageRef.child(fname).putBytes(data1);
                    mDatabase.child("description").setValue(description);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Uploader.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            new SweetAlertDialog(Uploader.this)
                                    .setTitleText("Great !! ")
                                    .setContentText("Image Uploaded Successfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                    startActivity(new Intent(Uploader.this,Homepage.class));
                                    finish();
                                        }

//
                                    })
                                    .show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Uploader.this, "Failed", Toast.LENGTH_SHORT).show();

                            mProgressDialog.dismiss();

                        }
                    });

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        fname=filename.getText().toString();
        description = desc.getText().toString();

        if(fname.isEmpty()) {
            filename.setError("Enter File name");
            filename.requestFocus();
            return;
        }
        if (description.isEmpty())
        {
            desc.requestFocus();
            desc.setError("Enter Description");
            return;
        }
        switch (v.getId()) {
            case R.id.idbtn:


                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
                break;
            case R.id.selectImage:
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, GALLERY_REQUEST);
                break;
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        new SweetAlertDialog(Uploader.this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Please Enable GPS")
                .setConfirmText("OK")
                .show();
//        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }
}
