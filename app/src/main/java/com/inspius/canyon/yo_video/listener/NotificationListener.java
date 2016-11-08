package com.inspius.canyon.yo_video.listener;

import com.inspius.canyon.yo_video.greendao.DBNotification;

/**
 * Created by Billy on 10/7/15.
 */
public interface NotificationListener {
    void onNotificationInserted(DBNotification notification);

    void onNotificationSizeChanged(int totalNotView);
}
