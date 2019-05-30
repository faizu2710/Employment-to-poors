package com.autocracy.faizan.login_registration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScholarshipTypesAdapter extends RecyclerView.Adapter<ScholarshipTypesAdapter.MyViewHolder>  {
    Context c;
   ArrayList<ScholarshipTypesModel>arrayList;

    public ScholarshipTypesAdapter(Context context, ArrayList<ScholarshipTypesModel>a){
        this.c = context;
        this.arrayList = a;


    }
    @NonNull
    @Override
    public ScholarshipTypesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        return new ScholarshipTypesAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.types,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScholarshipTypesAdapter.MyViewHolder myViewHolder, final int i) {
        Log.d("array",String.valueOf(arrayList.get(i).getImg()));
        Log.d("array2",String.valueOf(R.drawable.abc));

        myViewHolder.i1.setImageResource(arrayList.get(i).getImg());
        myViewHolder.t1.setText(arrayList.get(i).getType());

            myViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (arrayList.get(i).getType())
                    {
                        case "College Based":
                            Intent intent = new Intent(c,CollegeBased.class);
                            c.startActivity(intent);
                            break;
                        case "Merit Based":
                            c.startActivity(new Intent(c,MeritBased.class));
                            break;
                        case "Means Based":
                            c.startActivity(new Intent(c,MeansBased.class));

                            break;
                        case "International":
                            c.startActivity( new Intent(c,International.class));
                            break;
                        case "Talent":
                            c.startActivity(new Intent(c,Talent.class));
                            break;
                        case "Minority":
                            c.startActivity(new Intent(c,Minority.class));
                            break;
                        case "School":
                            c.startActivity(new Intent(c,School.class));
                            break;
                        case "Disabled":
                            c.startActivity(new Intent(c,Disabled.class));
                            break;

                    }
                }
            });
//        switch (i)
//        {
//            case 0:
//                myViewHolder.i1.setImageResource(R.drawable.abc);
//        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1;
        ImageView i1;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.text1);
            i1 = itemView.findViewById(R.id.img1);
            card = itemView.findViewById(R.id.card);

//            Log.d("bindview",t+String.valueOf(i));
        }
    }
}
