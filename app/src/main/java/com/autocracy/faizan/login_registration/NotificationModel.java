package com.autocracy.faizan.login_registration;
public class NotificationModel
{

    public NotificationModel(String companyName,String jobTitle){
        this.companyName = companyName;
        this.jobTitle = jobTitle;

    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    String companyName;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    String jobTitle;
}
