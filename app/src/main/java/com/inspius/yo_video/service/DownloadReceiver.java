package com.inspius.yo_video.service;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.inspius.yo_video.helper.Logger;

/**
 * Created by Billy on 8/2/16.
 */
public class DownloadReceiver extends BroadcastReceiver {
    private static final String TAG = DownloadReceiver.class.getSimpleName();

    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            DownloadManager mDownloadManager =
                    (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor c = mDownloadManager.query(query);
            if (c.moveToFirst()) {
                int columnIndex = c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS);

                //if completed successfully
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
                    //check if this is your download uri
                    Logger.d(TAG, uri.toString());

//                    if (!uri.contains("com.example.app"))
//                        return;

                    //here you get file path so you can move
                    //it to other location if you want
                    String downloadedPackageUriString =
                            c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

                    Logger.d(TAG,downloadId +" | "+ downloadedPackageUriString);

                    //notify your app that download was completed
                    //with local broadcast receiver
//                    Intent localReceiver = new Intent();
//                    localReceiver.putExtra("ID", id);
//                    LocalBroadcastManager
//                            .getInstance(context)
//                            .sendBroadcast(localReceiver);

                    DownloadRequestQueue.getInstance().onDownloadSuccess(downloadId, downloadedPackageUriString);
                } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
                    //if failed you can make a retry or whatever
                    //I just delete my id from sqllite
                }
            }
        }
    }
}
