package com.autocracy.faizan.login_registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelectionOfCandidates extends AppCompatActivity {
    String parentId,id,userId;
    FirebaseDatabase firebaseDatabase;
    public List<SelectionData> list;
    SelectionAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<String> deleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_of_candidates);
        parentId = getIntent().getExtras().getString("pid");
        id = getIntent().getExtras().getString("id");

        Log.d("pid",parentId);
        Log.d("id",id);

// Popup for giving instruction to user begins
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        Boolean ifshowDialog = preferences.getBoolean("showDialog",true);

        if (ifshowDialog)
        {
            showDialog();
        }
//ends
        //        Bottom nav bar starts
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
                            case R.id.c_profile:
                                startActivity(new Intent(SelectionOfCandidates.this,CompanyUserProfile.class));
                                finish();
                                break;
                            case R.id.add_post:

                                startActivity(new Intent(SelectionOfCandidates.this,NewCompanyPost.class));
                                finish();
                                break;
                            case R.id.my_posts:
                                startActivity(new Intent(SelectionOfCandidates.this,ViewCompanyPosts.class));
                                finish();
                                break;
                            case R.id.selected:
                                startActivity(new Intent(SelectionOfCandidates.this, SelectedCandidates.class));
                                finish();
                                break;
                        }
                        return true;
                    }
                });
