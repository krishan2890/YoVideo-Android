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
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.VideoActivity;
import com.inspius.yo_video.app.AppConstant;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        Map<String, String> data = null;
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);
        }

        // Check if message contains a notification payload.
        String messageBody = "";
        if (remoteMessage.getNotification() != null) {
            messageBody = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            Log.d(TAG, "Message Notification Body: " + messageBody);

            if (TextUtils.isEmpty(title))
                title = "YoVideo notification!";

            if (AccountDataManager.getInstance().isLogin())
                updateNotificationDB(data);
            else
                sendNotification(title, messageBody, data);
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title, String messageBody, Map<String, String> data) {
        Intent intent = new Intent(this, VideoActivity.class);

        if (data != null) {
            for (String key : data.keySet()) {
                intent.putExtra(key, data.get(key));
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    void updateNotificationDB(Map<String, String> data) {
        if (data != null) {
            for (String key : data.keySet()) {
                if (key.equals(AppConstant.KEY_NOTIFICATION_TITLE)) {
                    String title = data.get(AppConstant.KEY_NOTIFICATION_TITLE);
                    String message = data.get(AppConstant.KEY_NOTIFICATION_MESSAGE);
                    String content = data.get(AppConstant.KEY_NOTIFICATION_CONTENT);

                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content))
                        return;


                    if (TextUtils.isEmpty(message))
                        message = content;

                    AppNotificationManager.getInstance().insertNotification(title, message, content);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "You received a new notification", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
            }
        }
    }
}
