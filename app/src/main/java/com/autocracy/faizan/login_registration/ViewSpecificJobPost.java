package com.autocracy.faizan.login_registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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

public class ViewSpecificJobPost extends AppCompatActivity {
    String parentId,contact,title,location,salaryfrom,salaryto,openings,description,skills,id,email,comname,website;
    TextView tcontact,ttitle,tlocation,tsalary,topenings,tdescription,tskills,temail,tcomname,twebsite;
    Button apply;
    final String[] m_Text = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific_job_post);

        parentId = getIntent().getExtras().getString("parentid");
        contact = getIntent().getExtras().getString("contact");
        title = getIntent().getExtras().getString("jobtitle");
        location = getIntent().getExtras().getString("joblocation");
        description = getIntent().getExtras().getString("description");
        openings = getIntent().getExtras().getString("openings");
        skills = getIntent().getExtras().getString("skills");
        salaryfrom= getIntent().getExtras().getString("salaryfrom");
        salaryto = getIntent().getExtras().getString("salaryto");
        id = getIntent().getExtras().getString("id");

        findViews();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = currentUser.getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/company/"+parentId);
        Log.d("ref is",String.valueOf(databaseReference));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Map<String,String> map = (Map<String, String>)dataSnapshot.getValue();
                Log.d("name is",map.get("contact"));
                contact = String.valueOf(map.get("contact"));
                email = String.valueOf(map.get("email"));
                comname = String.valueOf(map.get("name"));
                website = String.valueOf(map.get("website"));
                tcomname.setText("Company Name : "+comname);
                twebsite.setText(website);
                temail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setValues();

//Checking if user is already applied
        DatabaseReference d = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id);
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId))
                {
                    apply.setText("applied");
                    apply.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//Checking if user is already selected.
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("selectedcandidates/"+parentId+"/"+id);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId))
                {
                    apply.setText("Selected");
                    apply.setClickable(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSpecificJobPost.this);
                builder.setTitle("Any Experience??(in years)");

// Set up the input
                final EditText input = new EditText(ViewSpecificJobPost.this);


// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String n = input.getText().toString();
                            if (n.isEmpty()) {
                                new SweetAlertDialog(ViewSpecificJobPost.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Enter experience or click NO").show();
                            } else {
                                int no = Integer.parseInt(n);
                                if (no < 0 || no > 15) {
                                    new SweetAlertDialog(ViewSpecificJobPost.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Enter Proper Experience").show();
                                } else {
                                    m_Text[0] = input.getText().toString();
                                    dbCodeForCompany();
                                    dbCodeForUsers();
                                    dbCodeForNotification();
                                    new SweetAlertDialog(ViewSpecificJobPost.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Done!")
                                            .setContentText("Applied for this Job!!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
//                                        startActivity(new Intent(ViewSpecificJobPost.this,Jobs.class));
                                                    finish();


                                                }
                                            })
                                            .show();

                                }
                            }
                        }

                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            m_Text[0] = "0";
                            dbCodeForCompany();
                            dbCodeForUsers();
                            new SweetAlertDialog(ViewSpecificJobPost.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Done!")
                                    .setContentText("Applied for this Job!!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
//                                         startActivity(new Intent(ViewSpecificJobPost.this,Jobs.class));
                                            finish();


                                        }
                                    })
                                    .show();

                        }
                    });

                    builder.show();
                }

            private void dbCodeForNotification() {
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("notifications/"+userId+"/"+parentId+"/"+id);
                databaseReference2.child("selected").setValue(false);
                databaseReference2.child("view").setValue(false);

            }


//                UserId starts





            private void dbCodeForCompany() {



                Log.d("userid",userId);

//                UserId ends
                DatabaseReference d1 = FirebaseDatabase.getInstance().getReference().child("appliedCandidates");
                d1.child(parentId).child(id).child(userId).child("userid").setValue(userId);
                d1.child(parentId).child(id).child(userId).child("experience").setValue(String.valueOf(m_Text[0]));
            }

            private void dbCodeForUsers(){
                DatabaseReference d2 = FirebaseDatabase.getInstance().getReference().child("appliedJobs");
                d2.child(userId).child(parentId).child(id).setValue(true);


            }
        });


    }

    private void setValues() {
        ttitle.setText(title);
        tskills.setText("Skills : "+skills);
        tsalary.setText(salaryfrom+"-"+salaryto);
        tdescription.setText("Description : "+description);

        tcontact.setText(contact);
        tlocation.setText(location);
        topenings.setText(openings);



    }

    private void findViews() {
        tcomname = findViewById(R.id.coname);
        ttitle = findViewById(R.id.title);
        tlocation = findViewById(R.id.colocation);
        tcontact = findViewById(R.id.cocontact);
        topenings = findViewById(R.id.coopenings);
        tsalary = findViewById(R.id.salary);
        temail = findViewById(R.id.coemail);
        tdescription = findViewById(R.id.description);
        tskills = findViewById(R.id.skills);
        apply = findViewById(R.id.apply);
        twebsite = findViewById(R.id.website);
    }
}
