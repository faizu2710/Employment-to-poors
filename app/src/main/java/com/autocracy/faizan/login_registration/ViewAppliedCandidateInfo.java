package com.autocracy.faizan.login_registration;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewAppliedCandidateInfo extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser,userid;
    TextView ename,egender,eaddress,eage,eskills,equalification;
    TextView tnumber;
    String name,number,gender,address,age,skills,qualification;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applied_candidate_info);
        userid = getIntent().getExtras().getString("userid");
        Log.d("u",userid);

        findViews();
        getSetValues();


    }

    private void getSetValues() {

        databaseReference = FirebaseDatabase.getInstance().getReference("users/poors/"+userid);
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

    }
}
