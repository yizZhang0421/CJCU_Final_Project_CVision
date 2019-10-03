package com.cjcu.ima105.cvision;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeBrocastFragment extends Fragment {

    public static Fragment newInstance(Activity context, int position, String[] item_content, int[] color) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putCharSequenceArray("itemContent", item_content);
        bundle.putIntArray("color", color);
        return Fragment.instantiate(context, HomeBrocastFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        LinearLayout linearLayout = (LinearLayout)inflater.inflate(R.layout.home_brocast_item, container, false);
        int position = this.getArguments().getInt("position");
        String text = (String) this.getArguments().getCharSequenceArray("itemContent")[position];
        int color = this.getArguments().getIntArray("color")[position];

        ConstraintLayout item_container = linearLayout.findViewById(R.id.item_contener);
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(15.0f);
        item_container.setBackground(background);


        TextView textView = (TextView) linearLayout.findViewById(R.id.item_text);
        textView.setText(text);

        return linearLayout;
    }
}
