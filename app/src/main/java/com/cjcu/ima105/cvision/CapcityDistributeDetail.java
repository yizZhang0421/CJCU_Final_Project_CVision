package com.cjcu.ima105.cvision;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjcu.ima105.cvision.Frame;
import com.cjcu.ima105.cvision.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class CapcityDistributeDetail extends Frame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup parent = (ViewGroup) findViewById(R.id.mFrameContent);
        parent.removeAllViews();
        parent.addView(getLayoutInflater().inflate(R.layout.capcity, parent, false), 0);

        final PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(10, Color.BLUE));
        pieData.add(new SliceValue(20, Color.GRAY));
        pieData.add(new SliceValue(10, Color.RED));
        pieData.add(new SliceValue(60, Color.MAGENTA));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setValueLabelTextSize(16);
        pieChartView.setPieChartData(pieChartData);
        pieChartView.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                TextView detail = findViewById(R.id.mDetail);
                detail.setText((value.getValue()+""));
            }
            @Override
            public void onValueDeselected() {
            }
        });

    }
}