package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JobUserProfile extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser,userid;
    EditText ename,egender,eaddress,eage,eskills,equalification;
    TextView tnumber;
    String name,number,gender,address,age,skills,qualification;
    Button update;
    ImageView profileimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_user_profile);

        //        Bottom nav bar starts
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomnav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.all_jobs:
                                startActivity(new Intent(JobUserProfile.this,Jobs.class));
                                finish();
                                break;
                            case R.id.matching_jobs:
                                startActivity(new Intent(JobUserProfile.this,MatchingJobs.class));
                                finish();
                                break;
                            case R.id.my_status:
                                startActivity(new Intent(JobUserProfile.this,JobStatus.class));
                                finish();
                                break;
//

                        }
                        return true;
                    }
                });
//        ends

//        firebase user
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentFirebaseUser.getUid();


        findViews();

        getSetValues();

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePic();
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }

    private void profilePic() {

    }

    private void updateProfile() {
name = ename.getText().toString();
        address = eaddress.getText().toString();
        age = eage.getText().toString();
//        number = enumber.getText().toString();
        skills = eskills.getText().toString();
        qualification = equalification.getText().toString();
        gender = egender.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            ename.requestFocus();
            ename.setError("Enter your name");
        }else if(TextUtils.isEmpty(age))
        {
            eage.requestFocus();
            eage.setError("Enter your age");
        }else if(TextUtils.isEmpty(gender))
        {
            egender.requestFocus();
            egender.setError("Enter your gender");
        }else if(TextUtils.isEmpty(address))
        {
            eaddress.requestFocus();
            eaddress.setError("Enter your address");
        }else if(TextUtils.isEmpty(skills))
        {
            eskills.requestFocus();
            eskills.setError("Enter your Skills");
        }else if(TextUtils.isEmpty(qualification))
        {
            equalification.requestFocus();
            equalification.setError("Enter your Qualification");
        }
        else
        {
            databaseReference = firebaseDatabase.getReference("users/poors/"+userid);
            databaseReference.child("name").setValue(name);
            databaseReference.child("address").setValue(address);
            databaseReference.child("age").setValue(age);
            databaseReference.child("gender").setValue(gender);
            databaseReference.child("skills").setValue(skills);
            databaseReference.child("qualification").setValue(qualification);
            databaseReference.child("contact").setValue(number);


            new SweetAlertDialog(JobUserProfile.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Done!")
                    .setContentText("Profile is Updated !")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(JobUserProfile.this,Jobs.class));
                            finish();

                        }
                    })
                    .show();


        }

    }

    private void getSetValues() {
        databaseReference = firebaseDatabase.getReference("users/poors/"+userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();

                name = data.get("name");
                ename.setText(name);
                Log.d("name is",name);
                number = data.get("contact");
                tnumber.setText(number);
                gender = data.get("gender");
                egender.setText(gender);
                address = data.get("address");
                eaddress.setText(address);
                age = data.get("age");
                eage.setText(age);
                skills = data.get("skills");
                eskills.setText(skills);
                qualification = data.get("qualification");
                equalification.setText(qualification);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/poors/"+userid);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("imageuri"))
                        {
                            String uri = dataSnapshot.child("imageUri").getValue(String.class);

                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        profileimage.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            } catch (IOException e) {
                            }
                        }

                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void findViews() {
        ename = findViewById(R.id.username);
        tnumber = findViewById(R.id.number);
        egender = findViewById(R.id.gender);
        eaddress = findViewById(R.id.address);
        eage = findViewById(R.id.age);
        eskills = findViewById(R.id.skills);
        update = findViewById(R.id.update);
        equalification = findViewById(R.id.qualification);
        profileimage = findViewById(R.id.profile_image);




    }
}
