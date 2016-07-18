package com.inspius.canyon.yo_video.service;

import android.util.Log;

import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.listener.NotificationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 1/14/16.
 */
public class AppNotificationManager {
    private static AppNotificationManager mInstance;
    private List<NotificationListener> listeners;
    private int sizeNotView;

    public static synchronized AppNotificationManager getInstance() {
        if (mInstance == null)
            mInstance = new AppNotificationManager();

        return mInstance;
    }

    public AppNotificationManager() {
        this.listeners = new ArrayList<>();

        loadTotalNotificationNotview();
    }

    public int loadTotalNotificationNotview(){
        sizeNotView = (int) DatabaseManager.getInstance().getTotalNotificationNotView();
        return sizeNotView;
    }

    public DBNotification insertNotification(String title, String message, String content) {
        DBNotification notification = new DBNotification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setContent(content);
        notification.setStatus(0);

        notification = DatabaseManager.getInstance().insertNotification(notification);

        Log.d("notification", notification.getId() + " : " + notification.getTitle());

        sizeNotView++;
        for (NotificationListener listener : listeners) {
            listener.onNotificationInserted(notification);
            listener.onNotificationSizeNotViewChanged(sizeNotView);
        }
        return notification;
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

    public void changeStatusNotification(DBNotification notification) {
        if (notification == null)
            return;

        if (notification.getStatus() == 1)
            return;

        notification.setStatus(1);

        DatabaseManager.getInstance().updateNotification(notification);

        sizeNotView--;
        if (sizeNotView < 0)
            sizeNotView = 0;

        for (NotificationListener listener : listeners)
            listener.onNotificationSizeNotViewChanged(sizeNotView);
    }

    public List<DBNotification> getListNotification(int page) {
        List<DBNotification> data = DatabaseManager.getInstance().listNotification();
        if (data == null)
            return new ArrayList<>();
        return data;
    }

    public DBNotification getNotificationById(long id) {
        return DatabaseManager.getInstance().getNotificationByID(id);
    }

    public int getSizeNotView() {
        return sizeNotView;
    }
}
