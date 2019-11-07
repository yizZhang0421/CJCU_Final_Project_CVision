package com.cjcu.ima105.cvision;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelPageFragment extends Fragment {
    public ModelPageFragment(){}
    public static View model_page_labels_view;
    private ArrayList<HashMap<String, String>> modellabellist_response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout_resource = ((Long) getArguments().get("layout")).intValue();
        switch (layout_resource) {
            case R.layout.model_page_labels:
                modellabellist_response = ServerOperate.label_list(CurrentPosition.key, CurrentPosition.model_id);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout_resource = ((Long) getArguments().get("layout")).intValue();
        final View view = inflater.inflate(layout_resource, container, false);
        HashMap<String, String> model_info = ServerOperate.get_model_info(CurrentPosition.model_id);
        switch (layout_resource){
            case R.layout.model_page_labels:
                model_page_labels_view=view;
                RecyclerView recyclerView = view.findViewById(R.id.mModel_page_recycle_view);
                recyclerView.setLayoutManager(new GridLayoutManager(ModelPage.modelpage, 2));
                ModelPageLabelRecycleViewAdapter adapter = new ModelPageLabelRecycleViewAdapter(modellabellist_response);
                recyclerView.setAdapter(adapter);
                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.create_label_fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ModelPage.modelpage);
                        View mView = getLayoutInflater().inflate(R.layout.model_list_popup, null);
                        TextView title = mView.findViewById(R.id.textView);
                        title.setText("建立類別");
                        final EditText create_label_name=mView.findViewById(R.id.mCreate_model_name);
                        create_label_name.setHint("類別名稱");
                        Button create_model_confirm = mView.findViewById(R.id.mCreate_model_confirm);
                        Button create_mdoel_cancel= mView.findViewById(R.id.mCreate_model_cancel);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();
                        create_mdoel_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        create_model_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageServerResponse createlabel_response = ServerOperate.create_label(CurrentPosition.key, CurrentPosition.model_id ,create_label_name.getText().toString());
                                if(createlabel_response.status_code==0){
                                    //renew page and dismiss
                                    ArrayList<HashMap<String, String>> modellabellist_response = ServerOperate.label_list(CurrentPosition.key, CurrentPosition.model_id);
                                    RecyclerView recyclerView = view.findViewById(R.id.mModel_page_recycle_view);
                                    recyclerView.setLayoutManager(new GridLayoutManager(ModelPage.modelpage,2));
                                    ModelPageLabelRecycleViewAdapter adapter = new ModelPageLabelRecycleViewAdapter(modellabellist_response);
                                    recyclerView.setAdapter(adapter);
                                    dialog.dismiss();
                                }
                                else{
                                    //show error feedback
                                    Toast.makeText(ModelPage.modelpage, "Label name invalid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
            case R.layout.model_page_evaluate:
                if(model_info.get("acc").equals("null")){
                    view.findViewById(R.id.model_evaluate_wraper).setVisibility(View.INVISIBLE);
                }
                break;
            case R.layout.model_page_train:
                Button button = view.findViewById(R.id.start_train_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageServerResponse messageServerResponse = ServerOperate.train(CurrentPosition.key, CurrentPosition.model_id);
                        Toast.makeText(ModelPage.modelpage, messageServerResponse.message, Toast.LENGTH_SHORT).show();
                        ModelPage.modelpage.finish();
                    }
                });
                break;
            case R.layout.model_page_config:
                Switch share = view.findViewById(R.id.share_switch);
                String initial_share = model_info.get("share");
                if(initial_share.equals("0")){
                    share.setChecked(false);
                }
                else{
                    share.setChecked(true);
                }
                share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ServerOperate.share_model(CurrentPosition.key, CurrentPosition.model_id, isChecked);
                    }
                });


                break;
        }
        return view;
    }
}
class ModelPageLabelRecycleViewAdapter extends RecyclerView.Adapter<ModelPageLabelRecycleViewAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> mLabels;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mRelativeLayout;
        public MyViewHolder(RelativeLayout v) {
            super(v);
            mRelativeLayout = v;
            final TextView label_name = mRelativeLayout.findViewById(R.id.mModel_page_label_name);
            mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(ModelPage.modelpage, label_name.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelList.modelList.startActivity(new Intent(ModelPage.modelpage, LabelPage.class));
                    CurrentPosition.label_id=((TextView)mRelativeLayout.findViewById(R.id.label_id)).getText().toString();
                    CurrentPosition.label_name=((TextView)mRelativeLayout.findViewById(R.id.mModel_page_label_name)).getText().toString();
                }
            });
        }
    }
    public ModelPageLabelRecycleViewAdapter(ArrayList<HashMap<String, String>> mLabels) {
        this.mLabels=mLabels;
    }
    @Override
    public ModelPageLabelRecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.model_page_label_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView labelid = holder.mRelativeLayout.findViewById(R.id.label_id);
        labelid.setText(mLabels.get(position).get("id"));
        TextView labelname = holder.mRelativeLayout.findViewById(R.id.mModel_page_label_name);
        labelname.setText(mLabels.get(position).get("name"));
    }

    @Override
    public int getItemCount() {
        return mLabels.size();
    }
}