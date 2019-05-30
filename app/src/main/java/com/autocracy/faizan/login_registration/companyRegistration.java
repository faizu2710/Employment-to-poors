package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class companyRegistration extends AppCompatActivity {
EditText cname,caddress,cemail,cwebsite;
String name,address,email,website,userid,userNumber;
//private DrawerLayout drawerLayout;
//private ActionBarDrawerToggle actionBarDrawerToggle;

Button next;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (actionBarDrawerToggle.onOptionsItemSelected(item))
//        {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/company");

        //        Menu starts
//        drawerLayout = findViewById(R.id.drawer_layout);
//
//        actionBarDrawerToggle = new ActionBarDrawerToggle(companyRegistration.this,drawerLayout,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        NavigationView navigation = (NavigationView)findViewById(R.id.nav_view);
//        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                switch (id)
//                {
//                    case R.id.logout:
//                        FirebaseAuth.getInstance().signOut();
//                        Intent i = new Intent(companyRegistration.this, MainActivity.class);
//                        startActivity(i);
//                        finish();
//                        break;
//                    case R.id.home:
//                        startActivity(new Intent(companyRegistration.this,Homepage.class));
//                        break;
//                }
//                return false;
//            }
//        });
//        Menu ends

        findViews();
 next.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         addCompany();
     }
 });


    }

    private void addCompany() {
        name = cname.getText().toString();
        address = caddress.getText().toString();
        email = cemail.getText().toString();
        website = cwebsite.getText().toString();


        if (TextUtils.isEmpty(name)) {
            cname.requestFocus();
            cname.setError("Please Enter Your Name");
        } else if (TextUtils.isEmpty(address)) {
            caddress.requestFocus();
            caddress.setError("Please Enter Your address");
        } else if (TextUtils.isEmpty(email)) {
            cemail.requestFocus();
            cemail.setError("Please Enter Your Email address");
        }
//        else if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
//        {
//            cemail.requestFocus();
//            cemail.setError("Please Enter Correct Email address");
//        }
        else if (TextUtils.isEmpty(website)) {
            cemail.requestFocus();
            cemail.setError("Please Enter Your Website");
        }else {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                userid = currentFirebaseUser.getUid();
                userNumber = currentFirebaseUser.getPhoneNumber();
            databaseReference.child(userid).child("id").setValue(userid);
            databaseReference.child(userid).child("name").setValue(name);
            databaseReference.child(userid).child("email").setValue(email);
            databaseReference.child(userid).child("address").setValue(address);
            databaseReference.child(userid).child("website").setValue(website);
            databaseReference.child(userid).child("contact").setValue(userNumber);

            new SweetAlertDialog(companyRegistration.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Done!")
                    .setContentText("You are now Ready to post Jobs !!")
                    .setConfirmText("GO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(companyRegistration.this,ViewCompanyPosts.class));
                            finish();


                        }
                    })
                    .show();


//            Toast.makeText(companyRegistration.this,"Company Registered Successfully",Toast.LENGTH_LONG).show();

        }


    }

    private void findViews() {
    cname = findViewById(R.id.cname);
    caddress = findViewById(R.id.caddress);
    cemail = findViewById(R.id.cmail);
    cwebsite = findViewById(R.id.cwebsite);
    next = findViewById(R.id.next);
    }
}
