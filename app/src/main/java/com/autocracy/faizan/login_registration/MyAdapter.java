package com.autocracy.faizan.login_registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListData> arrayList;
    public MyAdapter(Context c, ArrayList<ListData> a){
        this.context = c;
        this.arrayList = a;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.postedjobs,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        int[] colors1 = {Color.parseColor("#240b36"),Color.parseColor("#c31432")};
        int[] colors2 = {Color.parseColor("#F37335"),Color.parseColor("#FDC830")};
        int[] colors3 = {Color.parseColor("#000046"),Color.parseColor("#1CB5E0")};

        GradientDrawable gd1 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors1);
        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors2);
        GradientDrawable gd3 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors3);

        if (i%3==0)
        {
            myViewHolder.c.setBackground(gd1);
            myViewHolder.l.setBackground(gd1);

        }
        else if(i%3==1)
        {
            myViewHolder.c.setBackground(gd2);
            myViewHolder.l.setBackground(gd2);

        }
        else if(i%3==2){
            myViewHolder.c.setBackground(gd3);
            myViewHolder.l.setBackground(gd3);
           
        }

        final String t,d,s,sf,st,l,c,o,child,id,parentid;
        final String stringdatetime;
        t = arrayList.get(i).getTitle();
        d = arrayList.get(i).getDescription();

        s = arrayList.get(i).getSkills();

        sf = arrayList.get(i).getSalaryfrom();
        st = arrayList.get(i).getSalaryto();

        l = arrayList.get(i).getLocation();

        c = arrayList.get(i).getContact();

        o = arrayList.get(i).getOpenings();
        id = arrayList.get(i).getId();
        parentid = arrayList.get(i).getParentid();
        child = String.valueOf(i+1);
        stringdatetime = arrayList.get(i).getDatetime();



        myViewHolder.title.setText(arrayList.get(i).getTitle());
        myViewHolder.location.setText(arrayList.get(i).getLocation());

        myViewHolder.count.setText(String.valueOf(i+1));
       myViewHolder.datetime.setText(arrayList.get(i).getDatetime());

//        myViewHolder.datetime.setText(dateString);
        myViewHolder.viewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewSpecificCompanyPost.class);
                intent.putExtra("ti",t);
                intent.putExtra("de",d);
                intent.putExtra("sk",s);
                intent.putExtra("sfr",sf);
                intent.putExtra("sto",st);
                intent.putExtra("co",c);
                intent.putExtra("op",o);
                intent.putExtra("lo",l);
                intent.putExtra("childr",id);
                context.startActivity(intent);
//                ((ViewSpecificCompanyPost)context).finish();


//                context.startActivity(new Intent(context,ViewSpecificCompanyPost.class));

            }
        });

        myViewHolder.deletepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Deleting..")
                        .setContentText("Are You sure ?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = currentUser.getUid();
                                deletePost(parentid,id,userId);
                                context.startActivity(new Intent(context,ViewCompanyPosts.class));
                                ((Activity)context).finish();



//                                Toast.makeText(context,"Post id is "+id,Toast.LENGTH_LONG).show();


                            }
                        }).setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                    }
                })
                        .show();


            }

            private void deletePost(final String parentid, final String id, String userId) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("companyPost/"+userId+"/"+id);
                databaseReference.removeValue();

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentid+"/"+id);
                databaseReference1.removeValue();

                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("appliedJobs");
                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d1 : dataSnapshot.getChildren())
                        {
                            for (DataSnapshot d2 : d1.getChildren())
                            {

                                if (d2.getKey().equals(parentid)) {
                                    Log.d("ddddd",d2.getKey());
                                    databaseReference2.child(d1.getKey()).child(d2.getKey()).child(id).setValue(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("selectedcandidates/"+parentid+"/"+id);
                databaseReference3.removeValue();

                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("notifications/");
                databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d1 : dataSnapshot.getChildren())
                        {
                        if (d1.hasChild(parentid))
                        {
                            for (DataSnapshot d2 : d1.getChildren())
                            {
                                if (d2.hasChild(id))
                                {
                                    d2.child(id).getRef().removeValue();
                                    Log.d("Removed Value is ","");
                                }
                            }
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,location,count,datetime;
        Button viewPost,deletepost;
        LinearLayout l;
        CardView c;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            count = itemView.findViewById(R.id.count);
            viewPost = itemView.findViewById(R.id.viewpost);
            deletepost = itemView.findViewById(R.id.deletepost);
            datetime = itemView.findViewById(R.id.datetime);
            c = itemView.findViewById(R.id.card);
            l = itemView.findViewById(R.id.list_root);

        }
    }
    }
