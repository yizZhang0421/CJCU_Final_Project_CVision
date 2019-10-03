package com.cjcu.ima105.cvision.market.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cjcu.ima105.cvision.R;

import com.cjcu.ima105.cvision.market.Adapter.CategoryRankAdapter;


public class RankFragment extends Fragment {



    private View view;

    private Toolbar toolbar;
    private ViewPager viewPager;
    Typeface mTypeface;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.market_fragment_rank, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);




        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab().setText("模型"));
        tabLayout.addTab(tabLayout.newTab().setText("資料集"));




        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        CategoryRankAdapter adapter = new CategoryRankAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        //viewPager.setOffscreenPageLimit(2);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;



    }

    public void setCurrentTab(int i) {
        viewPager.setCurrentItem(i);
    }


//    private void setToolbar() {
//
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setDisplayHomeAsUpEnabled(false);
//
//        actionBar.setTitle("");


    }





