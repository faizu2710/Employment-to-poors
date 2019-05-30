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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SkillsMatchingJobs extends RecyclerView.Adapter<SkillsMatchingJobs.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<ListData> jobarrayList;
    ArrayList<ListData>jobarrayListFull;
    String userId;
public String c;
    public SkillsMatchingJobs(Context c, ArrayList<ListData> a){
        this.context = c;
        this.jobarrayList = a;
        this.jobarrayListFull = new ArrayList<>(a);



    }
    @NonNull
    @Override
    public SkillsMatchingJobs.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SkillsMatchingJobs.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skills_matching_jobs,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SkillsMatchingJobs.MyViewHolder myViewHolder, int i) {

//
        int[] colors1 = {Color.parseColor("#240b36"),Color.parseColor("#c31432")};
//        Dark shade of color 1 --
        int[] colors2 = {Color.parseColor("#F37335"),Color.parseColor("#FDC830")};
//        Dark shade of color2 -- Color.parseColor("#F37335"),
        int[] colors3 = {Color.parseColor("#000046"),Color.parseColor("#1CB5E0")};
//        Dark shade of color 3 -- Color.parseColor("#000046"),

        GradientDrawable gd1 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors1);
        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors2);
        GradientDrawable gd3 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors3);

        gd1.setCornerRadius(0f);
        gd2.setCornerRadius(0f);
        gd3.setCornerRadius(0f);




        final String parentId,title,location,description,openings,salaryfrom,salaryto,skills,contact,id;

        parentId = jobarrayList.get(i).getParentid();
        title = jobarrayList.get(i).getTitle();
        location = jobarrayList.get(i).getLocation();
        salaryfrom = jobarrayList.get(i).getSalaryfrom();
        salaryto = jobarrayList.get(i).getSalaryto();
        openings = jobarrayList.get(i).getOpenings();
        description = jobarrayList.get(i).getDescription();
        skills = jobarrayList.get(i).getSkills();
        contact = jobarrayList.get(i).getContact();
        id = jobarrayList.get(i).getId();

        //        checking if user already applied or not (start)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();
        final DatabaseReference d = FirebaseDatabase.getInstance().getReference("appliedCandidates/"+parentId+"/"+id); /*+"/userid"*/
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> d1 = dataSnapshot.getChildren();
//              for (DataSnapshot d2 : d1)
//              {
//              }
                String check = String.valueOf(dataSnapshot.getKey());
                Log.d("dekho ", check);



                if(dataSnapshot.hasChild(userId))
                {
                    Log.d("mile is","matched");
                    myViewHolder.applied.setVisibility(View.VISIBLE);
                }

                else
                {
                    myViewHolder.applied.setVisibility(View.GONE);

                    Log.d("nai ","mila");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        ends

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("selectedcandidates/"+parentId+"/"+id);
        Log.d("link",databaseReference.toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.hasChild(userId)) {
                        myViewHolder.applied.setText("Selected");
                        myViewHolder.applied.setVisibility(View.VISIBLE);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myViewHolder.jobtitle.setText(jobarrayList.get(i).getTitle());
        myViewHolder.complocation.setText(jobarrayList.get(i).getLocation());
//        myViewHolder.compSkillsRequired.setText("Skills : "+jobarrayList.get(i).getSkills());
        myViewHolder.compSalary.setText(jobarrayList.get(i).getSalaryfrom()+"-"+jobarrayList.get(i).getSalaryto());
        myViewHolder.jobdatetime.setText("Posted on : "+jobarrayList.get(i).getDatetime());
//        myViewHolder.openings.setText(jobarrayList.get(i).getOpenings());
//        myViewHolder.jobcount.setText(String.valueOf(i+1));

        if (i%3==0)
        {
//            myViewHolder.c.setBackground(gd1);
            myViewHolder.l.setBackground(gd1);
//            myViewHolder.r.setBackground(gd1);
        }
        else if(i%3==1)
        {
//            myViewHolder.c.setBackground(gd2);
            myViewHolder.l.setBackground(gd2);
//            myViewHolder.r.setBackground(gd2);
        }
        else if(i%3==2){
//            myViewHolder.c.setBackground(gd3);
            myViewHolder.l.setBackground(gd3);
//            myViewHolder.r.setBackground(gd3);
        }


        myViewHolder.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(context,ViewSpecificJobPost.class);
                i.putExtra("parentid",parentId);
                i.putExtra("jobtitle",title);

                i.putExtra("joblocation",location);

                i.putExtra("description",description);

                i.putExtra("openings",openings);

                i.putExtra("salaryfrom",salaryfrom);
                i.putExtra("salaryto",salaryto);

                i.putExtra("skills",skills);
                i.putExtra("contact",contact);
                i.putExtra("id",id);

                context.startActivity(i);

            }
        });





//        myViewHolder.compname.setText("Company name : "+c);








    }

    @Override
    public int getItemCount() {
        return jobarrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView jobtitle,openings,complocation,compSkillsRequired,compSalary,jobcount,jobdatetime,applied,view;
        CardView c;
        LinearLayout l,l2;
        RelativeLayout r;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            jobtitle = itemView.findViewById(R.id.jobtitle);
//            openings = itemView.findViewById(R.id.openings);

            complocation = itemView.findViewById(R.id.complocation);
//            compSkillsRequired = itemView.findViewById(R.id.compSkillsRequired);
            compSalary = itemView.findViewById(R.id.compSalary);
//            jobcount = itemView.findViewById(R.id.jobcount);
            c = itemView.findViewById(R.id.bg);
            jobdatetime = itemView.findViewById(R.id.jobdatetime);
            l = itemView.findViewById(R.id.list_root);
            l2 = itemView.findViewById(R.id.l2);
            r = itemView.findViewById(R.id.rbackg);
            applied = itemView.findViewById(R.id.applied);


        }
    }


    @Override
    public Filter getFilter() {
        return jobFilter;
    }
    private Filter jobFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           ArrayList<ListData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length()==0)
            {
                filteredList.addAll(jobarrayListFull);
            }
            else

            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ListData l : jobarrayListFull)
                {
                    if (l.getTitle().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(l);
                    }
                    else if (l.getSkills().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(l);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            jobarrayList.clear();
            jobarrayList.addAll((Collection<? extends ListData>) results.values);
            notifyDataSetChanged();
        }
    };
}
