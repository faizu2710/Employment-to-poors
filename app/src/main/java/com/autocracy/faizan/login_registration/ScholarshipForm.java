package com.autocracy.faizan.login_registration;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class ScholarshipForm extends AppCompatActivity {
    private EditText ename, eaddress, econtact, eemail, epassword, eage;
    private EditText elandline;
    private String name;
    private String address;
    private String contact;
    private String email;
    private String password;
    private String age;
    String landline;
    Button next;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String gender, userid, userNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    String uid;
    private TextView dob, tvdob, doblabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_form);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("scholarship/");

        findviews();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase  = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("scholarship/");
                addUser();
            }
        });
    }



    public void addUser() {
        name = ename.getText().toString();
        age = eage.getText().toString();
        contact = econtact.getText().toString();
        address = eaddress.getText().toString();
//        password = epassword.getText().toString();
//        email = eemail.getText().toString();
        landline=elandline.getText().toString();


        if (TextUtils.isEmpty(name)) {
            ename.requestFocus();
            ename.setError("Please Enter Your Name");
        } else if (TextUtils.isEmpty(address)) {
            eaddress.requestFocus();
            eaddress.setError("Please Enter Your address");
        }
        else if (TextUtils.isEmpty(age)) {
            eage.requestFocus();
            eage.setError("Please Enter Your Age");
        }else if (TextUtils.isEmpty(contact)) {
            econtact.requestFocus();
            econtact.setError("Please Enter Your Contact Number");
        }else if (TextUtils.isEmpty(landline)) {
            elandline.requestFocus();
            elandline.setError("Please Enter Your number");
        }
        else if(contact.length() < 10 || contact.length()>10){
            econtact.setError("Please enter a valid contact");
        }
        else if(landline.length()<12 || landline.length()>12){
            elandline.setError("Please enter a valid number");
        }
        else {





                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        Log.d("email is ",userid);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scholarship");
                        databaseReference.child(userid).child("name").setValue(name);
                        databaseReference.child(userid).child("address").setValue(address);
                        databaseReference.child(userid).child("gender").setValue(gender);
                        databaseReference.child(userid).child("contact").setValue(contact);
                        databaseReference.child(userid).child("landline").setValue(landline);
                        databaseReference.child(userid).child("email").setValue(email);
                        databaseReference.child(userid).child("id").setValue(userid);
                        databaseReference.child(userid).child("age").setValue(age);
                        Toast.makeText(ScholarshipForm.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ScholarshipForm.this,ScholarshipMainpage.class);
                        i.putExtra("openProfile","1");
                        startActivity(i);
                        finish();

        }
    }

    private void findviews() {
        next = findViewById(R.id.next);
        ename = findViewById(R.id.name);
        eaddress = findViewById(R.id.address);
        econtact = findViewById(R.id.contact);
//        eemail = findViewById(R.id.email);
//        epassword = findViewById(R.id.password);
        radioGroup = findViewById(R.id.radiogroup);
        eage = findViewById(R.id.age);
        elandline = findViewById(R.id.landline);
//        String id = FirebaseAuth.getInstance().getCurrentUser();
//        Log.d("provider is ",id);
    }

    public void checkButton(View view) {

        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(id);
        gender = (String) radioButton.getText();
        Toast.makeText(ScholarshipForm.this, gender, Toast.LENGTH_LONG).show();
    }


}

