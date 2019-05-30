package com.autocracy.faizan.login_registration;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationsAdapter extends RecyclerView.Adapter<UserNotificationsAdapter.MyViewHolder> {

    Context context;
    ArrayList<NotificationModel> arrayList;
    String cn,jt;

    public UserNotificationsAdapter(Context c, List<NotificationModel> a)
    {
        this.context = c;
        this.arrayList = (ArrayList<NotificationModel>) a;
//        this.cn = cn;
//        this.jt = jt;
    }


    @NonNull
    @Override
    public UserNotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserNotificationsAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_notification_recycler,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserNotificationsAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.jobtitle.setText("For "+arrayList.get(i).getJobTitle());
//        Log.d("cn",cn);
//        Log.d("jt",jt);

        myViewHolder.cname.setText(arrayList.get(i).getCompanyName());
        String companyName = arrayList.get(i).getCompanyName();
        String ini = companyName.substring(0,1);
        myViewHolder.initial.setText(ini);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cname,jobtitle,initial;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        cname = itemView.findViewById(R.id.cname);
        jobtitle = itemView.findViewById(R.id.title);
        initial = itemView.findViewById(R.id.initial);
        }

    }
}
