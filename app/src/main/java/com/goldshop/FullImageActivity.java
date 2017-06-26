package com.goldshop;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.goldshop.adapter.FullImageViewPagerAdapter;
import com.goldshop.adapter.ViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    PagerAdapter adapter;
    CirclePageIndicator mIndicator;
    ArrayList<String> imageUrlList;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Full Image");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            position=bundle.getInt("position");
            imageUrlList=bundle.getStringArrayList("list");
        }

        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.full_image_viewPager);
        // Pass results to ViewPagerAdapter Class
        adapter = new FullImageViewPagerAdapter(this, imageUrlList);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);

        // ViewPager Indicator
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        mIndicator.setViewPager(viewPager);
        viewPager.setCurrentItem(position);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
