package com.autocracy.faizan.login_registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.MyViewHolder> {
    Context context;
    ArrayList<ListData> arrayList;

    public AppliedJobsAdapter(Context context, ArrayList<ListData> listData) {
        this.context = context;
        this.arrayList = listData;
    }

    @NonNull
    @Override
    public AppliedJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppliedJobsAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selection_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobsAdapter.MyViewHolder myViewHolder, final int i) {

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
        myViewHolder.count.setText(String.valueOf(i+1));
        myViewHolder.name.setText(arrayList.get(i).getTitle());
        myViewHolder.location.setText(arrayList.get(i).getLocation());
        myViewHolder.salary.setText(arrayList.get(i).getSalaryfrom()+"-"+arrayList.get(i).getSalaryto()+" \u20B9");

        myViewHolder.l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(context,ViewSpecificJobPost.class);
                intent.putExtra("parentid",arrayList.get(i).getParentid());
                intent.putExtra("jobtitle",arrayList.get(i).getTitle());

                intent.putExtra("joblocation",arrayList.get(i).getLocation());

                intent.putExtra("description",arrayList.get(i).getDescription());

                intent.putExtra("openings",arrayList.get(i).getOpenings());

                intent.putExtra("salaryfrom",arrayList.get(i).getSalaryfrom());
                intent.putExtra("salaryto",arrayList.get(i).getSalaryto());

                intent.putExtra("skills",arrayList.get(i).getSkills());
                intent.putExtra("contact",arrayList.get(i).getContact());
                intent.putExtra("id",arrayList.get(i).getId());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, salary, count;
        LinearLayout l, l3;
        CardView c;
        RelativeLayout r;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.experience);
            salary = itemView.findViewById(R.id.skills);
            count = itemView.findViewById(R.id.count);
            c = itemView.findViewById(R.id.card);
            l = itemView.findViewById(R.id.list_root);
            l3 =itemView.findViewById(R.id.l3);



        }
    }
}
