package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectedCandidates extends AppCompatActivity {
    RecyclerView recyclerView;
    public List<ListData> list;
    public DatabaseReference databaseReference;
    MyAdapterForSelectedCandidates adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_candidates);

        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(SelectedCandidates.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.logout:
                        new SweetAlertDialog(SelectedCandidates.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Logging out..")
                                .setContentText("Are you sure ??")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog
//                                            .setTitleText("Deleted!")
//                                            .setContentText("Your imaginary file has been deleted!")
//                                            .setConfirmText("OK")
//                                            .setConfirmClickListener(null)
//                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(SelectedCandidates.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    }

//
                                }).setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })

                                .show();


                        break;
                    case R.id.home:
                        startActivity(new Intent(SelectedCandidates.this, HomepageCN.class));
                        finish();
                        break;
                }
                return false;
            }
        });
//        Menu ends


        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomnav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.c_profile:
                                startActivity(new Intent(SelectedCandidates.this, CompanyUserProfile.class));
                                finish();
                                break;
                            case R.id.add_post:

                                startActivity(new Intent(SelectedCandidates.this, NewCompanyPost.class));
                                finish();
                                break;
                            case R.id.applied_status:
                                startActivity(new Intent(SelectedCandidates.this, CompanyPostStatus.class));
                                finish();
                                break;

                            case R.id.my_posts:
                                startActivity(new Intent(SelectedCandidates.this, ViewCompanyPosts.class));
                                finish();
                                break;

                        }
                        return true;
                    }
                });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("companyPost/"+userId);



        recyclerView = findViewById(R.id.rview);
        LinearLayoutManager llm = new LinearLayoutManager(SelectedCandidates.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);



        list = new ArrayList<ListData>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    new SweetAlertDialog(SelectedCandidates.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops !!")
                            .setContentText("You don't have any posts!!")
                            .setConfirmText("Add post")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog
//                                            .setTitleText("Deleted!")
//                                            .setContentText("Your imaginary file has been deleted!")
//                                            .setConfirmText("OK")
//                                            .setConfirmClickListener(null)
//                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    startActivity(new Intent(SelectedCandidates.this, NewCompanyPost.class));
                                    finish();

                                }

//
                            }).setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
//                            startActivity(new Intent(CompanyPostStatus.this, Homepage.class));
//                            finish();
                        }
                    })

                            .show();


                } else {

                    list = new ArrayList<>();
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();


                    for (DataSnapshot d1 : dataSnapshots) {
                        ListData listData = d1.getValue(ListData.class);

                        Log.d("title is", listData.getTitle());
//
                        listData.setLocation(listData.getLocation());
                        listData.setTitle(listData.getTitle());
                        list.add(listData);


                    }
                        adapter = new MyAdapterForSelectedCandidates(SelectedCandidates.this,list);
    //                    adapter = new MyAdapterForCompanyPostStatus(CompanyPostStatus.this, (ArrayList<ListData>) list);
                        recyclerView.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
                Toast.makeText(SelectedCandidates.this, (CharSequence) databaseError, Toast.LENGTH_LONG).show();
                Log.d("error is ", String.valueOf(databaseError));
            }
        });

    }
}







