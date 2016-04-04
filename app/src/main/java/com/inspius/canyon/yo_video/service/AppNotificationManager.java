package com.inspius.canyon.yo_video.service;

import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.listener.NotificationListener;
import com.inspius.canyon.yo_video.model.NotificationJSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 1/14/16.
 */
public class AppNotificationManager {
    private static AppNotificationManager mInstance;
    private List<NotificationListener> listeners;
    private List<NotificationJSON> data;
    private int sizeNotView;

    public static synchronized AppNotificationManager getInstance() {
        if (mInstance == null)
            mInstance = new AppNotificationManager();

        return mInstance;
    }

    public AppNotificationManager() {
        this.listeners = new ArrayList<>();
        data = new ArrayList<>();
    }

    /**
     * @param listener
     */
    public void subscribeNotification(NotificationListener listener) {
        listeners.add(listener);
    }

    /**
     * @param listener
     */
    public void unSubscribeNotification(NotificationListener listener) {
        listeners.remove(listener);
    }

    /**
     * clear listener
     */
    public void clearSubscribeNotification() {
        listeners.clear();
    }

    /**
     * @param position
     */
    public void changeStateNotification(int position) {
        if (data.get(position).status == 1)
            return;

        data.get(position).status = 1;
        sizeNotView--;
        if (sizeNotView < 0)
            sizeNotView = 0;


        for (NotificationListener listener : listeners)
            listener.onNotificationNotReadChanged(sizeNotView);
    }

    /**
     * @param notificationJSON
     */
    public void addNotification(NotificationJSON notificationJSON) {
        data.add(0, notificationJSON);
        if (notificationJSON.status == 0)
            sizeNotView++;

        List<NotificationJSON> newData = new ArrayList<>();
        newData.add(notificationJSON);
        for (NotificationListener listener : listeners) {
            listener.onNotificationChanged(newData, data);
            listener.onNotificationNotReadChanged(sizeNotView);
        }
    }

    /**
     * @param notifications
     */
    public void addNotification(List<NotificationJSON> notifications) {
        for (NotificationJSON model : notifications) {
            if (model.status == 0)
                sizeNotView++;
            data.add(model);
        }
        for (NotificationListener listener : listeners) {
            listener.onNotificationChanged(notifications, data);
            listener.onNotificationNotReadChanged(sizeNotView);
        }
    }

    /**
     * request get list notification
     */
    public void requestGetNotification() {
        RPC.requestGetListNotifications(new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                List<NotificationJSON> notifications = (List<NotificationJSON>) results;
                if (notifications == null)
                    return;

                clearNotification();
                addNotification(notifications);
            }
        });
    }

    /**
     * Clear data
     */
    private void clearNotification() {
        sizeNotView = 0;
        data.clear();
    }

    /**
     * Get list notification
     *
     * @return
     */
    public List<NotificationJSON> getData() {
        return data;
    }
}
