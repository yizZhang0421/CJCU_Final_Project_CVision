package com.cjcu.ima105.cvision.market.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjcu.ima105.cvision.LabelPage;
import com.cjcu.ima105.cvision.MarketItemDescription;
import com.cjcu.ima105.cvision.R;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.Line;

public class RecycleAdapter_MarketDescription extends RecyclerView.Adapter<RecycleAdapter_MarketDescription.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        ImageView imageView;
        public MyViewHolder(LinearLayout linearLayout, TextView textView, ImageView imageView) {
            super(linearLayout);
            this.linearLayout=linearLayout;
            this.textView=textView;
            this.imageView=imageView;
        }
    }

    private ArrayList<Bitmap> samples;
    private ArrayList<String> sample_names;
    Context context;
    public RecycleAdapter_MarketDescription(Context mainActivityContacts, ArrayList<Bitmap> samples, ArrayList<String> sample_names) {
        this.samples = samples;
        this.sample_names=sample_names;
        this.context = mainActivityContacts;
    }

    @NonNull
    @Override
    public RecycleAdapter_MarketDescription.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView snapshot = (ImageView) v;
                Dialog listDialog = new Dialog(context);
                View showview = ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.full_image_show, null, false);
                ImageView imageView = showview.findViewById(R.id.imageview);
                imageView.setImageDrawable(snapshot.getDrawable());
                imageView.setVisibility(View.VISIBLE);
                showview.findViewById(R.id.loading).setVisibility(View.GONE);
                listDialog.setContentView(showview);
                listDialog.setCancelable(true);
                listDialog.show();
                Window window = listDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.CENTER;
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                listDialog.getWindow().setAttributes(lp);
            }
        });
        AppCompatTextView textView = new AppCompatTextView(context);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);
        linearLayout.addView(imageView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER|Gravity.CENTER_VERTICAL;
        linearLayout.setLayoutParams(params);
        return new MyViewHolder(linearLayout, textView, imageView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.textView.setText(sample_names.get(i));
        viewHolder.imageView.setImageBitmap(samples.get(i));
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }
}
