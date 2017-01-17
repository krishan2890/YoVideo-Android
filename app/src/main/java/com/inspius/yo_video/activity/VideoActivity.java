package com.inspius.yo_video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.inspius.coreapp.CoreAppActivity;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.fragment.SplashFragment;
import com.inspius.yo_video.fragment.notifications.NotificationDetailAppFragment;
import com.inspius.yo_video.greendao.DBNotification;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.service.AppNotificationManager;

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

        if (!isOpenNotification(getIntent()))
            addFragment(SplashFragment.newInstance(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return;

        if (!isOpenNotification(intent))
            addFragment(SplashFragment.newInstance(), true);
    }

    boolean isOpenNotification(Intent intent) {
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                if (key.equals(AppConstant.KEY_NOTIFICATION_TITLE)) {
                    String title = intent.getExtras().getString(AppConstant.KEY_NOTIFICATION_TITLE);
                    String message = intent.getExtras().getString(AppConstant.KEY_NOTIFICATION_MESSAGE);
                    String content = intent.getExtras().getString(AppConstant.KEY_NOTIFICATION_CONTENT);

                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content))
                        return false;

                    if (TextUtils.isEmpty(message))
                        message = content;

                    DBNotification dbNotification = AppNotificationManager.getInstance().insertNotification(title, message, content);

                    addFragment(NotificationDetailAppFragment.newInstance(dbNotification), true);

                    return true;
                }
            }
        }

        return false;
    }
}
