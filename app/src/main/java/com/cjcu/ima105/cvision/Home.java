package com.cjcu.ima105.cvision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class Home extends Frame {
    public static int PAGES = 0;
    public static int FIRST_PAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup parent = (ViewGroup) findViewById(R.id.mFrameContent);
        parent.removeAllViews();
        parent.addView(getLayoutInflater().inflate(R.layout.home, parent, false), 0);
        ((Button)findViewById(R.id.home)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.home_click, 0, 0);



        Toolbar toolbar = new Toolbar(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        LinearLayout.LayoutParams toolBarParams = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT,height/4);
        toolBarParams.gravity=Gravity.CENTER;
        toolbar.setLayoutParams(toolBarParams);
        toolbar.setBackground(getDrawable(R.drawable.homepage_action_bar));
        View toolbar_content = getLayoutInflater().inflate(R.layout.home_page_toolbar,null);
        Toolbar.LayoutParams contentLayout=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        contentLayout.gravity=Gravity.CENTER;
        toolbar_content.setLayoutParams(contentLayout);
        toolbar.addView(toolbar_content);
        final RelativeLayout toolbar_contenter = findViewById(R.id.home_page_toolbar);
        toolbar_contenter.addView(toolbar);
        final ImageView userPhoto = findViewById(R.id.googleUserPhoto);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection conn = new URL(CurrentPosition.photoUri).openConnection();
                    conn.connect();
                    InputStream isCover = conn.getInputStream();
                    Bitmap bmpCover = BitmapFactory.decodeStream(isCover);
                    isCover.close();
                    userPhoto.setImageBitmap(bmpCover);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView userEmail = findViewById(R.id.googleUserEmail);
        userEmail.setText(CurrentPosition.email);
        Button googleSignOutPage = findViewById(R.id.googleSignOutPage);
        googleSignOutPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.startActivity(new Intent(Home.this, SignOut.class));
                SignOut.home=Home.this;
                //with animation
            }
        });


        PAGES = 5;
        ViewPager brocast = findViewById(R.id.home_brocast);
        CustomPagerAdapter mAdapter = new CustomPagerAdapter(this, this.getSupportFragmentManager(),
                new String[]{"about server","some inform","Hello~","BEARB","bala bala"},
                new int[]{Color.parseColor("#8efe87"),Color.parseColor("#43c5f1"),Color.parseColor("#fed487"),Color.parseColor("#8efe87"),Color.parseColor("#43c5f1")}
                );
        brocast.setAdapter(mAdapter);
        brocast.setPageTransformer(false, mAdapter);
        brocast.setCurrentItem(FIRST_PAGE);
        brocast.setOffscreenPageLimit(3);
        brocast.setPageMargin(-150);

        LinearLayout button_progress_list = findViewById(R.id.button_progress_list);
        button_progress_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.startActivity(new Intent(Home.this, ProgressList.class));
            }
        });

        LinearLayout button_market = findViewById(R.id.home_layout_store);
        button_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.startActivity(new Intent(Home.this, Market.class));
            }
        });



    }
}
class CustomPagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    private Activity mContext;
    private FragmentManager mFragmentManager;
    private float mScale;
    private String[] item_content;
    private int[] color;

    public CustomPagerAdapter(Activity context, FragmentManager fragmentManager, String[] item_content, int[] color) {
        super(fragmentManager);
        this.mFragmentManager = fragmentManager;
        this.mContext = context;
        this.item_content=item_content;
        this.color=color;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeBrocastFragment.newInstance(mContext, position, item_content, color);
    }

    @Override
    public int getCount() {
        return Home.PAGES;
    }

    @Override
    public void transformPage(View page, float position) {
    }
}

