package com.autocracy.faizan.login_registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
public class ScholarshipModule extends AppCompatActivity {

    ViewPager viewPager;
            LinearLayout sliderDotspanel;
private int dotscount;
private ImageView[] dots;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_module);
        Button button1 = findViewById(R.id.skip);
        button1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(ScholarshipModule.this, ScholarshipLogin2.class);
        startActivity(intent);
        finish();

        }
        });

        Button button = findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        //Intent intent = new Intent(MainActivity.this, Mainpage.class);
        //startActivity(intent);
        int c = viewPager.getCurrentItem();
        if (c==3)
        {
        startActivity(new Intent(ScholarshipModule.this,ScholarshipLogin2.class));
        finish();
        }
        viewPager.setCurrentItem(getItem(+1), true);

        }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {

        dots[i] = new ImageView(this);
        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);
        sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
@Override
public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

@Override
public void onPageSelected(int position) {
        for (int i = 0; i < dotscount; i++) {
        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        }

@Override
public void onPageScrollStateChanged(int state) {
        }

        });
        }

private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
        }
        }
