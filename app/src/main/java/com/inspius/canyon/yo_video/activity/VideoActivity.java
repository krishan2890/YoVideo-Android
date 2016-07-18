package com.inspius.canyon.yo_video.activity;

import android.content.Intent;
import android.os.Bundle;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.fragment.SplashFragment;
import com.inspius.canyon.yo_video.fragment.notifications.NotificationDetailAppFragment;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.service.AppNotificationManager;
import com.inspius.coreapp.CoreAppActivity;

public class VideoActivity extends CoreAppActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        AppUtils.printHashKey(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT)) {
            NotificationJSON notification = (NotificationJSON) bundle.getSerializable(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT);
            DBNotification dbNotification = AppNotificationManager.getInstance().insertNotification(notification.title, notification.message, notification.content);
            addFragment(NotificationDetailAppFragment.newInstance(dbNotification), true);
        } else
            addFragment(SplashFragment.newInstance(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return;

        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT)) {
            NotificationJSON notification = (NotificationJSON) bundle.getSerializable(AppConstant.KEY_BUNDLE_NOTIFICATION_CONTENT);
            DBNotification dbNotification = AppNotificationManager.getInstance().insertNotification(notification.title, notification.message, notification.content);
            addFragment(NotificationDetailAppFragment.newInstance(dbNotification), true);
        }
    }
}
