package com.cjcu.ima105.cvision.market.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.cjcu.ima105.cvision.CurrentPosition;
import com.cjcu.ima105.cvision.LabelPage;
import com.cjcu.ima105.cvision.Market;
import com.cjcu.ima105.cvision.R;

import com.cjcu.ima105.cvision.ServerOperate;
import com.cjcu.ima105.cvision.market.customfonts.EditText_Roboto_Regular;
import com.cjcu.ima105.cvision.market.fragments.TopAppsFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wolfsoft on 10/11/2015.
 */
public class CategoryRankAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public CategoryRankAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        String keyword = ((EditText_Roboto_Regular)((AppCompatActivity)Market.market_contect).findViewById(R.id.store_keyword)).getText().toString();
        switch (position) {
            case 0:
                TopAppsFragment tab1 = new TopAppsFragment();
                ArrayList<HashMap<String, String>> model_store = ServerOperate.model_store(keyword);
                Bundle bundle1 = new Bundle();
                String[] image = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    ArrayList<HashMap<String, String>> samples = ServerOperate.model_samples(model_store.get(i).get("id"), "128");
                    String base64 = samples.get(0).get("base64");
                    byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    if(bitmap.getHeight()>bitmap.getWidth()){
//                        bitmap=Bitmap.createScaledBitmap(bitmap, 128*bitmap.getWidth()/bitmap.getHeight(), 128, false);
//                    }
//                    else if(bitmap.getHeight()<bitmap.getWidth()){
//                        bitmap=Bitmap.createScaledBitmap(bitmap, 128, 128*bitmap.getHeight()/bitmap.getWidth(), false);
//                    }
                    Bitmap square = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(square);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(bitmap, (128 - bitmap.getWidth()) / 2, (128 - bitmap.getHeight()) / 2, null);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    square.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    bytes = byteArrayOutputStream .toByteArray();
                    image[i]=Base64.encodeToString(bytes, Base64.DEFAULT);
                }
                bundle1.putStringArray("image", image);

                String[] number = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    number[i]=(i+1)+"";
                }
                bundle1.putStringArray("number", number);

                String[] title = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    title[i]=model_store.get(i).get("name");
                }
                bundle1.putStringArray("title", title);

                String[] view1 = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    view1[i]="0";
                }
                bundle1.putStringArray("view1", view1);

                String[] install = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    install[i]=model_store.get(i).get("email");
                }
                bundle1.putStringArray("install", install);

                String[] description = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    description[i]=model_store.get(i).get("description");
                }
                bundle1.putStringArray("description", description);

                String[] id = new String[model_store.size()];
                for(int i=0;i<model_store.size();i++) {
                    id[i]=model_store.get(i).get("id");
                }
                bundle1.putStringArray("id", id);

                bundle1.putString("market", "model");
                tab1.setArguments(bundle1);
                return tab1;

            case 1:
                TopAppsFragment tab2 = new TopAppsFragment();
                ArrayList<HashMap<String, String>> label_store = ServerOperate.label_store(keyword);
                Bundle bundle2 = new Bundle();
                String[] image2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    ArrayList<HashMap<String, String>> samples = ServerOperate.label_samples(label_store.get(i).get("id"), "128");
                    String base64 = samples.get(0).get("base64");
                    byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    if(bitmap.getHeight()>bitmap.getWidth()){
//                        bitmap=Bitmap.createScaledBitmap(bitmap, 128*bitmap.getWidth()/bitmap.getHeight(), 128, false);
//                    }
//                    else if(bitmap.getHeight()<bitmap.getWidth()){
//                        bitmap=Bitmap.createScaledBitmap(bitmap, 128, 128*bitmap.getHeight()/bitmap.getWidth(), false);
//                    }
                    Bitmap square = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(square);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(bitmap, (128 - bitmap.getWidth()) / 2, (128 - bitmap.getHeight()) / 2, null);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    square.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    bytes = byteArrayOutputStream .toByteArray();
                    image2[i]=Base64.encodeToString(bytes, Base64.DEFAULT);
                }
                bundle2.putStringArray("image", image2);

                String[] number2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    number2[i]=(i+1)+"";
                }
                bundle2.putStringArray("number", number2);

                String[] title2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    title2[i]=label_store.get(i).get("name");
                }
                bundle2.putStringArray("title", title2);

                String[] view12 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    view12[i]="0";
                }
                bundle2.putStringArray("view1", view12);

                String[] install2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    install2[i]=label_store.get(i).get("email");
                }
                bundle2.putStringArray("install", install2);

                String[] description2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    description2[i]=label_store.get(i).get("description");
                }
                bundle2.putStringArray("description", description2);

                String[] id2 = new String[label_store.size()];
                for(int i=0;i<label_store.size();i++) {
                    id2[i]=label_store.get(i).get("id");
                }
                bundle2.putStringArray("id", id2);

                bundle2.putString("market", "label");
                tab2.setArguments(bundle2);
                return tab2;

                default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}