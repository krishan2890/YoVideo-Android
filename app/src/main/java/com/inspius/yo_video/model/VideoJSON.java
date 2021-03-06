package com.inspius.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class VideoJSON implements Serializable {
    public int id;

    @SerializedName("url_social")
    public String urlSocial;

    @SerializedName("category_id")
    public long categoryId;

    public String series;
    public String author;

    @SerializedName("url_image")
    public String urlImage;

    public String description;
    public String title;

    @SerializedName("update_at")
    public String updateAt;

    @SerializedName("create_at")
    public String createAt;

    @SerializedName("stats")
    public VideoStatsJSON statsJSON;

    @SerializedName("video")
    public VideoLinkJSON videoLinkJSON;

    @SerializedName("vip_play")
    public int vipPlay;

    @SerializedName("another_category_ids")
    public Object anotherCategoryId;
}
