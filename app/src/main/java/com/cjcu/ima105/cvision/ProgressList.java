package com.cjcu.ima105.cvision;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProgressList extends AppCompatActivity {
    public static AppCompatActivity ProgressListAvtivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_list);
        ProgressList.ProgressListAvtivity=this;

        RecyclerView recyclerView = findViewById(R.id.recycleview_progress_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<HashMap<String, String>> modellist_response = ServerOperate.progress_list(CurrentPosition.key);
        ProgressListRecycleViewAdapter adapter = new ProgressListRecycleViewAdapter(modellist_response);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setRemoveDuration(500);
        recyclerView.setItemAnimator(itemAnimator);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_v));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(String key : ProgressListRecycleViewAdapter.threads.keySet()){
            ProgressListRecycleViewAdapter.threads.get(key).interrupt();
        }
        ProgressListRecycleViewAdapter.threads= new HashMap<>();
    }
}
class ProgressListRecycleViewAdapter extends RecyclerView.Adapter<ProgressListRecycleViewAdapter.MyViewHolder> {
    public static ArrayList<HashMap<String, String>> progressList;
    public static HashMap<String, Thread> threads = new HashMap<>();
    public static ProgressListRecycleViewAdapter progressListRecycleViewAdapterContext;
    public ProgressListRecycleViewAdapter (ArrayList<HashMap<String, String>> modellist) {
        this.progressList = modellist;
        progressListRecycleViewAdapterContext=this;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public Thread progress_stream;
        public MyViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
            mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ProgressTerminateBottomSheet progressTerminateBottomSheet = new ProgressTerminateBottomSheet();
                    Bundle args = new Bundle();
                    args.putString("model", ((TextView)view.findViewById(R.id.textview_model_id)).getText().toString());
                    args.putInt("position", getAdapterPosition());
                    progressTerminateBottomSheet.setArguments(args);
                    progressTerminateBottomSheet.show(ProgressList.ProgressListAvtivity.getSupportFragmentManager(), "bottomSheet");
                    return true;
                }
            });
        }
    }

    @Override
    public ProgressListRecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final TextView modelid = holder.mLinearLayout.findViewById(R.id.textview_model_id);
        final String id = progressList.get(position).get("id");
        modelid.setText(id);
        TextView modelname = holder.mLinearLayout.findViewById(R.id.textview_model_name);
        modelname.setText(progressList.get(position).get("name"));
        final TextView progress_detail = holder.mLinearLayout.findViewById(R.id.textview_progress_detail);

        holder.progress_stream = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL(ServerOperate.SERVER_PROTOCOL + "://" + ServerOperate.SERVER_ADDRESS + ":" + ServerOperate.SERVER_PORT + "/progress").openConnection()));
                    httpcon.setDoOutput(true);
                    httpcon.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(httpcon.getOutputStream());
                    out.write(("key=" + CurrentPosition.key + "&model_id=" + id).getBytes("UTF-8"));
                    out.flush();
                    out.close();
                    InputStreamReader in = new InputStreamReader(httpcon.getInputStream(), "UTF-8");
                    int a = 0;
                    String return_string = "";
                    while (!(((char) a) + "").equals("\n")) {
                        a = in.read();
                    }
                    while (true) {
                        a = in.read();
                        if ((((char) a) + "").equals("\n")) {
                            if (return_string.equals("finish")) {
                                return_string = "";
                                ProgressList.ProgressListAvtivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_detail.setText("Finish");
                                    }
                                });
                                break;
                            }

                            JSONObject jsonObject = new JSONObject(return_string);
                            final HashMap<String, String> map = new HashMap<>();
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                map.put(key, jsonObject.getString(key));
                            }
                            ProgressList.ProgressListAvtivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    progress_detail.setText("epoch: " + map.get("epoch") + "\n" + "acc: " + map.get("acc") + "\n" + "loss: " + map.get("loss"));
                                    progress_detail.setText("學習次數: " + map.get("epoch") + "\n" + "準確率: " + map.get("acc"));
                                }
                            });
                            return_string = "";
                            continue;
                        }
                        return_string += ((char) a);
                    }
                    in.close();
                    httpcon.disconnect();
                    progressList.remove(position);
                    threads.remove(id);
                    ProgressListRecycleViewAdapter.this.notifyItemRemoved(position);
                    ProgressListRecycleViewAdapter.this.notifyItemRangeChanged(position, ProgressListRecycleViewAdapter.this.progressList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.progress_stream.start();
        threads.put(id, holder.progress_stream);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

}
