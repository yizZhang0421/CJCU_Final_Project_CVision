package com.cjcu.ima105.cvision;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.cjcu.ima105.cvision.market.fragments.RankFragment;


public class Market extends AppCompatActivity {

    FrameLayout frameLayout;
    public static Context market_contect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_home);

        market_contect=this;

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        replace_fragment(new RankFragment());
    }
    public void replace_fragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }
}
