package com.cjcu.ima105.cvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class Frame extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)findViewById(R.id.home)).setClickable(false);
                Frame.this.startActivity(new Intent(Frame.this, Home.class));
                Frame.this.finish();
            }
        });
        findViewById(R.id.model).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)findViewById(R.id.model)).setClickable(false);
                Frame.this.startActivity(new Intent(Frame.this, ModelList.class));
                FrameLayout fl = findViewById(R.id.mFrameContent);
                Frame.this.finish();
            }
        });
        findViewById(R.id.predict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button)findViewById(R.id.predict)).setClickable(false);
                Frame.this.startActivity(new Intent(Frame.this, PredictCamera.class));
                FrameLayout fl = findViewById(R.id.mFrameContent);
                Frame.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && findViewById(R.id.home_fragment)==null) {
            Frame.this.startActivity(new Intent(Frame.this, Home.class));
            Frame.this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}