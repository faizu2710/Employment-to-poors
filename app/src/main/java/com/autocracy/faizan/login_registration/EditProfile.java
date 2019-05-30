package com.autocracy.faizan.login_registration;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    ImageView imageView;
    ImageView image_profile;
    private  static  final int IMAGE_REQUEST = 1;
    private  Uri imageUri;
    private StorageTask uploadTask;
    private  StorageReference storageReference;

    private TabLayout tabLayout;
    private Toolbar toolbar;
    FragmentViewPagerAdapter adapter;
    private ViewPager viewPager;

    View view;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser, userid;
    TextView ename, egender, eaddress, eage,econtact, elandline,eemail;
    TextView tnumber;
    String name, number, gender, address, age,email,contact,landline;
    Button update;

    public EditProfile() {
        // Required empty public constructor
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if(isRemoving()){
//            Fragment fragment = new Profile();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.myprofile, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

//        firebaseDatabase.setPersistenceEnabled(true);
////        firebaseDatabase.setPersistenceEnabled(true);
//        toolbar = view.findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//        tabLayout = view.findViewById(R.id.tablayout);
//        viewPager = view.findViewById(R.id.viewPager_id);
//        adapter= new FragmentViewPagerAdapter(getFragmentManager());
//
//        viewPager.setAdapter(adapter);
////        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentFirebaseUser.getUid();
        findViews(view);
        getSetValues();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    private void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i,IMAGE_REQUEST);

    }

    private  String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private  void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading Your Profile Picture");
        pd.show();


        if(imageUri != null){
            final StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    +"."+ getFileExtension(imageUri));
            uploadTask = filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();

                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
//
                                Uri downloadUri = task.getResult();
                                Log.d("hi",task.getResult().toString());
                                String mUri = downloadUri.toString();

                                DatabaseReference d = firebaseDatabase.getReference("scholarship/"+userid+"/imageUri");

                                d.setValue(mUri);
                                pd.dismiss();
                            }else{
                                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    })
            ;
        }else{
//                    Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }

    private void updateProfile() {
        name = ename.getText().toString();
        address = eaddress.getText().toString();
        age = eage.getText().toString();
//        number = enumber.getText().toString();
        contact = tnumber.getText().toString();
        landline = elandline.getText().toString();
        gender = egender.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ename.requestFocus();
            ename.setError("Enter your name");
        } else if (TextUtils.isEmpty(age)) {
            eage.requestFocus();
            eage.setError("Enter your age");
        } else if (TextUtils.isEmpty(gender)) {
            egender.requestFocus();
            egender.setError("Enter your gender");
        } else if (TextUtils.isEmpty(address)) {
            eaddress.requestFocus();
            eaddress.setError("Enter your address");
        } else if (TextUtils.isEmpty(contact)) {
            econtact.requestFocus();
            econtact.setError("Enter your contact number");
        } else if (TextUtils.isEmpty(landline)) {
            elandline.requestFocus();
            elandline.setError("Enter your landline number");
        }else if (contact.length()>10 || contact.length()<10) {
            econtact.requestFocus();
            econtact.setError("Please enter correct contact");
        }else if (landline.length()>12 || landline.length()<12) {
            elandline.requestFocus();
            elandline.setError("Please enter correct contact");
        }
        else {
            databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
            databaseReference.child("name").setValue(name);
            databaseReference.child("address").setValue(address);
            databaseReference.child("age").setValue(age);
            databaseReference.child("gender").setValue(gender);
            databaseReference.child("contact").setValue(contact);
            databaseReference.child("landline").setValue(landline);

            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Done!").setContentText("Profile is Updated !").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
//                    Profile profile = new Profile();

                    Intent i = new Intent(getActivity(),ScholarshipMainpage.class);
                    i.putExtra("openProfile","10");
                    startActivity(i);
//                    Fragment fragment = new Profile();
//                    FragmentTransaction transaction = ((Mainpage)getActivity()).getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.frame_container, fragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    Intent i = new Intent(getActivity(),Mainpage.class);
//                    startActivity(i);
                }
            }).show();
        }
    }

    private void getSetValues() {
        databaseReference = firebaseDatabase.getReference("scholarship/" + userid);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final DatabaseReference databaseReference1 = firebaseDatabase.getReference("scholarship/"+userid);
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
                                image_profile.setImageBitmap(bitmap);

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


//    private void swapFragment() {
//        QualificationDetails newfragment = new QualificationDetails();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame_container, newfragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //      FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }
    private void findViews(View view) {
        ename = view.findViewById(R.id.display_name);
        tnumber = view.findViewById(R.id.contact);
        egender = view.findViewById(R.id.gender);
        eaddress = view.findViewById(R.id.address);
        eage = view.findViewById(R.id.age);
        image_profile = view.findViewById(R.id.profile_image);

//        eemail = view.findViewById(R.id.display_email);
//                eskills = view.findViewById(R.id.contact);
        update = view.findViewById(R.id.update);
        elandline = view.findViewById(R.id.landline);
    }

}
