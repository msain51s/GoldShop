package com.goldshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.goldshop.utility.OnSwipeTouchListener;
import com.goldshop.utility.Preference;

public class HomeActivity extends BaseActivity
         {
    View homeView,infoView;
             ImageView infoIcon;
             Animation bottomUp,bottomDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_home);
        getLayoutInflater().inflate(R.layout.content_home,frameLayout);
        toolbar.setVisibility(View.GONE);
        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(buttonLayoutParams);

       bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);
       bottomDown= AnimationUtils.loadAnimation(this,
               R.anim.bottom_down);

        infoIcon= (ImageView) findViewById(R.id.info_icon);
        infoView=findViewById(R.id.info_view);
        homeView=findViewById(R.id.home_view);
   /*Manoj Says...
   *    swipe handler implementation
   * */
        homeView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
               Intent intent=new Intent(HomeActivity.this,GalleryActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSwipeRight() {
                Intent intent=new Intent(HomeActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSwipeBottomToTop() {
                super.onSwipeBottomToTop();
                Intent intent=new Intent(HomeActivity.this,ReachUsActivity.class);
                startActivity(intent);
            }
        });
/*Manoj Says...
   *    suggestion displaying
   * */
        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              infoView.startAnimation(bottomUp);
              infoView.setVisibility(View.VISIBLE);
            }
        });
/*Manoj Says...
   *    suggestion disappearing
   * */
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoView.startAnimation(bottomDown);
                infoView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
