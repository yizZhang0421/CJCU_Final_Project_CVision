package com.cjcu.ima105.cvision;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cjcu.ima105.cvision.market.Adapter.RecycleAdapter_MarketDescription;
import com.cjcu.ima105.cvision.market.customfonts.Button_Roboto_Medium;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;
import java.util.HashMap;


public class MarketItemDescription extends AppCompatActivity {
    private static int checkedIndex;
    private RecyclerView recyclerView;
    private RecycleAdapter_MarketDescription mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_item_description);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        PorterShapeImageView image = findViewById(R.id.image);

        byte[] decodedString = Base64.decode(getIntent().getStringExtra("image"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(bitmap);

        TextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        TextView view1 = (TextView) findViewById(R.id.view);
        view1.setText(getIntent().getStringExtra("view1"));

        TextView install = (TextView) findViewById(R.id.intall);
        install.setText(getIntent().getStringExtra("install"));

        TextView description = findViewById(R.id.description);
        description.setText(getIntent().getStringExtra("description"));
        description.setMovementMethod(new ScrollingMovementMethod());

        final TextView id = findViewById(R.id.model_id);
        id.setText(getIntent().getStringExtra("id"));

        Button_Roboto_Medium button = findViewById(R.id.import_model);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("market").equals("model")){
                    MessageServerResponse result = ServerOperate.model_import(CurrentPosition.key, id.getText().toString());
                    Toast.makeText(MarketItemDescription.this, result.message, Toast.LENGTH_SHORT).show();
                }
                else if(getIntent().getStringExtra("market").equals("label")){
                    ArrayList<HashMap<String, String>> modelList = ServerOperate.model_list(CurrentPosition.key);
                    HashMap<HashMap<String, String>, ArrayList<HashMap<String, String>>> modelLabel = new HashMap<>();
                    int length = 0;
                    for(HashMap<String, String> i : modelList){
                        ArrayList<HashMap<String, String>> labelList = ServerOperate.label_list(CurrentPosition.key, i.get("id"));
                        if(labelList.size()!=0){
                            modelLabel.put(i, labelList);
                            length+=labelList.size();
                        }
                    }
                    final String[] modelLabelName = new String[length];
                    final String[] label_id = new String[length];
                    int put_index=0;
                    for(HashMap<String, String> model : modelLabel.keySet()){
                        ArrayList<HashMap<String, String>> labelList = modelLabel.get(model);
                        for(HashMap<String, String> label : labelList){
                            modelLabelName[put_index]=model.get("name")+"/"+label.get("name");
                            label_id[put_index]=label.get("id");
                            put_index++;
                        }
                    }

                    if(modelLabelName.length==0){
                        Toast.makeText(MarketItemDescription.this, "不存在可匯入的類別", Toast.LENGTH_SHORT).show();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MarketItemDescription.this);
                    builder.setTitle("選擇匯入類別");
                    checkedIndex = 0; //this will checked the item when user open the dialog
                    builder.setSingleChoiceItems(modelLabelName, checkedIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedIndex=which;
                        }
                    });
                    builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MessageServerResponse result = ServerOperate.label_import(CurrentPosition.key, id.getText().toString(), label_id[checkedIndex]);
                            Toast.makeText(MarketItemDescription.this, result.message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ArrayList<Bitmap> samples = new ArrayList<>();
        ArrayList<String> sample_names = new ArrayList<>();
        ArrayList<HashMap<String, String>> base64_samples = null;
        if(getIntent().getStringExtra("market").equals("model")){
            base64_samples = ServerOperate.model_samples(id.getText().toString(), null);
        }
        else if(getIntent().getStringExtra("market").equals("label")){
            base64_samples = ServerOperate.label_samples(id.getText().toString(), null);
        }
        recyclerView = (RecyclerView) findViewById(R.id.sample_recyclerview);
        for(HashMap<String, String> i : base64_samples) {
            sample_names.add(i.get("name"));
            byte[] b = Base64.decode(i.get("base64"), Base64.DEFAULT);
            samples.add(BitmapFactory.decodeByteArray(b, 0, b.length));
        }
        mAdapter = new RecycleAdapter_MarketDescription(this, samples, sample_names);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.market_samples_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
