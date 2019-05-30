package com.autocracy.faizan.login_registration;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
//            private final List<Fragment> fragmentList = new ArrayList<>();
//            private final List<String> fragmenttitle = new ArrayList<>();


    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                EditProfile editProfile = new EditProfile();
                return editProfile;
            case 1:
                QualificationDetails qualificationDetails = new QualificationDetails();
                return qualificationDetails;
            case 2:
                ExtraDetails extraDetails = new ExtraDetails();
                return extraDetails;
        }
        return null;

    }

    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment, String title) {
//        fragmentList.add(fragment);
//        fragmenttitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0)
        {
           title = "Personal";
        }
        else if (position == 1)title = "Qualification";
            else if (position == 2)title = "Other Details";
        return title;
    }
    public void AddFragment(Fragment fragment, String Title){
//        fragmentList.add(fragment);
//        fragmenttitle.add(Title);

    }
}
