package com.cjcu.ima105.cvision;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ModelPage extends AppCompatActivity {
    public static String model_name;
    public static ModelPage modelpage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_page);
        modelpage=this;

        TextView modelname=findViewById(R.id.mModel_page_model_name);
        modelname.setText(model_name);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void addTabs(ViewPager viewPager) {
        ModelPageFragmentPagerAdapter adapter = new ModelPageFragmentPagerAdapter(getSupportFragmentManager());

        ModelPageFragment fragment = new ModelPageFragment();
        Bundle args = new Bundle();
        args.putLong("layout", R.layout.model_page_labels);
        fragment.setArguments(args);
        adapter.addFrag(fragment, "類別");

        fragment = new ModelPageFragment();
        args = new Bundle();
        args.putLong("layout", R.layout.model_page_evaluate);
        fragment.setArguments(args);
        adapter.addFrag(fragment, "評估");

        fragment = new ModelPageFragment();
        args = new Bundle();
        args.putLong("layout", R.layout.model_page_train);
        fragment.setArguments(args);
        adapter.addFrag(fragment, "訓練");

        fragment = new ModelPageFragment();
        args = new Bundle();
        args.putLong("layout", R.layout.model_page_config);
        fragment.setArguments(args);
        adapter.addFrag(fragment, "設定");
        viewPager.setAdapter(adapter);
    }

}



class ModelPageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ModelPageFragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
