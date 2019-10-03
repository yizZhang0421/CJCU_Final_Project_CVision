package com.cjcu.ima105.cvision.market.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjcu.ima105.cvision.Market;
import com.cjcu.ima105.cvision.MarketItemDescription;
import com.cjcu.ima105.cvision.R;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.List;

import com.cjcu.ima105.cvision.market.ModelClass.HomeTopAppsModelClass;


/**
 * Created by Rp on 6/14/2016.
 */
public class RecycleAdapter_TopApps extends RecyclerView.Adapter<RecycleAdapter_TopApps.MyViewHolder> {
    Context context;


    private List<HomeTopAppsModelClass> moviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        PorterShapeImageView image;
        TextView number, title, view1, install, description;
        LinearLayout wraper;


        public MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            view1 = view.findViewById(R.id.view);
            install = view.findViewById(R.id.intall);
            number = view.findViewById(R.id.number);
            description = view.findViewById(R.id.description);
            wraper=view.findViewById(R.id.wraper);


        }

    }


    public RecycleAdapter_TopApps(Context mainActivityContacts, List<HomeTopAppsModelClass> moviesList) {
        this.moviesList = moviesList;
        this.context = mainActivityContacts;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.market_item_topapp_list, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final HomeTopAppsModelClass movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.view1.setText(movie.getView());
        holder.install.setText(movie.getInstall());
        holder.number.setText(movie.getNumber());
        holder.image.setImageResource(movie.getImage());

        holder.wraper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Market.market_contect, MarketItemDescription.class);
                intent.putExtra("image", movie.getImage());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("view1", movie.getView());
                intent.putExtra("install", movie.getInstall());
                intent.putExtra("description", movie.getDescription());
                Market.market_contect.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}


