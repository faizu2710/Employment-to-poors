package com.autocracy.faizan.login_registration;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MyProfile extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    FragmentViewPagerAdapter adapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager_id);
        adapter= new FragmentViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
//        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

//    }
//    private void setupViewPager(ViewPager viewPager)
//
//    {
//        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
//        adapter.AddFragment(new EditProfile(), "Personal Details");
//        adapter.AddFragment(new QualificationDetails(), "Education Details");
//        adapter.AddFragment(new ExtraDetails(), "Other Details");
//        viewPager.setAdapter(adapter);
//
//    }

    }
}
