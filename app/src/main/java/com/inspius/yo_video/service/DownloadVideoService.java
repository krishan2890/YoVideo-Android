package com.inspius.yo_video.service;

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

import com.inspius.yo_video.R;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.model.VideoModel;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class DownloadVideoService extends IntentService {

    private static final String TAG = DownloadVideoService.class.getSimpleName();
    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String VIDEO_EXTRA = "video";

    public static final String PATH_RESULT_EXTRA = "path";
    public static final String TITLE_RESULT_EXTRA = "title";


    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    private NotificationManager notificationManager;
    private Builder builder;

    private VideoModel mVideo;

    int notificationID;
    int currentProgress;

    public DownloadVideoService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.d(TAG, "Service Started!");
        final PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        try {
            try {
                mVideo = (VideoModel) intent.getSerializableExtra(VIDEO_EXTRA);
                builder = new NotificationCompat.Builder(this);
                builder.setContentTitle(mVideo.getTitle())
                        .setContentText("Video is being downloaded...")
                        .setSmallIcon(R.drawable.ic_launcher);

                notificationID = (new Random()).nextInt();

                final String url = mVideo.getVideoUrl();
                downloadData(url, reply);
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
        currentProgress = progress;
        builder.setProgress(100, progress, false);
        notificationManager.notify(notificationID, builder.build());
    }

    private void showDoneNotification() {
        builder.setContentText("Download complete").setProgress(0, 0, false);

        notificationManager.notify(notificationID, builder.build());
    }

    private void downloadData(String requestUrl, final PendingIntent reply) {
        Logger.d(TAG, requestUrl);

        final File dir = getAlbumStorageDir(getApplicationContext(), "YoVideo");
        final String fileName = URLUtil.guessFileName(requestUrl, null, null);
        File fileSave = new File(dir, fileName);
        currentProgress = 0;

        AppRestClient.download(requestUrl, new FileAsyncHttpResponseHandler(fileSave) {
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
                Logger.d(TAG, file.getAbsolutePath());

                Intent result = new Intent();
                result.putExtra(TITLE_RESULT_EXTRA, mVideo.getTitle());
                result.putExtra(PATH_RESULT_EXTRA, file.getAbsolutePath());

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
                if (increment != currentProgress)
                    showProgressNotification(increment);
                if(increment == 10){
                    AppRestClient.cancelAllRequestsSyncHttpClient();
                    Logger.d(TAG, "onCancel | " + mVideo.getTitle());
                }
            }

            @Override
            public void onCancel() {
                Logger.d(TAG, "onCancel | " + mVideo.getTitle());
                super.onCancel();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy | " + mVideo.getTitle());
        this.notificationManager.cancelAll();
    }
}
