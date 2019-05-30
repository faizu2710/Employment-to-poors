package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewCompanyPost extends AppCompatActivity {
EditText ttitle,tdesc,tskills,tsalfrom,tsalto,tlocation,topenings,tcontact,tdatetime;
Button post;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
String title,desc,skills,salfrom,salto,location,openings,contact,userid,datetime;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewCompanyPost.this,ViewCompanyPosts.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_company_post);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("companyPost");



        findViews();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addPost();
            }
        });


    }

    private void addPost() {

        title = ttitle.getText().toString();
        desc = tdesc.getText().toString();
        skills = tskills.getText().toString();
        salfrom = tsalfrom.getText().toString();
        salto = tsalto.getText().toString();
        location = tlocation.getText().toString();
        openings = topenings.getText().toString();
        contact = tcontact.getText().toString();
//        For Storing Date
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        final String dateString = sdf.format(date);
        datetime = tdatetime.getText().toString();


        if (TextUtils.isEmpty(title)) {
            ttitle.requestFocus();
            ttitle.setError("Please Enter Job Title");
        } else if (TextUtils.isEmpty(desc)) {
            tdesc.requestFocus();
            tdesc.setError("Please Enter short description");
        } else if (TextUtils.isEmpty(skills)) {
            tskills.requestFocus();
            tskills.setError("Please Enter required Skills");
        } else if (TextUtils.isEmpty(salfrom)) {
            tsalfrom.requestFocus();
            tsalfrom.setError("Please Enter Salary From");
        }
        else if (TextUtils.isEmpty(salto)) {
            tsalto.requestFocus();
            tsalto.setError("Please Enter Salary To");
        }
        else if (TextUtils.isEmpty(location)) {
            tlocation.requestFocus();
            tlocation.setError("Please Enter Location");
        }
        else if (TextUtils.isEmpty(openings)) {
            topenings.requestFocus();
            topenings.setError("Please Enter Number of Openings");
        }
        else if (TextUtils.isEmpty(contact)) {
            tcontact.requestFocus();
            tcontact.setError("Please Enter Contact Number");
        }else if (contact.length()<10 || contact.length()>10) {
            tcontact.requestFocus();
            tcontact.setError("Please Enter 10 digits Contact Number");
        }else
        {
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userid = currentFirebaseUser.getUid();

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i;
                    String key = "0";
                   for (DataSnapshot d1 : dataSnapshot.child(userid).getChildren())
                   {
                       key = d1.getKey();
                       Log.d("keyyyy",key);
                   }
                    int count = Integer.parseInt(key);
                    ++count;
                    String c = Integer.toString(count);
                    Log.d("countttt",c);
                    databaseReference.child(userid).child(c).child("parentid").setValue(userid);

                    databaseReference.child(userid).child(c).child("id").setValue(c);
                    databaseReference.child(userid).child(c).child("title").setValue(title);
                    databaseReference.child(userid).child(c).child("description").setValue(desc);
                    databaseReference.child(userid).child(c).child("skills").setValue(skills);
                    databaseReference.child(userid).child(c).child("salaryfrom").setValue(salfrom);
                    databaseReference.child(userid).child(c).child("salaryto").setValue(salto);
                    databaseReference.child(userid).child(c).child("location").setValue(location);
                    databaseReference.child(userid).child(c).child("openings").setValue(openings);
                    databaseReference.child(userid).child(c).child("contact").setValue(contact);
                    databaseReference.child(userid).child(c).child("datetime").setValue(dateString);

//Popup message for successfull Entry.

                    //Popup message for successfull Entry ends.


//                    Toast.makeText(NewCompanyPost.this,"COunt is "+count+"Posted Successfully",Toast.LENGTH_LONG).show();
                    new SweetAlertDialog(NewCompanyPost.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Done!")
                            .setContentText("Have a look at your posts !!")
                            .setConfirmText("GO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    startActivity(new Intent(NewCompanyPost.this,ViewCompanyPosts.class));
                                    finish();

                                }
                            })
                            .show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }



    }

    private void findViews() {
        ttitle = findViewById(R.id.jobtitle);
        ttitle = findViewById(R.id.jobtitle);
        tdesc = findViewById(R.id.jobdesc);
        tsalfrom = findViewById(R.id.salfrom);
        tsalto = findViewById(R.id.salto);
        tskills = findViewById(R.id.jobskills);
        tlocation = findViewById(R.id.joblocation);
        topenings = findViewById(R.id.jobopenings);
        tcontact = findViewById(R.id.jobcontact);
        post = findViewById(R.id.post);
        tdatetime = findViewById(R.id.datetime);


    }
}
