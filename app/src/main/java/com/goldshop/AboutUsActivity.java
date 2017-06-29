package com.goldshop;

import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.goldshop.adapter.AboutUsPagerAdapter;
import com.goldshop.adapter.EducationPagerAdapter;
import com.goldshop.fragment.ManagementFragment;
import com.goldshop.fragment.NewsFragment;
import com.goldshop.fragment.ProfileFragment;
import com.goldshop.utility.FragmentLifecycle;
import com.goldshop.utility.Preference;
import com.goldshop.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends BaseActivity {

    WebView webView;
    Preference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 //       setContentView(R.layout.activity_about_us);
        getLayoutInflater().inflate(R.layout.activity_about_us,frameLayout);
        preference=new Preference(this);

        setupToolbar();
        webView= (WebView) findViewById(R.id.about_us_webview);

        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://shridurgajewellers.com/about-us-mobile/");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(preference.getCART_COUNT()!=0) {
            cart_countText.setVisibility(View.VISIBLE);
            cart_countText.setText(""+preference.getCART_COUNT());
        }
    }

    private void setupToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTitle.setText("About Us");
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            Utils.showLoader(AboutUsActivity.this);
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

