package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;
public class ScholarshipMainpage extends AppCompatActivity {
    private long backpressed;

    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(getBaseContext(), "Press Back Again To exit", Toast.LENGTH_LONG).show();
        }
        backpressed = System.currentTimeMillis();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_mainpage);final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        fragment=new Home();
                        loadFragment(fragment);
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem menuItem1 = menu.getItem(0);
                        menuItem1.setChecked(true);
                        break;
                    case R.id.action_search:
//                    toolbar.setTitle("Search");
                        fragment = new Search();
                        loadFragment(fragment);
                        Menu menu1 = bottomNavigationView.getMenu();
                        menuItem = menu1.getItem(1);
                        menuItem.setChecked(true);
                        break;
                    case R.id.navigation_scholarship:
                        fragment = new Scholarships();
                        loadFragment(fragment);
                        Menu menu2 = bottomNavigationView.getMenu();
                        menuItem = menu2.getItem(2);
                        menuItem.setChecked(true);
                        break;
                    case R.id.navigation_profile:
                        fragment = new Profile();
                        loadFragment(fragment);
                        Menu menu3 = bottomNavigationView.getMenu();
                        menuItem = menu3.getItem(3);
                        menuItem.setChecked(true);

                        break;
                }
                return false;
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

            String no = getIntent().getExtras().getString("openProfile");
            int n = Integer.parseInt(no);
            Log.d("recieved ", String.valueOf(n));
            if (n == 10) {
                Fragment fragment = new Profile();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {

                Fragment fragment = new Home();
                loadFragment(fragment);
            }


    }
}
