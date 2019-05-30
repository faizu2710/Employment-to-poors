package com.autocracy.faizan.login_registration;
import android.content.Intent;
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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Fetch extends AppCompatActivity {
    ArrayList<String> arrayList = new ArrayList<>();

    private ProgressDialog mProgressDialog;
    private String url = "http://www.indianngos.org/sitemap_City.aspx?city=MUMBAI";
    private ArrayList<String> mBlogTitleList = new ArrayList<>();

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(Fetch.this,HomepageCN.class));
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        new Description().execute();

        if(haveNetwork())
        {

        }
        else if (!haveNetwork())
        {
            Toast.makeText(Fetch.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean haveNetwork()
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


    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Fetch.this);
            mProgressDialog.setTitle("Searching NGOs");
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

                Elements mElementBlogTitle = mBlogDocument.select("div[class=panel-body]").select("a");  //mElementBlogTitle.eq(i);
                String mBlogTitle = mElementBlogTitle.text();
                Log.d("before",mBlogTitle);

                String [] cleanData = mBlogTitle.split("TRUST | FOUNDATION| MANDAL| PRATISTHAN| SANGH | ASSOCIATION | SANSTHA");
                Log.d("After ",Arrays.toString(cleanData));

                Elements mElementBlogDesc = mBlogDocument.select("div[class=panel-body]").select("a");
                int j =0;


                String mblogDesc[] = new String[mElementBlogDesc.size()];
                for (Element a : mElementBlogDesc) {

                    mblogDesc[j] = a.attr("href");
                    arrayList.add("http://www.indianngos.org/"+mblogDesc[j]);
                    j++;
                }

                Log.d("pura hora",Arrays.toString(cleanData));
                Log.d("link",(Arrays.toString(mblogDesc)));

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

            DataAdapter mDataAdapter = new DataAdapter(Fetch.this, mBlogTitleList,arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);

            mProgressDialog.dismiss();
        }

    }

}

