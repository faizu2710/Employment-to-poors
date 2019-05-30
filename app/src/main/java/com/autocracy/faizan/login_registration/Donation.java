package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Donation extends AppCompatActivity {
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        startActivity(new Intent(Donation.this,HomepageCN.class));
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

       findViewById(R.id.money).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paytm.com/helpinghand"));
               startActivity(browserIntent);


           }
       });


        findViewById(R.id.material).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donation.this,Fetch.class));
                finish();
            }
        });
    }
}
