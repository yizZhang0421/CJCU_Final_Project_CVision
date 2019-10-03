package com.cjcu.ima105.cvision.market.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cjcu.ima105.cvision.R;

import java.util.ArrayList;

import com.cjcu.ima105.cvision.market.Adapter.RecycleAdapter_TopApps;
import com.cjcu.ima105.cvision.market.ModelClass.HomeTopAppsModelClass;


public class TopAppsFragment extends Fragment {



    private View view;

    private LinearLayout linearLayout;

    private ArrayList<HomeTopAppsModelClass> homeTopAppsModelClasses;

    private RecyclerView recyclerView;
    private RecycleAdapter_TopApps mAdapter;


//    private Integer image[] = {R.drawable.square_img, R.drawable.square_img,R.drawable.square_img,R.drawable.square_img,R.drawable.square_img,R.drawable.square_img,R.drawable.square_img};
//    private String number[] = {"1","2","3","4","5","6","7"};
//    private String title[] = {"Facebook","Instagram","Racing","Music","Truecaller","Flipcart","Crickbuzz"};
//    private String view1[] = {"23.3M","35.3M","12.5M","45.4M","25.1M","66.0M","12.7M"};
//    private String install[] = {"Installed 8.5m time(s)","Installed 7.5m time(s)","Installed 15m time(s)","Installed 5m time(s)","Installed 8m time(s)","Installed 52m time(s)","Installed 3m time(s)"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.market_fragment_topapps, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        /*Recyclerview  code is here*/


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        homeTopAppsModelClasses = new ArrayList<>();


        Bundle bundle = this.getArguments();
        int[] image = (int[]) bundle.get("image");
        String[] number = (String[]) bundle.get("number");
        String[] title = (String[]) bundle.get("title");
        String[] view1 = (String[]) bundle.get("view1");
        String[] install = (String[]) bundle.get("install");
        String[] description = (String[]) bundle.get("description");
        for (int i = 0; i < image.length; i++) {
            HomeTopAppsModelClass beanClassForRecyclerView_contacts = new HomeTopAppsModelClass(image[i],number[i],title[i],view1[i],install[i], description[i]);

            homeTopAppsModelClasses.add(beanClassForRecyclerView_contacts);
        }


        mAdapter = new RecycleAdapter_TopApps(getActivity(),homeTopAppsModelClasses);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        return view;



    }

}