//        ends
            list = new ArrayList<SelectionData>();
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(SelectionOfCandidates.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> i1 = dataSnapshot.getChildren();


               for (DataSnapshot d : i1)
               {
                    String key = d.getKey();
                   Log.d("outside ",key);

                   DatabaseReference d1 = databaseReference.child(key);
                   Log.d("ref",d1.toString());

                   d1.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                           Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
//                           Log.d("expe is",String.valueOf(map.get("experience")));
//                           Log.d("expeuser is",String.valueOf(map.get("userid")));



                           SelectionData selectionData = dataSnapshot.getValue(SelectionData.class);
//                               Log.d("abcd", selectionData.getExperience());
                               selectionData.setExperience(selectionData.getExperience());
                               selectionData.setUserid(selectionData.getUserid());
                               Log.d("abcd", selectionData.getUserid());

                               list.add(selectionData);
                               Log.d("selection on ",String.valueOf(selectionData));

                           adapter = new SelectionAdapter(SelectionOfCandidates.this, (ArrayList<SelectionData>) list);
                           recyclerView.setAdapter(adapter);




//                           new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//                               @Override
//                               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                                   return false;
//                               }
//
//                               @Override
//                               public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
//
//                                 int position = viewHolder.getAdapterPosition();
//                                 String uid = adapter.arrayList.get(position).getUserid();
//                                   list.remove(viewHolder.getAdapterPosition());
//                                   adapter.notifyItemRemoved(position);
//                                   adapter.notifyDataSetChanged();
//
//                                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id+"/"+uid);
//                                   databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                       @Override
//                                       public void onSuccess(Void aVoid) {
//                                           Toast.makeText(SelectionOfCandidates.this,"Deleted From Database",Toast.LENGTH_LONG).show();
//                                       }
//
//                                   })
//                                           .addOnFailureListener(new OnFailureListener() {
//                                               @Override
//                                               public void onFailure(@NonNull Exception e) {
//                                                   Toast.makeText(SelectionOfCandidates.this,"Unable to delete From Database",Toast.LENGTH_LONG).show();
//
//                                               }
//                                           });
//
//
////                                   Toast.makeText(SelectionOfCandidates.this,"Rejected",Toast.LENGTH_LONG).show();
////                                   adapter.notify();
//
//
//                               }
//                           }).attachToRecyclerView(recyclerView);
//
//                           new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
//                               @Override
//                               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                                   return false;
//                               }
//
//                               @Override
//                               public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//
//
//                                   list.remove(viewHolder.getAdapterPosition());
//                                   adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                                   adapter.notifyDataSetChanged();
//
//                                   Toast.makeText(SelectionOfCandidates.this,"Selected",Toast.LENGTH_LONG).show();
//
//
//                               }
//                           }).attachToRecyclerView(recyclerView);

                       }



                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });


               }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        deleteData = new ArrayList<>();
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d("Removing",String.valueOf(FirebaseDatabase.getInstance().getReference("notifications/"+adapter.arrayList.get(viewHolder.getAdapterPosition()).getUserid()+"/"+parentId+"/"+id)));

                if (i == ItemTouchHelper.LEFT) {
                    Log.d("inside","left");
                    String uid = adapter.arrayList.get(viewHolder.getAdapterPosition()).getUserid();
                    Log.d("nikla", uid);
//                    Toast.makeText(SelectionOfCandidates.this, "Swiped at " + viewHolder.getAdapterPosition(), Toast.LENGTH_LONG).show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("appliedCandidates/" + parentId + "/" + id);
                    databaseReference.child(uid).setValue(null);

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("appliedJobs/"+uid+"/"+parentId+"/"+id);
                    databaseReference1.setValue(false);

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("notifications/"+uid+"/"+parentId+"/"+id);
                    Log.d("removing link",databaseReference2.toString());
                    databaseReference2.child("selected").setValue(false);
                    databaseReference2.child("view").setValue(false);



                    adapter.removeItem(viewHolder.getAdapterPosition());

                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                    adapter.notifyDataSetChanged();
//                adapter.notifyDataSetChanged();

                }
                else if(i == ItemTouchHelper.RIGHT)
                {

                    String uid = adapter.arrayList.get(viewHolder.getAdapterPosition()).getUserid();
                    String exp = adapter.arrayList.get(viewHolder.getAdapterPosition()).getExperience();
//            FOr notifying users
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications/"+uid+"/"+parentId+"/"+id);
                    databaseReference.child("selected").setValue(true);
                    databaseReference.child("view").setValue(false);
//                    Selected Candidates

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("selectedcandidates/"+parentId+"/"+id+"/"+uid);
                    databaseReference1.child("userid").setValue(uid);
                    databaseReference1.child("experience").setValue(exp);

//                    Removing selected from applied candidates (Company end)
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("appliedCandidates/" + parentId + "/" + id);
                    databaseReference2.child(uid).setValue(null);

//                    Removing selected from applied jobs (User end)
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("appliedJobs/"+uid+"/"+parentId+"/"+id);
                    databaseReference3.setValue(false);

                    adapter.removeItem(viewHolder.getAdapterPosition());

                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                    adapter.notifyDataSetChanged();
//                    ends



//                    For reducing openings
                    final DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("companyPost/"+parentId+"/"+id+"/openings");
                    databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int opening = Integer.parseInt(dataSnapshot.getValue(String.class));
                            Log.d("open",String.valueOf(opening));
                            if (opening!=0)
                            databaseReference4.setValue(String.valueOf(opening-1));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
//                    ends

                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

//        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id);
//                for (String u : deleteData)
//               databaseReference.child(u).setValue(null);
//            }
//        });



//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//                               @Override
//                               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                                   return false;
//                               }
//
//                               @Override
//                               public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
//
//                                 int position = viewHolder.getAdapterPosition();
//                                 String uid = adapter.arrayList.get(position).getUserid();
//
//
//                                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id+"/"+uid);
//                                   databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                       @Override
//                                       public void onSuccess(Void aVoid) {
//                                           Toast.makeText(SelectionOfCandidates.this,"Deleted From Database",Toast.LENGTH_LONG).show();
//                                       }
//
//                                   })
//                                           .addOnFailureListener(new OnFailureListener() {
//                                               @Override
//                                               public void onFailure(@NonNull Exception e) {
//                                                   Toast.makeText(SelectionOfCandidates.this,"Unable to delete From Database",Toast.LENGTH_LONG).show();
//
//                                               }
//                                           });
//
//                                            adapter.removeItem(position);
//                                   Toast.makeText(SelectionOfCandidates.this,"Rejected",Toast.LENGTH_LONG).show();
//
//
//                               }
//                           }).attachToRecyclerView(recyclerView);

// ------------------------------------------------ Right swipe Code Below-----------------------------------------------


//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
//                               @Override
//                               public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                                   return false;
//                               }
//
//                               @Override
//                               public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//
//
//                                   list.remove(viewHolder.getAdapterPosition());
//                                   adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                                   adapter.notifyDataSetChanged();
//
//                                   Toast.makeText(SelectionOfCandidates.this,"Selected",Toast.LENGTH_LONG).show();
//
//
//                               }
//                           }).attachToRecyclerView(recyclerView);

    }

    private void showDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(SelectionOfCandidates.this);
        alert.
                setCancelable(false)
                .setMessage("Swipe right to select candidates. \nLeft to reject Candidates.\nClick on specific post if you want to see thier details")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Don't Show Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences preferences = getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("firstLaunch",false);
                        editor.apply();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
