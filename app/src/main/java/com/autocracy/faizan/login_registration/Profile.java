package com.autocracy.faizan.login_registration;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.zip.Inflater;



public class Profile extends Fragment{
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView imageView;
    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser, userid;
    TextView ename, egender, eaddress, eage, elandline,eemail;
    TextView tnumber;
    String name, number, gender, address, age,email, landline;
    Button update;

    DrawerLayout drawerLayout;


    public Profile() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //firebase user
        firebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentFirebaseUser.getUid();
        findViews(view);
        getSetValues();


        drawerLayout = view.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) getContext(), drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

//        actionBarDrawerToggle.syncState();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = view.findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem != null) {
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), ScholarshipLogin.class));
                        getActivity().finish();
                }
                return false;
            }
        });
        ImageView b1 = view.findViewById(R.id.edit_profile);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),MyProfile.class));
                getActivity().finish();
            }
        });
    }
    private void swapFragment(){
        EditProfile newfragment = new EditProfile();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, newfragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void getSetValues() {

        databaseReference = firebaseDatabase.getReference("scholarship/"+userid);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();

                name = data.get("name");
                ename.setText(name);
                Log.d("name is", name);
                number = data.get("contact");
                tnumber.setText(number);
                gender = data.get("gender");
                egender.setText(gender);
                address = data.get("address");
                eaddress.setText(address);
                age = data.get("age");
                eage.setText(age);
                landline = data.get("landline");
                elandline.setText(landline);
                email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                eemail.setText(email);


                final DatabaseReference databaseReference1 = firebaseDatabase.getReference("scholarship/"+userid);
                databaseReference1.keepSynced(true);
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("imageUri")) {
                            String uri = dataSnapshot.child("imageUri").getValue(String.class);

                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageView.setImageBitmap(bitmap);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            } catch (IOException e) {
                            }
                        }
                    }




                    @Override public void onCancelled (@NonNull DatabaseError databaseError){

                    }

                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void findViews(View view) {
        ename = view.findViewById(R.id.display_name);
        tnumber = view.findViewById(R.id.contact);
        egender = view.findViewById(R.id.gender);
        eaddress = view.findViewById(R.id.address);
        eage = view.findViewById(R.id.age);
        eemail = view.findViewById(R.id.display_email);
//                eskills = view.findViewById(R.id.contact);
//                update = view.findViewById(R.id.update);
        elandline = view.findViewById(R.id.landline);
        imageView = view.findViewById(R.id.profile_image);

    }

}
