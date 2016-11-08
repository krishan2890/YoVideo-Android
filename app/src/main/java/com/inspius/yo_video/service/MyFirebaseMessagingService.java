package com.inspius.yo_video.service;

/**
 * Created by Billy on 7/18/16.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.VideoActivity;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.greendao.DBNotification;
import com.inspius.yo_video.helper.NotificationUtils;
import com.inspius.yo_video.model.NotificationJSON;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageBody = remoteMessage.getNotification().getBody();
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + messageBody);


        String title = "YoVideo Notification";
        String message = messageBody;
        String content = messageBody;

        NotificationJSON notification = new NotificationJSON();
        notification.title = title;
        notification.message = message;
        notification.content = content;

        //Calling method to generate notification
        sendNotification(notification);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(NotificationJSON notification) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT, notification);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.title)
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
