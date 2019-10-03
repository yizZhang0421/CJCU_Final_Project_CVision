package com.cjcu.ima105.cvision.market.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class MyTextView_Roboto_Black extends AppCompatTextView {

    public MyTextView_Roboto_Black(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView_Roboto_Black(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView_Roboto_Black(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
            setTypeface(tf);
        }
    }

}