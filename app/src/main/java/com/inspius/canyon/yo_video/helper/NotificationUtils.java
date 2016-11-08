package com.inspius.canyon.yo_video.helper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.service.AppNotificationManager;

import java.util.List;

/**
 * Created by Billy on 11/17/2015.
 */
public class NotificationUtils {
    private String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils() {
    }

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(NotificationJSON notificationJSON, Intent intent) {

        // Check for empty push message
        if (notificationJSON == null)
            return;

        intent.putExtra(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT, notificationJSON);

        if (isAppIsInBackground(mContext)) {
            // notification icon
            int icon = R.mipmap.ic_launcher;

            int mNotificationId = AppConfig.NOTIFICATION_ID;

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    mContext);
            Notification notification = mBuilder.setSmallIcon(icon).setTicker(notificationJSON.title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(notificationJSON.title)
                    .setStyle(inboxStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(notificationJSON.message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);
        } else {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            mContext.startActivity(intent);

//            AppNotificationManager.getInstance().addNotification(notificationJSON);
        }
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }

        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
