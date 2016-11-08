package com.inspius.yo_video.service;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.webkit.URLUtil;

import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.model.VideoModel;

import java.util.Map;
import java.util.Set;

/**
 * Created by Billy on 8/2/16.
 */
public class DownloadRequestQueue {
    private static final String TAG = DownloadRequestQueue.class.getSimpleName();

    private static DownloadRequestQueue mInstance;

    Map<Long, VideoModel> mapVideosDownload;
    Context mContext;
    DownloadManager downloadManager;

    public static synchronized DownloadRequestQueue getInstance() {
        if (mInstance == null)
            mInstance = new DownloadRequestQueue();

        return mInstance;
    }

    public DownloadRequestQueue() {
        mContext = GlobalApplication.getAppContext();
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mapVideosDownload = new ArrayMap<>();
    }

    public void downloadVideo(VideoModel mVideo) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mVideo.getVideoUrl()));
        //file type
        //request.setMimeType("application/pdf");

        //if you want to download only over wifi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //to set title of download

        request.setTitle(mVideo.getTitle());
        request.setDescription("File is being downloaded...");

        //if your download is visible in history of downloads
        request.setVisibleInDownloadsUi(false);

        final String fileName = URLUtil.guessFileName(mVideo.getVideoUrl(), null, null);
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_MOVIES,
                fileName);

        //you should store unique queue id
        long queueId = downloadManager.enqueue(request);

        Logger.d(TAG, "queueId = " + queueId);

        mapVideosDownload.put(queueId, mVideo);
    }

    public void cancelAllDownload() {
        for (Map.Entry<Long, VideoModel> entry : mapVideosDownload.entrySet()) {
            long key = entry.getKey();
            cancelDownload(key);
        }
    }

    public void cancelDownload(long queueId) {
        downloadManager.remove(queueId);
        mapVideosDownload.remove(queueId);
    }

    public void onDownloadSuccess(long queueId, String path) {
        VideoModel model = mapVideosDownload.get(queueId);

        String title = model.getTitle();
        int videoID = model.getVideoId();

        DatabaseManager.getInstance().insertVideoToDownloadList(path, title, videoID);

        mapVideosDownload.remove(queueId);
    }
}
