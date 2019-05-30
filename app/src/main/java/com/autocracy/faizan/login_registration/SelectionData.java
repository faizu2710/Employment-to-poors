package com.autocracy.faizan.login_registration;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SelectionData {

    public SelectionData(){}

    public SelectionData(String experience,String userid)
    {
        this.userid = userid;
        this.experience = experience;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userId) {
        this.userid = userId;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    String userid;
    String experience;

}
