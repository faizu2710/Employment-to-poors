package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScholarshipLogin extends AppCompatActivity {

    EditText password,email;
    Button login,register;
    TextView forgot_password;
    String e,p;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_login);
        findViews();
        auth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = email.getText().toString();
                p = password.getText().toString();
                if (TextUtils.isEmpty(e)) {
                    email.requestFocus();
                    email.setError("Please enter your email!");
                } else if (TextUtils.isEmpty(p)) {
                    password.requestFocus();
                    password.setError("Please enter your password");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
//
                    auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ScholarshipLogin.this, "Verification Link has been sent to your email. Please verify and Login from here", Toast.LENGTH_LONG).show();
                                auth.getCurrentUser().sendEmailVerification();
//                            Intent i = new Intent(ScholarshipLogin.this,ScholarshipMainpage.class);
//                            i.putExtra("openProfile","2");
//                            startActivity(i);
//
//                            finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ScholarshipLogin.this, "User already Exists!!"+task.getException().toString(), Toast.LENGTH_LONG).show();
//                                email.setText("");
//                                password.setText("");
                            }

                        }
                    });

//                startActivity(new Intent(ScholarshipLogin.this,ScholarshipForm.class));
//                finish();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                e = email.getText().toString();
                p = password.getText().toString();
                if (TextUtils.isEmpty(e)) {
                    email.requestFocus();
                    email.setError("Please enter your email!");
                } else if (TextUtils.isEmpty(p)) {
                    password.requestFocus();
                    password.setError("Please enter your password");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth = FirebaseAuth.getInstance();
                    AuthCredential credential = EmailAuthProvider.getCredential(e, p);
                    auth.getCurrentUser().linkWithCredential(credential);
                    auth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (auth.getCurrentUser().isEmailVerified()) {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scholarships");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                                                progressBar.setVisibility(View.GONE);
                                                Intent i = new Intent(ScholarshipLogin.this, ScholarshipMainpage.class);
                                                i.putExtra("openProfile", "2");
                                                startActivity(i);
                                                finish();
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Intent i = new Intent(ScholarshipLogin.this, ScholarshipForm.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(ScholarshipLogin.this, "Please Verify your account first !!", Toast.LENGTH_LONG).show();
                                    email.setText("");
                                    password.setText("");
                                }


                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ScholarshipLogin.this, "Check your email and password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scholarship");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    if (dataSnapshot.hasChild(userid)) {
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser()!=null) {
//            Intent i = new Intent(ScholarshipLogin.this,ScholarshipMainpage.class);
//            i.putExtra("openProfile","2");
//            startActivity(i);
//            finish();
//        }
//
//    }



    private void getVals() {


    }

    private void findViews() {

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.signin);
        register = findViewById(R.id.signup);
        forgot_password= findViewById(R.id.fp);
        progressBar = findViewById(R.id.login_progress);
    }
}
