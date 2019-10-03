package com.cjcu.ima105.cvision;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

public class ResultPage extends AppCompatActivity {
    public static ImageView capture_result;
    public static SpinKitView result_loading;
    public static TextView predict_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        capture_result=findViewById(R.id.imageview_capture_result);
        result_loading=findViewById(R.id.spinkitview_result_loading);
        predict_result=findViewById(R.id.textview_predict_result);

    }
}
