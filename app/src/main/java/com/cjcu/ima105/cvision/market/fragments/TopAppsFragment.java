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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.market_fragment_topapps, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        /*Recyclerview  code is here*/
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        homeTopAppsModelClasses = new ArrayList<>();


        Bundle bundle = this.getArguments();
        String[] image = (String[]) bundle.get("image");
        String[] number = (String[]) bundle.get("number");
        String[] title = (String[]) bundle.get("title");
        String[] view1 = (String[]) bundle.get("view1");
        String[] install = (String[]) bundle.get("install");
        String[] description = (String[]) bundle.get("description");
        String[] id = (String[]) bundle.get("id");
        String market = (String) bundle.get("market");
        for (int i = 0; i < image.length; i++) {
            HomeTopAppsModelClass beanClassForRecyclerView_contacts = new HomeTopAppsModelClass(image[i],number[i],title[i],view1[i],install[i], description[i], id[i], market);

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


