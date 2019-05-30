package com.autocracy.faizan.login_registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class HomepageUser extends AppCompatActivity {
    public AlertDialog dialog;
    private long backpressed;
    public DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    CardView getjob,gp, scholarship, donation, upload;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String username;

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
    protected void onStart() {
        super.onStart();
        if (!haveNetwork())
        {
            new SweetAlertDialog(HomepageUser.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No Internet Connection!!")
                    .setContentText("Turn it on to use our services")
                    .setConfirmText("OK").show();
//            Toast.makeText(Homepage.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_user);



        firebaseDatabase = FirebaseDatabase.getInstance();
        //        Menu starts
        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomepageUser.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        NavigationView navigation = (NavigationView)findViewById(R.id.nav_view);
//        TextView userNumber = navigation.getHeaderView(0).findViewById(R.id.number);

        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        userNumber.setText(currentFirebaseUser.getPhoneNumber());


        databaseReference = FirebaseDatabase.getInstance().getReference("users/poors");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentFirebaseUser.getUid()))
                {
//                   DatabaseReference db = databaseReference.child(currentFirebaseUser.getUid());
                    Log.d("is answer ","chalra hai");
                    username = dataSnapshot.child(currentFirebaseUser.getUid()).child("name").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        TextView un = navigation.getHeaderView(0).findViewById(R.id.username);
//        un.setText(username);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id)
                {
                    case R.id.logout:
                        new SweetAlertDialog(HomepageUser.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Logging out..")
                                .setContentText("Are you sure ??")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog
//                                            .setTitleText("Deleted!")
//                                            .setContentText("Your imaginary file has been deleted!")
//                                            .setConfirmText("OK")
//                                            .setConfirmClickListener(null)
//                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(HomepageUser.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    }

//
                                }).setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
//                                startActivity(new Intent(HomepageUser.this, HomepageUser.class));
//                                finish();
                            }
                        })

                                .show();



                        break;
                    case R.id.home:
                        startActivity(new Intent(HomepageUser.this,HomepageUser.class));
                        finish();
                        break;
                }
                return false;
            }
        });
//        Menu ends

        getjob = findViewById(R.id.job);
        gp = findViewById(R.id.gp);
//        notification = findViewById(R.id.notification);
        scholarship = findViewById(R.id.scholarship);
        donation = findViewById(R.id.donation);
//        profile = findViewById(R.id.profile);
        upload = findViewById(R.id.upload);


        getjob.setOnClickListener(new View.OnClickListener() {
            DatabaseReference databaseReference = firebaseDatabase.getReference("users/poors");

            @Override
            public void onClick(View view) {
                final android.app.AlertDialog dialog = new SpotsDialog.Builder().setContext(HomepageUser.this).setMessage("Loading").build();
                dialog.show();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String id = currentFirebaseUser.getUid();

                        if (dataSnapshot.hasChild(id)) {
                            // Toast.makeText(JobRegistration.this,"Found user",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomepageUser.this, Jobs.class));
                            dialog.dismiss();
                            finish();
                        } else {
//                            Toast.makeText(JobRegistration.this, "User not Found", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(HomepageUser.this, JobRegistration.class);

                            startActivity(i);
                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomepageUser.this,Donation.class);
                startActivity(i);
                finish();
            }
        });

        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(HomepageUser.this,GovernmentPrograms.class));
finish();
            }
        });

//        postjob.setOnClickListener(new View.OnClickListener() {
//
//            DatabaseReference databaseReference = firebaseDatabase.getReference("users/company");
//
//            @Override
//            public void onClick(View view) {
//                final android.app.AlertDialog dialog = new SpotsDialog.Builder().setContext(Homepage.this).setMessage("Loading").build();
//                dialog.show();
//
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                        String id = currentFirebaseUser.getUid();
//
//                        if (dataSnapshot.hasChild(id)) {
//                            // Toast.makeText(JobRegistration.this,"Found user",Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(Homepage.this, ViewCompanyPosts.class));
//                            dialog.dismiss();
//
//                            finish();
//                        } else {
////                            Toast.makeText(JobRegistration.this, "User not Found", Toast.LENGTH_LONG).show();
//                            Intent i = new Intent(Homepage.this, companyRegistration.class);
//                            startActivity(i);
//                            dialog.dismiss();
//
//                            finish();
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });

        scholarship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SweetAlertDialog(HomepageUser.this))
//                        .setTitleText("Module is under Construction...")
//                        .show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scholarship");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            Intent i = new Intent(HomepageUser.this,ScholarshipMainpage.class);
                            i.putExtra("openProfile","1");
                            startActivity(i);
                            finish();
                        }
                        else {
                            Intent i = new Intent(HomepageUser.this, ScholarshipLogin2.class);
                            startActivity(i);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomepageUser.this,Uploader.class);
                startActivity(i);
                finish();
            }
        });
    }

//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Homepage.this,JobRegistration.class);
//                startActivity(i);
//            }
//        });
//

//

//

    @Override
    public void onBackPressed()
    {
        if (backpressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Press Back again to Exit",Toast.LENGTH_LONG).show();
        }
        backpressed = System.currentTimeMillis();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
