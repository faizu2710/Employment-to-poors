package com.autocracy.faizan.login_registration;

public class ScholarshipTypesModel {

    public ScholarshipTypesModel(String t, int n)
    {
        this.type = t;
        this.img = n;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    String type;
    int img;
}
