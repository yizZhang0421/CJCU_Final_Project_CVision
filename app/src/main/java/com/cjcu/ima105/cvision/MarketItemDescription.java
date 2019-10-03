package com.cjcu.ima105.cvision;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;


public class MarketItemDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_item_description);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        PorterShapeImageView image = findViewById(R.id.image);
        image.setImageDrawable(getDrawable(getIntent().getIntExtra("image", getIntent().getIntExtra("image", R.drawable.market_square_img))));

        TextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));

        TextView view1 = (TextView) findViewById(R.id.view);
        view1.setText(getIntent().getStringExtra("view1"));

        TextView install = (TextView) findViewById(R.id.intall);
        install.setText(getIntent().getStringExtra("install"));

        TextView description = findViewById(R.id.description);
        description.setText(getIntent().getStringExtra("description"));
        description.setMovementMethod(new ScrollingMovementMethod());
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
