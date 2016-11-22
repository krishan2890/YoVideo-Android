package com.inspius.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class CommentJSON implements Serializable{
    public int id;

    @SerializedName("comment_text")
    public String commentText;

    @SerializedName("create_at")
    public long createAt;

    @SerializedName("video_id")
    public long videoId;

    @SerializedName("user")
    public CustomerModel user;
}
