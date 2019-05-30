package com.autocracy.faizan.login_registration;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Jobs extends AppCompatActivity {
        private DrawerLayout drawerLayout;
        private ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userskills,companyName,username;
    SkillsMatchingJobs adapter;
    ArrayList<ListData> listData;
    ArrayList<String> keys;
    String skillmatch[];



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Jobs.this,HomepageUser.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawermenujobs,menu);
        MenuItem notification = menu.findItem(R.id.notification);
checkNotifications(menu);
// menu.getItem(4).setIcon(ContextCompat.getDrawable(Jobs.this,R.drawable.ic_notifications_red_24dp));
        notification.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Jobs.this,UserNotifications.class));

                return false;
            }
        });

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void checkNotifications(Menu menu) {
//        Checking Notifications
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        final DatabaseReference d1 = FirebaseDatabase.getInstance().getReference("notifications/"+userId);
        checking(d1,menu);

    }

    private void checking(final DatabaseReference d1, final Menu menu) {
        d1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds1.getChildren()) {

                        Boolean first = (Boolean) ds2.child("selected").getValue(Boolean.class);
                        Boolean second = (Boolean) ds2.child("view").getValue(Boolean.class);
                        Log.d("first is" + first, "second is" + second);
                        if (first) {
                            if (!(second)) {
                                Log.d("I am Inside", "if");
                                menu.getItem(1).setIcon(ContextCompat.getDrawable(Jobs.this, R.drawable.ic_notifications_red_24dp));

                            }

//                            else
//                            {
//                                Log.d("I am Inside","else");
//
//                                menu.getItem(4).setIcon(ContextCompat.getDrawable(Jobs.this,R.drawable.ic_notifications_black_24dp  ));
//
//                            }

                        }
                    }
//                    String key1 = ds1.getKey();
//
//                    DatabaseReference d2 = d1.child(key1);
//                    d2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
////                            Map<String,Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();
//                            for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
//                                Boolean xyz = (Boolean) ds2.getValue(Boolean.parseBoolean(ds2.getKey()));
//                                if ((Boolean) ds2.getValue(Boolean.parseBoolean(ds2.getKey()))) {
//                                    if (   !( ds2.child(ds2.getKey()).hasChild("view")))
//
//                                    menu.getItem(4).setIcon(ContextCompat.getDrawable(Jobs.this,R.drawable.ic_notifications_red_24dp));
//
//                                    break;
//                                }
//                                else
//                                {
//                                    menu.getItem(4).setIcon(ContextCompat.getDrawable(Jobs.this,R.drawable.ic_notifications_black_24dp  ));
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

//

        //        Menu starts
        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(Jobs.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
//        Setting user number in header
//        TextView userNumber = navigation.getHeaderView(0).findViewById(R.id.number);

        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        userNumber.setText(currentFirebaseUser.getPhoneNumber());


        databaseReference = FirebaseDatabase.getInstance().getReference("users/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentFirebaseUser.getUid()))
                {
//                   DatabaseReference db = databaseReference.child(currentFirebaseUser.getUid());
                    Log.d("is answer ","chalra hai");
                    username = dataSnapshot.child(currentFirebaseUser.getUid()).child("name").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        TextView un = navigation.getHeaderView(0).findViewById(R.id.username);
//        un.setText(username);
//        user number ends
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.logout:
                        new SweetAlertDialog(Jobs.this, SweetAlertDialog.WARNING_TYPE)
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
                                        Intent i = new Intent(Jobs.this, MainActivity.class);
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
                        startActivity(new Intent(Jobs.this, HomepageUser.class));
                        finish();
                        break;
                }
                return false;
            }
        });
//        Menu ends

//        Bottom nav bar starts
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottomnav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                     @Override
                     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                         switch (item.getItemId()) {
                             case R.id.profile:
                                 startActivity(new Intent(Jobs.this,JobUserProfile.class));

                                 finish();
                                 break;
                            case R.id.matching_jobs:
                                startActivity(new Intent(Jobs.this,MatchingJobs.class));

                                finish();
                                break;
                             case R.id.my_status:
                                 startActivity(new Intent(Jobs.this,JobStatus.class));
                                 finish();
                                 break;
//                            case R.id.all_jobs:
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
//        ends

        recyclerView = findViewById(R.id.joblist);
        LinearLayoutManager llm = new LinearLayoutManager(Jobs.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

//        Getting current user's skills - begin code
        databaseReference = FirebaseDatabase.getInstance().getReference("users/poors/"+userId+"/skills");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
                userskills =dataSnapshot.getValue(String.class);
                Log.d("skills is ",userskills);


//                Working code dont delete
                if (userskills.contains(","))
                    skillmatch = userskills.split(",");
                Log.d("split hua", String.valueOf(Arrays.toString(skillmatch)));
//                working code ends...


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("FEka re Feka ",String.valueOf(databaseError));
            }
        });


//        ends user skills code

//        listener starts

//        ends
        listData = new ArrayList<>();
        final ArrayList<ListData> list = new ArrayList<ListData>();
      final DatabaseReference  databaseReference1 = FirebaseDatabase.getInstance().getReference("companyPost");
      databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              Iterable<DataSnapshot> d = dataSnapshot.getChildren();
            int count=0;
              for (DataSnapshot v : d) {
                  String keyname = v.getKey();
                  DatabaseReference gettingname = FirebaseDatabase.getInstance().getReference("users/company/" + keyname + "/name");
                  gettingname.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          companyName = dataSnapshot.getValue(String.class);

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });

                  //code dekh upar ka aur kar complete..
                  count++;
                  DatabaseReference d2 = databaseReference1.child(String.valueOf(v.getKey()));
                  Log.d("key is on " + count, String.valueOf(v.getKey()));


//                  for (int i = 0; i < skillmatch.length; i++) {

                  Query q = d2.orderByChild("skills");
//                          .startAt(skillmatch[i]).endAt(skillmatch[i]+"\uf8ff");
                  q.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          for (DataSnapshot d : dataSnapshot.getChildren()) {
                              Log.d("values are : ", String.valueOf(d.getValue()));
                              ListData ld = d.getValue(ListData.class);
                              ld.setTitle(ld.getTitle());
                              ld.setLocation(ld.getLocation());
                              ld.setDatetime(ld.getDatetime());
//                            ld.setSkills(ld.getSkills());
                              ld.setSalaryfrom(ld.getSalaryfrom());
                              ld.setSalaryto(ld.getSalaryto());
                              ld.setOpenings(ld.getOpenings());
//                            ld.setParentid((ld.getParentid()));
                              listData.add(ld);


                          }

                          adapter = new SkillsMatchingJobs(Jobs.this, (ArrayList<ListData>) listData);
                          recyclerView.setAdapter(adapter);

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {
                          Toast.makeText(Jobs.this, (CharSequence) databaseError, Toast.LENGTH_LONG).show();
                          Log.d("error is ", String.valueOf(databaseError));
                      }
                  });
              }

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }



}
