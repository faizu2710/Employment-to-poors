package com.autocracy.faizan.login_registration;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.MyViewHolder> {


    Context context;
    ArrayList<SelectionData> arrayList;
    public String userid,experience;
    public SelectionAdapter(Context c, ArrayList<SelectionData> a){
        this.context = c;
        this.arrayList = a;

    }

    @NonNull
    @Override
    public SelectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelectionAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selection_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectionAdapter.MyViewHolder myViewHolder, final int i) {


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


//        public String ,experience;
        Log.d("pos is",String.valueOf(i));
        userid = arrayList.get(i).getUserid();
        experience = arrayList.get(i).getExperience();
        Log.d("userid is",userid);
        myViewHolder.experience.setText("Experience : "+arrayList.get(i).getExperience() +" years");
        Log.d("exp is",experience);



        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/poors/"+arrayList.get(i).getUserid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();

                    String name = map.get("name");
//                    Log.d("os",map.get("name").toString());
                    myViewHolder.name.setText(name);
                 String skills = map.get("skills");
                 myViewHolder.skills.setText("Skills : "+skills);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            myViewHolder.count.setText(String.valueOf(i+1));

            myViewHolder.l3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u = arrayList.get(i).getUserid();
                    Intent i = new Intent(context,ViewAppliedCandidateInfo.class);
                    i.putExtra("userid",u);
                    Log.d("clicked id is ",u);

                    context.startActivity(i);
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void removeItem(int position)
    {
        String uid = arrayList.get(position).getUserid();
        Log.d("Removed at",String.valueOf(position));
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,skills,experience,count;
        LinearLayout l,l3;
        CardView c;
        RelativeLayout r;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);


            name = itemView.findViewById(R.id.name);
            skills = itemView.findViewById(R.id.skills);
            count = itemView.findViewById(R.id.count);
            experience = itemView.findViewById(R.id.experience);
            l = itemView.findViewById(R.id.list_root);
            l3 = itemView.findViewById(R.id.l3);

            r = itemView.findViewById(R.id.root);
            c = itemView.findViewById(R.id.card);
    }
    }
}
