package com.autocracy.faizan.login_registration;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JobRegistration extends AppCompatActivity {

    private EditText ename, eaddress, eaadhar, equalification, eskills,eage;
    private String name, address, aadhar, qualification, skills,age;
    Button next;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String gender, userid, userNumber;
    FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobregistration);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/poors");
        findviews();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = firebaseDatabase.getInstance();
                databaseReference = database.getReference("users/poors/");
                addUser();
            }
        });
    }



    public void addUser() {
        name = ename.getText().toString();
        age = eage.getText().toString();
        aadhar = eaadhar.getText().toString();
        address = eaddress.getText().toString();
        qualification = equalification.getText().toString();
        skills = eskills.getText().toString();
        if (qualification.isEmpty()) qualification="";

        if (TextUtils.isEmpty(name)) {
            ename.requestFocus();
            ename.setError("Please Enter Your Name");
        } else if (TextUtils.isEmpty(address)) {
            eaddress.requestFocus();
            eaddress.setError("Please Enter Your address");
        } else if (TextUtils.isEmpty(aadhar)) {
            eaadhar.requestFocus();
            eaadhar.setError("Please Enter Your aadhar Number");
        }
        else if (aadhar.length()<12 || aadhar.length()>12) {
            eaadhar.requestFocus();
            eaadhar.setError("Please Enter Proper aadhar Number");
        } else if (TextUtils.isEmpty(skills)) {
            eskills.requestFocus();
            eskills.setError("Please Enter Your Skills");
        }
        else if (TextUtils.isEmpty(age)) {
            eage.requestFocus();
            eage.setError("Please Enter Your Age");
        }else {

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userid = currentFirebaseUser.getUid();
            userNumber = currentFirebaseUser.getPhoneNumber();
            databaseReference.child(userid).child("name").setValue(name);
            databaseReference.child(userid).child("address").setValue(address);
            databaseReference.child(userid).child("gender").setValue(gender);
            databaseReference.child(userid).child("qualification").setValue(qualification);
            databaseReference.child(userid).child("skills").setValue(skills);
            databaseReference.child(userid).child("id").setValue(userid);
            databaseReference.child(userid).child("contact").setValue(userNumber);
            databaseReference.child(userid).child("age").setValue(age);

//            Toast.makeText(JobRegistration.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            new SweetAlertDialog(JobRegistration.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Done!")
                    .setContentText("You are now Ready to apply for Jobs !!")
                    .setConfirmText("GO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(JobRegistration.this, Jobs.class));
                            finish();

                        }
                    })
                    .show();
        }
    }

    private void findviews() {
        next = findViewById(R.id.next);
        ename = findViewById(R.id.name);
        eaddress = findViewById(R.id.address);
        eaadhar = findViewById(R.id.aadhar);
        equalification = findViewById(R.id.qualification);
        eskills = findViewById(R.id.skills);
        radioGroup = findViewById(R.id.radiogroup);
      eage = findViewById(R.id.age);
    }

    public void checkButton(View view) {

        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(id);
        gender = (String) radioButton.getText();
        Toast.makeText(JobRegistration.this, gender, Toast.LENGTH_LONG).show();
    }


}

