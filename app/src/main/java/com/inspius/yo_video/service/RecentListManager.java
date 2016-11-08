package com.inspius.yo_video.service;

import com.inspius.yo_video.greendao.DBRecentVideo;
import com.inspius.yo_video.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 1/11/16.
 */
public class RecentListManager {
    private static RecentListManager mInstance;

    public static RecentListManager newInstance(int userId) {
        return new RecentListManager(userId);
    }

    public static RecentListManager getInstance() {
        return mInstance;
    }

    public static void removeInstance() {
        mInstance = null;
    }

    List<DBRecentVideo> dataRecentVideo;
    int userId;

    public RecentListManager(int userId) {
        mInstance = this;
        this.userId = userId;
        loadData();
    }

    public void loadData(){
        dataRecentVideo = DatabaseManager.getInstance().listVideoAtRecent(userId);
        if (dataRecentVideo == null)
            dataRecentVideo = new ArrayList<>();
    }

    public DBRecentVideo addVideo(VideoModel videoModel) {
        if (videoModel == null)
            return null;

        int index = existVideos(videoModel.getVideoId());
        if (index >= 0)
            return null;

        DBRecentVideo model = new DBRecentVideo();
        model.setVideoId(videoModel.getVideoId());
        model.setCategory(videoModel.getCategoryName());
        model.setName(videoModel.getTitle());
        model.setImage(videoModel.getImage());
        model.setLink(videoModel.getVideoUrl());
        model.setSeries(videoModel.getSeries());
        model.setView(videoModel.getViewNumber());
        model.setUserID(userId);

        DBRecentVideo video = DatabaseManager.getInstance().insertVideoToRecentList(model);

        dataRecentVideo.add(0, video);

        int size = dataRecentVideo.size();
        if (size > 10) {
            DatabaseManager.getInstance().deleteVideoAtRecentList(dataRecentVideo.get(size - 1).getId());
            dataRecentVideo.remove(size - 1);
        }
        return video;
    }

    public List<DBRecentVideo> getRecentVideo() {
        return dataRecentVideo;
    }

    private int existVideos(long videoId) {
        if (dataRecentVideo == null || dataRecentVideo.isEmpty())
            return -1;

        for (int i = 0; i < dataRecentVideo.size(); i++) {
            DBRecentVideo model = dataRecentVideo.get(i);
            if (videoId == model.getVideoId())
                return i;
        }

        return -1;
    }
}