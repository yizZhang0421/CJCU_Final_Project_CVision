package com.cjcu.ima105.cvision.market.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditText_Roboto_Meidum extends AppCompatEditText {

    public EditText_Roboto_Meidum(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_Roboto_Meidum(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_Roboto_Meidum(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
            setTypeface(tf);
        }
    }

}