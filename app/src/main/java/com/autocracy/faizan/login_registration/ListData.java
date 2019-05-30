package com.autocracy.faizan.login_registration;

public class ListData  {


    private String parentid;
    private String title;
    private String location;
    private String contact;
    private String description;
    private String id;
    private String openings;
    private String salaryfrom;
    private String salaryto;
    private String skills;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    private String datetime;

    public ListData(){}

    public ListData(String contact,String description,String id,String location,String openings,String parentid,String salaryfrom,String salaryto,String skills,String title,String datetime){
    this.contact = contact;
        this.description = description;
        this.id = id;
        this.location = location;
        this.openings = openings;
        this.salaryfrom = salaryfrom;
        this.salaryto= salaryto;
        this.skills = skills;
        this.title = title;
        this.datetime = datetime;
   }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenings() {
        return openings;
    }

    public void setOpenings(String openings) {
        this.openings = openings;
    }

    public String getSalaryfrom() {
        return salaryfrom;
    }

    public void setSalaryfrom(String salaryfrom) {
        this.salaryfrom = salaryfrom;
    }

    public String getSalaryto() {
        return salaryto;
    }

    public void setSalaryto(String salaryto) {
        this.salaryto = salaryto;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
