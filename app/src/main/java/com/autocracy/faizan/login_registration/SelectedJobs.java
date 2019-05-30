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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectedJobs extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
   public ArrayList<ListData> listData;
    RecyclerView recyclerView;
    AppliedJobsAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SelectedJobs.this,JobStatus.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_jobs);
        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(SelectedJobs.this, drawerLayout, R.string.open, R.string.close);
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
                        new SweetAlertDialog(SelectedJobs.this, SweetAlertDialog.WARNING_TYPE)
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
                                        Intent i = new Intent(SelectedJobs.this, MainActivity.class);
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
                        startActivity(new Intent(SelectedJobs.this, HomepageUser.class));
                        finish();
                        break;
                }
                return false;
            }
        });

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomnav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile:
                                startActivity(new Intent(SelectedJobs.this,JobUserProfile.class));

                                finish();
                                break;
                            case R.id.matching_jobs:
                                startActivity(new Intent(SelectedJobs.this,MatchingJobs.class));

                                finish();
                                break;
                            case R.id.all_jobs:
                                startActivity(new Intent(SelectedJobs.this,Jobs.class));
                                finish();
                                break;
//                               allJobs();
//                                Menu m2 = bottomNavigationView.getMenu();
//                                MenuItem mi2 = m2.getItem(0);
//                                mi2.setChecked(true);
//                                break;
//                            case R.id.action_item3:
//                                selectedFragment = ItemThreeFragment.newInstance();
//                                break;
//                        }

                        }
                        return true;
                    }
                });

        recyclerView = findViewById(R.id.appliedjoblist);
        LinearLayoutManager llm = new LinearLayoutManager(SelectedJobs.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = currentUser.getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("selectedcandidates");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot d1 : dataSnapshot.getChildren())
                {
                    for (final DataSnapshot d2 : d1.getChildren()) {
                        if (d2.hasChild(userid)) {
                            Query q1 = d1.getRef().orderByChild(userid);
                            q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("datafound2", dataSnapshot.toString());
                                    String parentid = d1.getKey();
                                    String id = d2.getKey();
                                    Log.d("pid", parentid+id);

                                    showData(parentid,id);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            private void showData(String parentid, String id) {
                listData = new ArrayList<>();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("companyPost/"+parentid+"/"+id);
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("dddd",dataSnapshot.toString());
                        ListData ld = dataSnapshot.getValue(ListData.class);
                        listData.add(ld);
                        adapter = new AppliedJobsAdapter(SelectedJobs.this,listData);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
