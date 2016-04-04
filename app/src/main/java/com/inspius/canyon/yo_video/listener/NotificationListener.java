package com.inspius.canyon.yo_video.listener;

import com.inspius.canyon.yo_video.model.CustomerModel;
import com.inspius.canyon.yo_video.model.NotificationJSON;

import java.util.List;

/**
 * Created by Billy on 10/7/15.
 */
public interface NotificationListener {

    public void onNotificationChanged(List<NotificationJSON> newData, List<NotificationJSON> listNotification);

    public void onNotificationNotReadChanged(int number);
}
