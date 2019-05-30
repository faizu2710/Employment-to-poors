package com.autocracy.faizan.login_registration;

import android.content.Intent;
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

public class CompanyUserProfile extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser,userid;
    EditText ename,eaddress,eemail,ewebsite;
    TextView tnumber;
    String name,number,address,email,website;
    Button update;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CompanyUserProfile.this,HomepageCN.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_user_profile);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomnav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.my_posts:
                                startActivity(new Intent(CompanyUserProfile.this,ViewCompanyPosts.class));
                                finish();
                                break;
                            case R.id.add_post:
                                startActivity(new Intent(CompanyUserProfile.this,NewCompanyPost.class));
                                finish();
                                break;
                            case R.id.applied_status:
                                startActivity(new Intent(CompanyUserProfile.this,CompanyPostStatus.class));
                                finish();
                                break;
                            case R.id.selected:
                                startActivity(new Intent(CompanyUserProfile.this, SelectedCandidates.class));
                                finish();
                                break;

//                            case R.id.action_item1:
//                                selectedFragment = ItemOneFragment.newInstance();
//                                break;
//                            case R.id.action_item2:
//                                selectedFragment = ItemTwoFragment.newInstance();
//                                break;
//                            case R.id.action_item3:
//                                selectedFragment = ItemThreeFragment.newInstance();
//                                break;
//                        }

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }
    private void updateProfile() {
        name = ename.getText().toString();
        address = eaddress.getText().toString();
        email = eemail.getText().toString();
//        number = enumber.getText().toString();
        website = ewebsite.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            ename.requestFocus();
            ename.setError("Enter your name");
        }else if(TextUtils.isEmpty(email))
        {
            eemail.requestFocus();
            eemail.setError("Enter your E-mail");
        }else if(TextUtils.isEmpty(address))
        {
            eaddress.requestFocus();
            eaddress.setError("Enter your address");
        }else if(TextUtils.isEmpty(website))
        {
            ewebsite.requestFocus();
            ewebsite.setError("Enter your Website");
        }
        else
        {
            databaseReference = firebaseDatabase.getReference("users/company/"+userid);
            databaseReference.child("name").setValue(name);
            databaseReference.child("address").setValue(address);
            databaseReference.child("email").setValue(email);
            databaseReference.child("website").setValue(website);


            new SweetAlertDialog(CompanyUserProfile.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Done!")
                    .setContentText("Profile is Updated !")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(CompanyUserProfile.this,ViewCompanyPosts.class));
                            finish();

                        }
                    })
                    .show();

        }

    }


    private void getSetValues() {
        databaseReference = firebaseDatabase.getReference("users/company/"+userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();

                name = data.get("name");
                ename.setText(name);
                Log.d("name is",name);
                number = data.get("contact");
                tnumber.setText(number);

                address = data.get("address");
                eaddress.setText(address);

                email = data.get("email");
                eemail.setText(email);

                website = data.get("website");
                ewebsite.setText(website);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void findViews() {
        ename = findViewById(R.id.cname);
        tnumber = findViewById(R.id.cnumber);
        eaddress = findViewById(R.id.caddress);
        update = findViewById(R.id.update);
       eemail = findViewById(R.id.cemail);
       ewebsite = findViewById(R.id.cwebsite);
    }
}
