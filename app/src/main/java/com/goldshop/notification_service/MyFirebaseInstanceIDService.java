package com.goldshop.notification_service;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goldshop.CartActivity;
import com.goldshop.ThanksActivity;
import com.goldshop.model.CartModel;
import com.goldshop.service.Response;
import com.goldshop.service.ResponseListener;
import com.goldshop.service.ServerRequest;
import com.goldshop.utility.Application;
import com.goldshop.utility.Connection;
import com.goldshop.utility.Preference;
import com.goldshop.utility.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 7/6/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    Preference preference;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        preference=new Preference(Application.mContext);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server flag
        preference.setFCM_ID_ServerSendTime(true);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Connection.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        Preference preference=new Preference(getApplicationContext());
        preference.setAppFCM_ID(token);
    }


}
