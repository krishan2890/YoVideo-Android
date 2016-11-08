package com.inspius.canyon.yo_video.model;

import android.text.TextUtils;

import com.inspius.canyon.yo_video.app.AppEnum;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.helper.YouTubeUrlParser;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class VideoModel implements Serializable {
    private VideoJSON videoJSON;
    private String title;
    private String image;
    private long viewNumber;
    private String categoryName;
    private String series;
    private String timeRemain;
    private String author;
    private String description;
    private String socialLink;
    private String videoUrl;
    private AppEnum.VIDEO_TYPE videoType = AppEnum.VIDEO_TYPE.UPLOAD;

    // private String videoType;
    public VideoModel(VideoJSON videoJSON) {
        this.videoJSON = videoJSON;
        this.title = videoJSON.title;
        this.image = videoJSON.urlImage;
        this.viewNumber = videoJSON.statsJSON.views;
        this.series = videoJSON.series;

        if (TextUtils.isEmpty(series))
            series = "No Series";

        this.timeRemain = videoJSON.videoLinkJSON.length;
        this.author = videoJSON.author;
        this.description = videoJSON.description;
        this.socialLink = videoJSON.urlSocial;

        this.videoUrl = videoJSON.videoLinkJSON.url;
        //   this.videoType = videoJSON.videoLinkJSON.type;
        if (!TextUtils.isEmpty(videoJSON.videoLinkJSON.type))
            videoType = AppEnum.VIDEO_TYPE.fromString(videoJSON.videoLinkJSON.type);

    }

    public boolean isVipPlayer() {
        return (videoJSON.vipPlay == 1) ? true : false;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        if (TextUtils.isEmpty(image) && getVideoType() == AppEnum.VIDEO_TYPE.YOUTUBE)
            image = String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", YouTubeUrlParser.getVideoId(getVideoUrl()));

        return image;
    }


    public String getViewNumber() {
        return AppUtils.getStatsFormat(String.valueOf(viewNumber));
    }

    public Long getView() {
        return viewNumber;
    }

    public String getCategoryName() {
        if (TextUtils.isEmpty(categoryName))
            return String.valueOf(getCategoryID());

        return categoryName;
    }

    public String getSeries() {
        return series;
    }

    public String getTimeRemain() {
        return timeRemain;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getCategoryID() {
        return videoJSON.categoryId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoSub() {
        return videoJSON.videoLinkJSON.subtitle;
    }


    public AppEnum.VIDEO_TYPE getVideoType() {
        return videoType;
    }

    public int getVideoId() {
        return videoJSON.id;
    }
}
