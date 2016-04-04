package com.inspius.canyon.yo_video.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.inspius.canyon.yo_video.activity.VideoActivity;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.helper.NotificationUtils;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.parse.ParseBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Billy on 17/2015.
 */

public class CustomPushReceiver extends ParseBroadcastReceiver {
    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

//    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent == null || intent.getExtras() == null)
            return;

        if (!intent.getExtras().containsKey("com.parse.Data"))
            return;

        String json = intent.getExtras().getString("com.parse.Data");

        Log.e(TAG, "Push received: " + json);

        parsePushJson(context, json);
    }

//    @Override
//    protected void onPushDismiss(Context context, Intent intent) {
//        super.onPushDismiss(context, intent);
//    }

//    @Override
//    protected void onPushOpen(Context context, Intent intent) {
//        super.onPushOpen(context, intent);
//    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, String json) {
        if (TextUtils.isEmpty(json))
            return;

        /**
         * Change your data to NotificationJSON
         */
        // NotificationJSON notificationJSON = new Gson().fromJson(json, NotificationJSON.class);

        NotificationJSON notificationJSON = new NotificationJSON();
        notificationJSON.title = String.format("Notification %s", (new Random().nextInt(100)));
        notificationJSON.message = "Test notification from Parse.com";
        notificationJSON.status = 0;

        Intent resultIntent = new Intent(context, VideoActivity.class);
        showNotificationMessage(context, notificationJSON, resultIntent);
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param notificationJSON
     * @param intent
     */
    private void showNotificationMessage(Context context, NotificationJSON notificationJSON, Intent intent) {

        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(notificationJSON, intent);
    }
}