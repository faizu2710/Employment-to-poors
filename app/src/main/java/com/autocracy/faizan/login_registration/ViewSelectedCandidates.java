package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewSelectedCandidates extends AppCompatActivity {
    String parentId,id,userId;
    FirebaseDatabase firebaseDatabase;
    public List<SelectionData> list;
    ViewSelectedCandidatesAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_candidates);
        parentId = getIntent().getExtras().getString("pid");
        id = getIntent().getExtras().getString("id");

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
                                startActivity(new Intent(ViewSelectedCandidates.this,CompanyUserProfile.class));
                                finish();
                                break;
                            case R.id.add_post:

                                startActivity(new Intent(ViewSelectedCandidates.this,NewCompanyPost.class));
                                finish();
                                break;
                            case R.id.my_posts:
                                startActivity(new Intent(ViewSelectedCandidates.this,ViewCompanyPosts.class));
                                finish();
                                break;
                            case R.id.applied_status:
                                startActivity(new Intent(ViewSelectedCandidates.this, CompanyPostStatus.class));
                                finish();
                                break;
                        }
                        return true;
                    }
                });
//        ends
        list = new ArrayList<SelectionData>();
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(ViewSelectedCandidates.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("selectedcandidates/"+parentId+"/"+id);
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

                            adapter = new ViewSelectedCandidatesAdapter(ViewSelectedCandidates.this, (ArrayList<SelectionData>) list);
                            recyclerView.setAdapter(adapter);

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
    }
}
