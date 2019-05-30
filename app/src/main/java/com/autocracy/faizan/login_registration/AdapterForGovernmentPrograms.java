package com.autocracy.faizan.login_registration;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterForGovernmentPrograms extends RecyclerView.Adapter<AdapterForGovernmentPrograms.MyViewHolder> {

    private ArrayList<String> mBlogTitleList = new ArrayList<>();


    private Context c;
    private int lastPosition = -1;

    public AdapterForGovernmentPrograms(Context context, ArrayList<String> mBlogTitleList){
        this.c = context;
        this.mBlogTitleList = mBlogTitleList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_blog_title;

        public MyViewHolder(View view) {
            super(view);
            tv_blog_title = view.findViewById(R.id.row_tv_blog_title);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_blog_title.setText(mBlogTitleList.get(position));

    }

    @Override
    public int getItemCount() {
        return mBlogTitleList.size();

    }

}