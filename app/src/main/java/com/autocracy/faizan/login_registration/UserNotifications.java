package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class UserNotifications extends AppCompatActivity {
    RecyclerView recyclerView;
    public List<NotificationModel> list;
    public DatabaseReference databaseReference;
    UserNotificationsAdapter adapter;
    String userid;
    static String cname,jtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentuser.getUid();

        recyclerView = findViewById(R.id.rview);
        final LinearLayoutManager llm = new LinearLayoutManager(UserNotifications.this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications/"+userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d1 : dataSnapshot.getChildren()) {

                    for (DataSnapshot d2 : d1.getChildren()) {
                        Log.d("datafound",d2.toString());
                        Boolean see = d2.child("selected").getValue(Boolean.class);
                        Boolean see2 = d2.child("view").getValue(Boolean.class);
//                        Log.d("value is",d2.getValue(Boolean.class).toString());

                        if (see) {
//                            count++;
                            if (see) {
//                                Boolean id = d2.child().getValue(Boolean.class);
                                final String parentId = d1.getKey();
                                final NotificationModel n;

                                DatabaseReference d = FirebaseDatabase.getInstance().getReference("companyPost/" + parentId + "/" + d2.getKey());
                                Log.d("ceo",d.toString());
                                d.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
//                                        n.setJobTitle(map.get("title"));
                                        String jobtitle = map.get("title");
                                        Log.d("titleisss",map.get("title"));
                                        gettingCname(jobtitle,parentId);
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
//                        if (count==0)
//                        {
//                            new SweetAlertDialog(UserNotifications.this, SweetAlertDialog.NORMAL_TYPE)
//                                    .setTitleText("No New Notifications!!")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sDialog) {
////                                    sDialog
////                                            .setTitleText("Deleted!")
////                                            .setContentText("Your imaginary file has been deleted!")
////                                            .setConfirmText("OK")
////                                            .setConfirmClickListener(null)
////                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
////                                            FirebaseAuth.getInstance().signOut();
//                                            Intent i = new Intent(UserNotifications.this, Jobs.class);
//                                            startActivity(i);
//                                            finish();
//
//                                        }
//
////
//                                    }).show();
//                        }
                    }
                }
            }

            private void gettingCname(final String jobtitle, String parentId) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/company/" + parentId);
                db.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        n.setCompanyName(dataSnapshot.child("name").getValue().toString());
                        Log.d("cnameis",dataSnapshot.child("name").getValue().toString());
                        String comname = dataSnapshot.child("name").getValue(String.class);
                        settingAdapter(comname,jobtitle);

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


    private void settingAdapter(String cname,String jobtitle)
    {

//        Log.d("Class c",cname);
        list.add(new NotificationModel(cname,jobtitle));
//        Log.d("update",list.get(0).toString());
        adapter = new UserNotificationsAdapter(UserNotifications.this, list);
        recyclerView.setAdapter(adapter);
        userNotified();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserNotifications.this,Jobs.class));
        finish();
    }

    private void userNotified() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d1 : dataSnapshot.getChildren())
                {
                    for (DataSnapshot d2 : d1.getChildren())
                    {                                Log.d("d1key",d2.getKey());

                        Boolean first = (Boolean) d2.child("selected").getValue(Boolean.class);
                        Boolean second = (Boolean) d2.child("view").getValue(Boolean.class);
                        if (first && !(second))
                            {

                                databaseReference.child(d1.getKey()).child(d2.getKey()).child("view").setValue(true);
                            }

//                        if (d2.getValue(Boolean.class))
//                        {
//                            Log.d("keymili",d2.getKey());
//                            databaseReference.child(d1.getKey()).child(d2.getKey()).child("view").setValue(false);
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
