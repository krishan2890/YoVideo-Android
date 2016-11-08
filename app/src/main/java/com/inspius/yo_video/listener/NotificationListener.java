package com.inspius.yo_video.listener;

import com.inspius.yo_video.greendao.DBNotification;

/**
 * Created by Billy on 10/7/15.
 */
public interface NotificationListener {
    void onNotificationInserted(DBNotification notification);

    void onNotificationSizeChanged(int totalNotView);
}
