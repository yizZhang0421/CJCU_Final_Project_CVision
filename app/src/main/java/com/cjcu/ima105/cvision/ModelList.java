package com.cjcu.ima105.cvision;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelList extends Frame {
    public static ModelList modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelList = this;

        ViewGroup parent = (ViewGroup) findViewById(R.id.mFrameContent);
        parent.removeAllViews();
        parent.addView(getLayoutInflater().inflate(R.layout.model_list, parent, false), 0);
        ((Button) findViewById(R.id.model)).setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.model_click, 0, 0);

        RecyclerView recyclerView = findViewById(R.id.mModel_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<HashMap<String, String>> modellist_response = ServerOperate.model_list(CurrentPosition.key);
        ModelListRecycleViewAdapter adapter = new ModelListRecycleViewAdapter(modellist_response);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_v));
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ModelList.this);
                View mView = getLayoutInflater().inflate(R.layout.model_list_popup, null);
                final EditText create_model_name = mView.findViewById(R.id.mCreate_model_name);
                Button create_model_confirm = mView.findViewById(R.id.mCreate_model_confirm);
                Button create_mdoel_cancel = mView.findViewById(R.id.mCreate_model_cancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                create_mdoel_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                create_model_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageServerResponse createmodel_response = ServerOperate.create_model(CurrentPosition.key, create_model_name.getText().toString());
                        if (createmodel_response.status_code == 0) {
                            //renew page and dismiss
                            RecyclerView recyclerView = findViewById(R.id.mModel_list_recycle_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ModelList.this));
                            ArrayList<HashMap<String, String>> modellist_response = ServerOperate.model_list(CurrentPosition.key);
                            ModelListRecycleViewAdapter adapter = new ModelListRecycleViewAdapter(modellist_response);
                            recyclerView.setAdapter(adapter);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
                            dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_v));
                            recyclerView.addItemDecoration(dividerItemDecoration);
                            dialog.dismiss();
                        } else {
                            //show error feedback
                            Toast.makeText(ModelList.this, "model name invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            RecyclerView recyclerView = findViewById(R.id.mModel_list_recycle_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(ModelList.this));
            ArrayList<HashMap<String, String>> modellist_response = ServerOperate.model_list(CurrentPosition.key);
            ModelListRecycleViewAdapter adapter = new ModelListRecycleViewAdapter(modellist_response);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
            dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_v));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }
}

class ModelListRecycleViewAdapter extends RecyclerView.Adapter<ModelListRecycleViewAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> model_list;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout mSwipteLayout;

        public MyViewHolder(SwipeLayout v) {
            super(v);
            mSwipteLayout = v;
            mSwipteLayout.findViewById(R.id.model_del_lite_button_wraper).setMinimumWidth(500);
            mSwipteLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            mSwipteLayout.addDrag(SwipeLayout.DragEdge.Left, mSwipteLayout.findViewById(R.id.model_del_lite_button_wraper));
            mSwipteLayout.setLeftSwipeEnabled(false);
            mSwipteLayout.findViewById(R.id.model_item_wraper).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelPage.model_name = ((TextView) mSwipteLayout.findViewById(R.id.mModelName)).getText().toString();
                    ModelList.modelList.startActivityForResult(new Intent(ModelList.modelList, ModelPage.class), 0
                            , ActivityOptions.makeSceneTransitionAnimation(ModelList.modelList, mSwipteLayout, "sharedView").toBundle());
                    CurrentPosition.model_id = ((TextView) mSwipteLayout.findViewById(R.id.model_id)).getText().toString();
                }
            });
            mSwipteLayout.findViewById(R.id.model_delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String model_id = ((TextView) mSwipteLayout.findViewById(R.id.model_id)).getText().toString();
                    ServerOperate.delete_model(CurrentPosition.key, model_id);

                    RecyclerView recyclerView = ModelList.modelList.findViewById(R.id.mModel_list_recycle_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ModelList.modelList));
                    ArrayList<HashMap<String, String>> modellist_response = ServerOperate.model_list(CurrentPosition.key);
                    ModelListRecycleViewAdapter adapter = new ModelListRecycleViewAdapter(modellist_response);
                    recyclerView.setAdapter(adapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
                    dividerItemDecoration.setDrawable(ModelList.modelList.getDrawable(R.drawable.divider_v));
                    recyclerView.addItemDecoration(dividerItemDecoration);
                }
            });
//            mSwipteLayout.findViewById(R.id.model_download_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final String model_id = ((TextView) mSwipteLayout.findViewById(R.id.model_id)).getText().toString();
//                    final String model_name = ((TextView) mSwipteLayout.findViewById(R.id.mModelName)).getText().toString();
//
//                }
//            });

        }
    }

    public ModelListRecycleViewAdapter(ArrayList<HashMap<String, String>> modellist) {
        this.model_list = modellist;
    }

    @Override
    public ModelListRecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeLayout v = (SwipeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.model_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView modelid = holder.mSwipteLayout.findViewById(R.id.model_id);
        modelid.setText(model_list.get(position).get("id"));
        TextView modelname = holder.mSwipteLayout.findViewById(R.id.mModelName);
        modelname.setText(model_list.get(position).get("name"));
        TextView details = holder.mSwipteLayout.findViewById(R.id.mAccuracy);
//        details.setText("acc: " + model_list.get(position).get("acc") + "\nloss: " + model_list.get(position).get("loss"));
        details.setText("準確率: " + model_list.get(position).get("acc"));
    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }
}