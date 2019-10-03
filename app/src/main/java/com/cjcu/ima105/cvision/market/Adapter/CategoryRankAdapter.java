package com.cjcu.ima105.cvision.market.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cjcu.ima105.cvision.R;

import com.cjcu.ima105.cvision.market.fragments.TopAppsFragment;

/**
 * Created by wolfsoft on 10/11/2015.
 */
public class
CategoryRankAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public CategoryRankAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TopAppsFragment tab1 = new TopAppsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putIntArray("image", new int[]{R.drawable.market_cjcu_dog, R.drawable.market_flower, R.drawable.market_gothic});
                bundle1.putStringArray("number", new String[]{"1","2","3"});
                bundle1.putStringArray("title", new String[]{"CJCU校狗", "花類辨識","哥得體字元"});
                bundle1.putStringArray("view1", new String[]{"20", "123","12"});
                bundle1.putStringArray("install", new String[]{"h24563026@mailst.cjcu.edu.tw","z58774556@gmail.com","z58774556@gmail.com"});
                bundle1.putStringArray("description", new String[]{"長榮大學校狗辨識","",""});
                tab1.setArguments(bundle1);
                return tab1;

            case 1:
                TopAppsFragment tab2 = new TopAppsFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putIntArray("image", new int[]{R.drawable.market_cjcu_dog, R.drawable.market_flower});
                bundle2.putStringArray("number", new String[]{"1","2"});
                bundle2.putStringArray("title", new String[]{"CJCU校狗", "花"});
                bundle2.putStringArray("view1", new String[]{"88", "77"});
                bundle2.putStringArray("install", new String[]{"h24563026@mailst.cjcu.edu.tw", "z58774556@gmail.com"});
                bundle2.putStringArray("description", new String[]{"長榮大學校狗資料集\n有虎皮、朱利安、老米三類影像，每類有2000張",""});
                tab2.setArguments(bundle2);
                return tab2;

                default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}