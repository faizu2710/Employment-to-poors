package com.autocracy.faizan.login_registration;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GovernmentPrograms extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private String url = "https://indiagen.in/indian-government-schemes/";
    private ArrayList<String> mBlogTitleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_programs);
        new Description().execute();
    }
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(GovernmentPrograms.this);
            mProgressDialog.setTitle("Searching Goverment Programmes");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document mBlogDocument = Jsoup.connect(url).get();
                // Using Elements to get the Meta data

                Elements mElementBlogTitle = mBlogDocument.select("div[class=entry-content]").select("h2");  //mElementBlogTitle.eq(i);
                String mBlogTitle = mElementBlogTitle.text();
                String [] cleanData = mBlogTitle.split("#");

                Log.d("pura hora",Arrays.toString(cleanData));

                for(int i=0; i<cleanData.length;i++) {


                    mBlogTitleList.add(cleanData[i]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.act_recyclerview);

            AdapterForGovernmentPrograms mDataAdapter = new AdapterForGovernmentPrograms(GovernmentPrograms.this, mBlogTitleList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);

            mProgressDialog.dismiss();
        }

    }

}
