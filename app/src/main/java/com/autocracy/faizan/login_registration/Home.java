package com.autocracy.faizan.login_registration;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Home extends Fragment {

    ViewPager viewPager2;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private ActionBar toolbar;
    private long backpressed;
    RecyclerView recyclerView;
    ScholarshipTypesAdapter adapter;
    ArrayList<ScholarshipTypesModel>arrayList;
    int i;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int image[] = {R.mipmap.pqr,R.mipmap.efg,R.mipmap.abc,R.mipmap.abc,R.mipmap.talent,R.mipmap.school,R.mipmap.minority,R.mipmap.disabled};
        String text[] = {"Merit Based","International","Means Based","College Based","School","Minority","Talent","Disabled"};

        recyclerView = view.findViewById(R.id.rc1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        for (int i=0;i<image.length;i++) {
            arrayList.add(new ScholarshipTypesModel(text[i], image[i]));

            adapter = new ScholarshipTypesAdapter(getContext(),arrayList);
            recyclerView.setAdapter(adapter);
            Log.d("Inside","loop"+image.length);
        }




        final ViewPager viewPager2 = view.findViewById(R.id.viewPager2);
        //viewPager2 = findViewById(R.id.viewPager2);
        sliderDotspanel = view.findViewById(R.id.SliderDots);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getContext());
        viewPager2.setAdapter(viewPageAdapter);

        dotscount = viewPageAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new myTimerTask(), 4000, 4000);
//          runnable.run();
//        ImageView i1 = view.findViewById(R.id.img1);
//        i1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Meritbased.class));
//            }
//        });
//        ImageView i2 = view.findViewById(R.id.img2);
//        i2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Collegebased.class));
//            }
//        });
//        ImageView i3 = view.findViewById(R.id.img3);
//        i3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Meansbased.class));
//            }
//        });
//        ImageView i4 = view.findViewById(R.id.img4);
//        i4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), International.class));
//            }
//        });
//        ImageView i5 = view.findViewById(R.id.img5);
//        i5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Talent.class));
//            }
//        });

    }


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);

    }


    //    public class myTimerTask extends TimerTask {
//        @Override
//        public void run() {
//
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (viewPager2.getCurrentItem() == 0) {
//                        viewPager2.setCurrentItem(1);
//                    } else if (viewPager2.getCurrentItem() == 1) {
//                        viewPager2.setCurrentItem(2);
//                    } else {
//                        viewPager2.setCurrentItem(0);
//                    }
//
//                }
//            });
//        }
//    }
    private ViewPager vPager;

    // the pager adapter that provides the pages to the view pager widget

    private ViewPageAdapter vAdapter;

    private Handler handler=new Handler();
    private Runnable runnale=new Runnable(){
        public void run(){
            int position = vPager.getCurrentItem();
            vPager.setCurrentItem(position,true);
            if(position>3 ) position=0;
            else position++;
            // Move to the next page after 10s
            handler.postDelayed(runnale, 10000);
        }
    };
}
