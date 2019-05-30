package com.autocracy.faizan.login_registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class QualificationDetails extends Fragment {

    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser, userid;
    TextView eschoolname, epercentage, ecollegename, ecourse,edegree;
    String schoolname, percentage, collegename, degree,course;
    Button update;
    Spinner spinner;

    public QualificationDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qualification_details, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentFirebaseUser.getUid();

        findViews(view);
        getSetValues();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEducation();
            }
        });
    }

    private void updateEducation() {

        schoolname = eschoolname.getText().toString();
        percentage = epercentage.getText().toString();
        collegename = ecollegename.getText().toString();
        degree = edegree.getText().toString();


        if (TextUtils.isEmpty(schoolname)) {
            eschoolname.requestFocus();
            eschoolname.setError("Enter your school name");
        } else if (TextUtils.isEmpty(percentage)) {
            epercentage.requestFocus();
            epercentage.setError("Enter your percentage");
        } else if (TextUtils.isEmpty(collegename)) {
            ecollegename.requestFocus();
            ecollegename.setError("Enter your college name");
        }else if (TextUtils.isEmpty(degree)) {
            edegree.requestFocus();
            edegree.setError("Enter your degree");
        }
        else {
            databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
            databaseReference.child("schoolname").setValue(schoolname);
            databaseReference.child("percentage").setValue(percentage);
            databaseReference.child("collegename").setValue(collegename);
            databaseReference.child("degree").setValue(degree);
            databaseReference.child("course").setValue(course);


            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Done!").setContentText("Profile is Updated !").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
//                    Profile profile = new Profile();

                    Intent i = new Intent(getActivity(),ScholarshipMainpage.class);
                    i.putExtra("openProfile","10");
                    startActivity(i);
//
                }
            }).show();
        }
    }

    private void findViews(View view) {
        eschoolname = view.findViewById(R.id.scname);
        ecollegename = view.findViewById(R.id.cname);
        edegree = view.findViewById(R.id.degree);
        epercentage = view.findViewById(R.id.percentage);
        update = view.findViewById(R.id.update);
        spinner = view.findViewById(R.id.spinner);
    }

    private void getSetValues() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.course,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();

                schoolname = data.get("schoolname");
                eschoolname.setText(schoolname);
                percentage = data.get("percentage");
                epercentage.setText(percentage);
                collegename = data.get("collegename");
                ecollegename.setText(collegename);
                degree = data.get("degree");
                edegree.setText(degree);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}