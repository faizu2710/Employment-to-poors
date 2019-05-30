package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewSpecificCompanyPost extends AppCompatActivity{
    EditText ttitle,tdesc,tskills,tsalfrom,tsalto,tlocation,topenings,tcontact;
    String title,desc,skills,salfrom,salto,location,openings,contact,userid;
    String t,d,s,sf,st,l,c,o,chi,id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle actionBarDrawerToggle;
    Button update;
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
        setContentView(R.layout.activity_view_specific_company_post);

        t = getIntent().getExtras().getString("ti");
        d = getIntent().getExtras().getString("de");

        s = getIntent().getExtras().getString("sk");

        sf = getIntent().getExtras().getString("sfr");

        st = getIntent().getExtras().getString("sto");

        l = getIntent().getExtras().getString("lo");

        o = getIntent().getExtras().getString("op");

        c = getIntent().getExtras().getString("co");
        id = getIntent().getExtras().getString("childr");
        Log.d("id is",id);



        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentFirebaseUser.getUid();
        databaseReference = firebaseDatabase.getReference("companyPost/"+userid);


        findViews();
        viewData();

//        findViewById(R.id.edittitle).setOnClickListener(this);
//        findViewById(R.id.editdesc).setOnClickListener(this);
//        findViewById(R.id.editskills).setOnClickListener(this);
//        findViewById(R.id.editsalfrom).setOnClickListener(this);
//        findViewById(R.id.editsalto).setOnClickListener(this);
//        findViewById(R.id.editlocation).setOnClickListener(this);
//        findViewById(R.id.editopenings).setOnClickListener(this);
//        findViewById(R.id.editcontact).setOnClickListener(this);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();

            }
        });






    }

    private void viewData() {
      ttitle.setText(t);
      tdesc.setText(d);
        tskills.setText(s);

        tsalfrom.setText(sf);

        tsalto.setText(st);

        tlocation.setText(l);
        topenings.setText(o);
        tcontact.setText(c);




    }

    private void updateDetails() {
        title = ttitle.getText().toString();
        desc = tdesc.getText().toString();
        skills = tskills.getText().toString();
        salfrom = tsalfrom.getText().toString();
        salto = tsalto.getText().toString();
        location = tlocation.getText().toString();
        openings = topenings.getText().toString();
        contact = tcontact.getText().toString();


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
        } else if (TextUtils.isEmpty(salto)) {
            tsalto.requestFocus();
            tsalto.setError("Please Enter Salary To");
        } else if (TextUtils.isEmpty(location)) {
            tlocation.requestFocus();
            tlocation.setError("Please Enter Location");
        } else if (TextUtils.isEmpty(openings)) {
            topenings.requestFocus();
            topenings.setError("Please Enter Number of Openings");
        } else if (TextUtils.isEmpty(contact)) {
            tcontact.requestFocus();
            tcontact.setError("Please Enter Contact Number");
        } else if (contact.length() < 10) {
            tsalfrom.requestFocus();
            tsalfrom.setError("Please Enter correct contact number");
        } else {
            databaseReference.child(id).child("title").setValue(title);
            databaseReference.child(id).child("description").setValue(desc);

            databaseReference.child(id).child("skills").setValue(skills);

            databaseReference.child(id).child("salaryfrom").setValue(salfrom);

            databaseReference.child(id).child("salaryto").setValue(salto);

            databaseReference.child(id).child("location").setValue(location);

            databaseReference.child(id).child("openings").setValue(openings);

            databaseReference.child(id).child("contact").setValue(contact);

//            Toast.makeText(ViewSpecificCompanyPost.this,"Data Updated Successfully",Toast.LENGTH_LONG).show();
            new SweetAlertDialog(ViewSpecificCompanyPost.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Done!")
                    .setContentText("Post is Updated !")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(ViewSpecificCompanyPost.this,ViewCompanyPosts.class));
                            finish();

                        }
                    })
                    .show();


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewSpecificCompanyPost.this,ViewCompanyPosts.class));
        finish();
    }

    //
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.edittitle:
//                ttitle.setEnabled(true);
//                break;
//            case R.id.editdesc:
//                tdesc.setEnabled(true);
//                break;
//            case R.id.editskills:
//                tskills.setEnabled(true);
//                break;
//                case R.id.editsalfrom:
//                tsalfrom.setEnabled(true);
//                break;
//            case R.id.editsalto:
//                tsalto.setEnabled(true);
//                break;
//            case R.id.editlocation:
//                tlocation.setEnabled(true);
//                break;
//            case R.id.editopenings:
//                topenings.setEnabled(true);
//                break;
//            case R.id.editcontact:
//                tcontact.setEnabled(true);
//                break;
//  }
//    }
    private void findViews(){
        ttitle = findViewById(R.id.etitle);
        tdesc = findViewById(R.id.edesc);
        tsalfrom = findViewById(R.id.esalfrom);
        tsalto = findViewById(R.id.esalto);
        tskills = findViewById(R.id.eskills);
        tlocation = findViewById(R.id.elocation);
        topenings = findViewById(R.id.eopenings);
        tcontact = findViewById(R.id.econtact);
        update = findViewById(R.id.update);
    }
}
