package com.inspius.canyon.yo_video.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.webkit.URLUtil;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.helper.FileUtil;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.io.File;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class DownloadIntentService extends IntentService {

    private static final String TAG = DownloadIntentService.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String URL_EXTRA_PATH = "path";
    public static final String URL_VIDEO_EXTRA = "video";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    private NotificationManager notificationManager;
    private Builder builder;

    private VideoModel mVideo;
    private int notificationID;

    private final AsyncHttpClient aClient = new SyncHttpClient();

    public DownloadIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        this.builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("Download Video Service")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher);

        notificationID = (new Random()).nextInt();

        Logger.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        try {
            try {
                mVideo = (VideoModel) intent.getSerializableExtra(URL_VIDEO_EXTRA);
                builder.setContentText(mVideo.getTitle());

                final String url = mVideo.getVideoUrl();
                Logger.d(TAG, url);

                final File dir = getAlbumStorageDir(getApplicationContext(), "YoVideo");
                final String fileName = URLUtil.guessFileName(url, null, null);
                File fileSave = new File(dir, fileName);

                aClient.get(url, new FileAsyncHttpResponseHandler(fileSave) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        try {
                            reply.send(statusCode);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
//                        debugFile(file);

                        Intent result = new Intent();
                        result.putExtra(URL_VIDEO_EXTRA, mVideo);
                        result.putExtra(URL_EXTRA_PATH, file.getAbsolutePath());

                        showDoneNotification();

                        try {
                            reply.send(getApplicationContext(), RESULT_CODE, result);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        int increment = (int) (100 * bytesWritten / totalSize);
                        showProgressNotification(increment);
                    }
                });
            } catch (Exception exc) {
                // could do better by treating the different sax/xml exceptions individually
                reply.send(ERROR_CODE);
            }
        } catch (PendingIntent.CanceledException exc) {
            Log.i(TAG, "reply cancelled", exc);
        }
    }

    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_MOVIES), albumName);
        if (!file.mkdirs()) {
            Logger.e("getAlbumStorageDir", "Directory not created");
        }
        return file;
    }

    private void showProgressNotification(int progress) {
        builder.setProgress(100, progress, false);
        notificationManager.notify(notificationID, builder.build());
    }

    private void showDoneNotification() {
        builder.setContentText("Download complete").setProgress(0, 0, false);

        notificationManager.notify(notificationID, builder.build());
    }

    private void debugFile(File file) {
        if (file == null || !file.exists()) {
            Logger.d(TAG, "Response is null");
            return;
        }
        try {
            Logger.d(TAG, file.getAbsolutePath() + "\r\n\r\n" + FileUtil.getStringFromFile(file));
        } catch (Throwable t) {
            Logger.d(TAG, "Cannot debug file contents");
        }
//        if (!deleteTargetFile()) {
//            Logger.d(TAG, "Could not delete response file " + file.getAbsolutePath());
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
        this.notificationManager.cancel(notificationID);
    }
}
