package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class CheckingUser extends AppCompatActivity {


    DatabaseReference databaseReference;
//
    public boolean haveNetwork()
    {
        boolean have_WIFI=false;
        boolean have_MobileData=false;

        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos)
        {
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                    have_WIFI=true;
            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    have_MobileData=true;
        }
        return have_WIFI||have_MobileData;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_user);
        final android.app.AlertDialog dialog= new SpotsDialog.Builder().setContext(CheckingUser.this).setMessage("Getting you inside").build();
        dialog.show();
        if (haveNetwork())
        checkingUser();
        else
        {

            new SweetAlertDialog(CheckingUser.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No Internet Connection!!")
                                .setContentText("Turn it on to use our services")
                                .setConfirmText("OK")
                    
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                    }
                                })
                                .show();
            dialog.dismiss();
//
        }
//        Boolean check = haveNetwork();
//
//        if (check)
//        {
//            checkingUser();
//            return;
//        }
//        else
//        {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//
//
//                }
//            }, 5000);
//        }
//






    }

    private void checkingUser() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();

//        Checking is user exist in poors data.

        databaseReference = FirebaseDatabase.getInstance().getReference("users/poors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userid))
                {
                    Log.d("found"+userid+" in poors",dataSnapshot.child(userid).getValue().toString());

                    startActivity(new Intent(CheckingUser.this,HomepageUser.class));
//                    dialog.dismiss();
                    finish();
                }
                else
                {
                    checkCN();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ends

//        Code to check user is a company

//        ends


    }

    private void checkCN() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users/company");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userid))
                {
                    Log.d("found "+userid+" in com",dataSnapshot.child(userid).getValue().toString());
                    startActivity(new Intent(CheckingUser.this,HomepageCN.class));
//                    dialog.dismiss();
                    finish();
                }
                else
                {
                    startActivity(new Intent(CheckingUser.this,Homepage.class));
//                    dialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
