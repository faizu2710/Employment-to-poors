package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExtraDetails extends Fragment {

    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser, userid;
    TextView eanualincome, eoccupation, einterest, eextra;
    String anualincome, occupation, interest, extra;
    Button update;


    public ExtraDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_extra_details, container, false);
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
                updateDetails();
            }
        });
    }

    private void updateDetails() {
        anualincome = eanualincome.getText().toString();
        occupation = eoccupation.getText().toString();
        interest = einterest.getText().toString();
        extra = eextra.getText().toString();

        if (TextUtils.isEmpty(anualincome)) {
            eanualincome.requestFocus();
            eanualincome.setError("Enter your income");
        } else if (TextUtils.isEmpty(occupation)) {
            eoccupation.requestFocus();
            eoccupation.setError("Enter your occupation");
        } else if (TextUtils.isEmpty(interest)) {
            einterest.requestFocus();
            einterest.setError("Enter your interests");
        } else if (TextUtils.isEmpty(extra)) {
            eextra.requestFocus();
            eextra.setError("Enter your talent");
        }
        else {
            databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
            databaseReference.child("anualincome").setValue(anualincome);
            databaseReference.child("occupation").setValue(occupation);
            databaseReference.child("interest").setValue(interest);
            databaseReference.child("extra").setValue(extra);

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
        eanualincome = view.findViewById(R.id.income);
        eoccupation = view.findViewById(R.id.occupation);
        einterest = view.findViewById(R.id.interest);
        eextra = view.findViewById(R.id.extra);
        update = view.findViewById(R.id.update);
    }

    private void getSetValues() {
        databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();

                anualincome = data.get("anualincome");
                eanualincome.setText(anualincome);
                occupation = data.get("occupation");
                eoccupation.setText(occupation);
                interest = data.get("interest");
                einterest.setText(interest);
                extra = data.get("extra");
                eextra.setText(extra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
