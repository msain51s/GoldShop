package com.goldshop;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.astuetz.PagerSlidingTabStrip;
import com.goldshop.adapter.EducationPagerAdapter;
import com.goldshop.fragment.CaratFragment;
import com.goldshop.fragment.ClarityFragment;
import com.goldshop.fragment.ColourFragment;
import com.goldshop.fragment.CutFragment;
import com.goldshop.fragment.ManagementFragment;
import com.goldshop.fragment.NewsFragment;
import com.goldshop.fragment.ProfileFragment;
import com.goldshop.utility.FragmentLifecycle;
import com.goldshop.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends BaseActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_education);
        getLayoutInflater().inflate(R.layout.activity_education,frameLayout);
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Event");

        webView= (WebView) findViewById(R.id.event_webview);

        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://shridurgajewellers.com/events/");

    }
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            Utils.showLoader(EventActivity.this);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            Utils.dismissLoader();
        }
    }
}
