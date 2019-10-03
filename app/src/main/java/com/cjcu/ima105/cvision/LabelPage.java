package com.cjcu.ima105.cvision;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class LabelPage extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionMode actionMode;
    private GridView gridview;
    private ProgressBar progressBar;
    private LabelImageAdapter labelImageAdapter;
    private boolean on_selection_mode;
    private HashSet<myImageView> selected_imageview;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label_page);

        toolbar = (Toolbar) findViewById(R.id.label_page_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(CurrentPosition.label_name);
        setSupportActionBar(toolbar);
        on_selection_mode=false;
        selected_imageview=new HashSet<>();

        progressBar = (ProgressBar)findViewById(R.id.label_page_loading);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        gridview = (GridView) findViewById(R.id.label_image_gridview);
        labelImageAdapter = new LabelImageAdapter(LabelPage.this);
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                on_selection_mode=true;
                actionMode = LabelPage.this.startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                        fab.hide();
                        MenuInflater inflater = actionMode.getMenuInflater();
                        inflater.inflate(R.menu.select_action_mode_menu, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                        boolean ret = false;
                        if (menuItem.getItemId() == R.id.delete) {
                            for(myImageView i : selected_imageview){
                                final myImageView delete_image_view = i;
                                labelImageAdapter.images_id.remove(delete_image_view.image_id);
                                labelImageAdapter.imagesid_imageview_map.remove(delete_image_view.image_id);
                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{

                                            MessageServerResponse delete_image_response = ServerOperate.delete_image(CurrentPosition.key, CurrentPosition.label_id, delete_image_view.image_id);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                t.start();
                            }
                            labelImageAdapter.notifyDataSetChanged();
                            actionMode.finish();
                            ret = true;
                        }
                        return ret;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {
                        for(myImageView i : selected_imageview){
                            i.selectedView.setVisibility(View.GONE);
                        }
                        selected_imageview=new HashSet<>();
                        fab.show();
                        on_selection_mode=false;
                    }
                });
                myImageView myImageView = (myImageView) view;
                myImageView.selectedView.setVisibility(View.VISIBLE);
                selected_imageview.add(myImageView);
                return true;
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // show image full screen
                myImageView myImageView = (myImageView) view;
                if(on_selection_mode==false) {
                    Dialog listDialog = new Dialog(LabelPage.this);
                    final View showview = getLayoutInflater().inflate(R.layout.full_image_show, null, false);
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(LabelPage.this);
                    imageLoader.init(config.build());
                    imageLoader.clearMemoryCache();
                    final ImageView imageView = showview.findViewById(R.id.imageview);
                    imageLoader.displayImage(com.cjcu.ima105.cvision.myImageView.get_url_from_image_id(myImageView.image_id), imageView, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            imageView.setVisibility(View.VISIBLE);
                            showview.findViewById(R.id.loading).setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
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
                else{
                    if(myImageView.selectedView.getVisibility()==View.GONE) {
                        myImageView.selectedView.setVisibility(View.VISIBLE);
                        selected_imageview.add(myImageView);
                    }
                    else{
                        myImageView.selectedView.setVisibility(View.GONE);
                        selected_imageview.remove(myImageView);
                        if(selected_imageview.size()==0){
                            actionMode.finish();
                        }
                    }
                }

            }
        });

        fab = findViewById(R.id.create_image_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LabelPage.this.startActivityForResult(new Intent(LabelPage.this, CreateImageCamera.class),0);
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> images_response = ServerOperate.label_images(CurrentPosition.key, CurrentPosition.label_id);
                Queue<String> images_response_q = new LinkedList<>();
                images_response_q.addAll(images_response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridview.setAdapter(labelImageAdapter);
                        progressBar.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                });
                while(images_response_q.isEmpty()==false){
                    String image_id = images_response_q.poll();
                    try {
                        labelImageAdapter.images_id.add(image_id);
                        labelImageAdapter.notifyDataSetChanged();
                    }catch (Exception e){}
                }
            }
        });
        t.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.label_page_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_label_delete:
                ServerOperate.delete_label(CurrentPosition.key, CurrentPosition.label_id);
                ArrayList<HashMap<String, String>> modellabellist_response = ServerOperate.label_list(CurrentPosition.key, CurrentPosition.model_id);
                RecyclerView recyclerView = ModelPageFragment.model_page_labels_view.findViewById(R.id.mModel_page_recycle_view);
                recyclerView.setLayoutManager(new GridLayoutManager(ModelPage.modelpage,2));
                ModelPageLabelRecycleViewAdapter adapter = new ModelPageLabelRecycleViewAdapter(modellabellist_response);
                recyclerView.setAdapter(adapter);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            ArrayList<String> images_response = ServerOperate.label_images(CurrentPosition.key, CurrentPosition.label_id);
            Queue<String> images_response_q = new LinkedList<>();
            images_response_q.addAll(images_response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridview.setAdapter(labelImageAdapter);
                    progressBar.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                }
            });
            labelImageAdapter.images_id = new ArrayList<>();
            while(images_response_q.isEmpty()==false){
                String image_id = images_response_q.poll();
                try {
                    labelImageAdapter.images_id.add(image_id);
                    labelImageAdapter.notifyDataSetChanged();
                }catch (Exception e){}
            }
        }
    }
}
class LabelImageAdapter extends BaseAdapter {
    public ArrayList<String> images_id;
    public HashMap<String, myImageView> imagesid_imageview_map;
    public Context context;
    private static ImageLoader imageLoader;
    LabelImageAdapter(Context context){
        this.images_id =new ArrayList<>();
        this.imagesid_imageview_map = new HashMap<>();
        this.context=context;
        this.imageLoader=ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        this.imageLoader.init(config.build());
        this.imageLoader.clearMemoryCache();
    }

    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {
        final myImageView imageView;
        if(imagesid_imageview_map.get(images_id.get(position))==null) {
            imageView = new myImageView(this.context, images_id.get(position));
            imageView.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imagesid_imageview_map.put(imageView.image_id, imageView);
            imageLoader.displayImage(myImageView.get_url_from_image_id(imageView.image_id), imageView.imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    imageView.imageView.setImageResource(R.mipmap.load_error);
                    imageView.loading.setVisibility(View.GONE);
                    imageView.imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.loading.setVisibility(View.GONE);
                    imageView.imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        else{
            imageView= imagesid_imageview_map.get(images_id.get(position));
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return images_id.size();
    }

    @Override
    public String getItem(int i) {
        return images_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
class myImageView extends RelativeLayout {
    public String image_id;
    public ImageView imageView;
    public ImageView selectedView;
    public SpinKitView loading;

    public myImageView(Context context, String image_id) {
        super(context);
        this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.image_id=image_id;
        loading = new SpinKitView(new ContextThemeWrapper(context, R.style.SpinKitView_Circle));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        loading.setLayoutParams(layoutParams);
        this.addView(loading);
        imageView=new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setVisibility(GONE);
        this.addView(imageView);
        selectedView=new ImageView(context);
        selectedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        selectedView.setImageResource(R.mipmap.selected);
        selectedView.setBackgroundColor(Color.parseColor("#BF2c2d30"));
        int padding_in_dp = 50;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        selectedView.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
        selectedView.setVisibility(GONE);
        this.addView(selectedView);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
    public static String get_url_from_image_id(String image_id){
        return ServerOperate.SERVER_PROTOCOL+"://"+ServerOperate.SERVER_ADDRESS+":"+ServerOperate.SERVER_PORT+"/get_image?key="+CurrentPosition.key+"&label_id="+CurrentPosition.label_id+"&image_id="+image_id;
    }
}