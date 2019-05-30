package com.autocracy.faizan.login_registration;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private long backpressed;
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    AlertDialog dialog,dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        StartFirebaseLogin();
        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = etPhoneNumber.getText().toString().trim();
                if (phoneNumber.length() < 10 || phoneNumber.length()>10) {
                    etPhoneNumber.setError("Enter correct Phone number !!");
                    etPhoneNumber.requestFocus();
                } else {
                    dialog = new SpotsDialog.Builder().setContext(MainActivity.this).setMessage("Sending OTP").build();
                    dialog.show();
                    phoneNumber = "+91" + phoneNumber;
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            60,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            MainActivity.this,        // Activity (for callback binding)
                            mCallback);                      // OnVerificationStateChangedCallbacks
                }
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                otp=etOTP.getText().toString();
                if (etOTP.length()<6)
                {
                    etOTP.setError("Enter Correct OTP");
                    etOTP.requestFocus();
                }
                else {
                    dialog2 = new  SpotsDialog.Builder().setContext(MainActivity.this).setMessage("Loggin in").build();
                    dialog2.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }
//                ProgressBar p = findViewById(R.id.progressBar);
//                p.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (backpressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else
        {
            Toast.makeText(getBaseContext(),"Press Back again to Exit",Toast.LENGTH_LONG).show();
        }
        backpressed = System.currentTimeMillis();
    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog2.dismiss();
                            startActivity(new Intent(MainActivity.this,CheckingUser.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                dialog2.dismiss();
                etOTP.requestFocus();
            }
        });
    }
    private void findViews() {
        btnGenerateOTP=findViewById(R.id.sendotp);
        btnSignIn=findViewById(R.id.loginbtn);
        etPhoneNumber=findViewById(R.id.etnumber);
        etOTP=findViewById(R.id.etotp);
    }
    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();

                etOTP.setText(code);





                Toast.makeText(MainActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                dialog.dismiss();
                verificationCode = s;
                Toast.makeText(MainActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
                etOTP.requestFocus();
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent i = new Intent(MainActivity.this,CheckingUser.class);
            startActivity(i);
        }
    }
}