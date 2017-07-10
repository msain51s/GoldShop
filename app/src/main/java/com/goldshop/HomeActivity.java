package com.goldshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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

import com.bumptech.glide.util.Util;
import com.goldshop.notification_service.MyFirebaseInstanceIDService;
import com.goldshop.service.Response;
import com.goldshop.service.ResponseListener;
import com.goldshop.service.ServerRequest;
import com.goldshop.utility.Application;
import com.goldshop.utility.Connection;
import com.goldshop.utility.NotificationUtils;
import com.goldshop.utility.OnSwipeTouchListener;
import com.goldshop.utility.Preference;
import com.goldshop.utility.Utils;
import com.google.android.gms.common.stats.ConnectionEvent;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity implements ResponseListener
         {
    View homeView,infoView;
             ImageView infoIcon;
             Animation bottomUp,bottomDown;
             private BroadcastReceiver mRegistrationBroadcastReceiver;
     Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_home);
        getLayoutInflater().inflate(R.layout.content_home,frameLayout);
        h=new Handler();
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

        /*Manoj Says...
  *    suggestion display first time
  * */
        if(!preference.getIS_SUGGESTION_DISPLAY_FIRST_TIME()){
            infoView.setVisibility(View.VISIBLE);
            preference.setIS_SUGGESTION_DISPLAY_FIRST_TIME(true);
        }else
            infoView.setVisibility(View.GONE);



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Connection.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Connection.TOPIC_GLOBAL);

                    Toast.makeText(getApplicationContext(), "Push notification: Enabled on your device" , Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Connection.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

  /*FCM ID SEND TO SERVER*/
        if(preference.isFCM_ID_ServerSendTime())
            sendFCM_ID_ToServer(preference.getAppFCM_ID());

    }


             @Override
             protected void onResume() {
                 super.onResume();

                 // register GCM registration complete receiver
                 LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                         new IntentFilter(Connection.REGISTRATION_COMPLETE));

                 // register new push message receiver
                 // by doing this, the activity will be notified each time a new message arrives
                 LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                         new IntentFilter(Connection.PUSH_NOTIFICATION));

                 // clear the notification area when the app is opened
                 NotificationUtils.clearNotifications(getApplicationContext());
             }

             @Override
             protected void onPause() {
                 super.onPause();
                 LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

             private void sendFCM_ID_ToServer(String token) {

                 if (Utils.ChechInternetAvalebleOrNot(HomeActivity.this)) {

                     Utils.showLoader(HomeActivity.this);
                     ServerRequest
                             .postRequest(
                                     Connection.BASE_URL + "update_DeviceToken",
                                     getSendTokenToServerData(preference.getUSER_ID(), token),
                                     HomeActivity.this,
                                     ResponseListener.REQUEST_SEND_FCMID_TO_SERVER);

                 } else {
                     //   Utils.shonterwSnakeBar(layout_view, "internet not connected !!!", Color.RED);Toast.makeText(LoginActivity.this,"Internet not connected !!!",Toast.LENGTH_LONG).show();
                     Toast.makeText(HomeActivity.this, "Internet not connected !!!", Toast.LENGTH_LONG).show();
                     return;
                 }
             }

             private JSONObject getSendTokenToServerData(String userId, String token) {
                 JSONObject json = new JSONObject();
                 try {
                     json.put("userId", userId);
                     json.put("token", token);
                     json.put("deviceType", "1");

                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 return json;

             }

             @Override
             public void onResponse(final Response response, final int rid) {


                 h.post(new Runnable() {

                     @Override
                     public void run() {
                         Utils.dismissLoader();
                         if (rid == ResponseListener.REQUEST_SEND_FCMID_TO_SERVER) {

                             if (response.isError()) {
                                 Toast.makeText(Application.mContext, response.getErrorMsg(),
                                         Toast.LENGTH_SHORT).show();
                                 return;
                             }
                             if (response.getData() != null) {
                                 try {
                                     JSONObject jsonObject1 = new JSONObject(response.getData());
                                     JSONObject jsonObject = null;
                                     JSONArray jsonArray = null;
                                     String status = jsonObject1.getString("status");
                                     if (status.equalsIgnoreCase("true")) {
                                         preference.setFCM_ID_ServerSendTime(false);
                                /*Utils.showCommonInfoPrompt(CartActivity.this,"Success",jsonObject1.getString("msg"));
                                mAdapter.removeItem(selectedItemPosition);
                                itemCountText.setText(list.size()+" item in your cart");
                                preference.setCART_COUNT(list.size());
                                if(preference.getCART_COUNT()!=0) {
                                    cart_countText.setVisibility(View.VISIBLE);
                                    cart_countText.setText(""+preference.getCART_COUNT());
                                }else
                                    cart_countText.setVisibility(View.GONE);*/
                                     } else{
                                         //   Utils.showCommonInfoPrompt(Application.mContext,"Failed",jsonObject1.getString("msg"));
                                     }

                                     Log.d("json_response", response.getData());
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             }

                         }
                     }
                 });
             }
}
