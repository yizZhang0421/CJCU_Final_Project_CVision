package com.cjcu.ima105.cvision;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjcu.ima105.cvision.market.customfonts.EditText_Roboto_Regular;
import com.cjcu.ima105.cvision.market.fragments.RankFragment;


public class Market extends AppCompatActivity {

    FrameLayout frameLayout;
    public static Context market_contect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_home);

        market_contect=this;

        final EditText_Roboto_Regular editText_roboto_regular = findViewById(R.id.store_keyword);
        editText_roboto_regular.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    replace_fragment(new RankFragment());
                    return true;
                }
                return false;
            }
        });
        ImageView cancel = findViewById(R.id.store_clear_keyword);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_roboto_regular.setText("");
            }
        });

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        replace_fragment(new RankFragment());
    }
    public void replace_fragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
