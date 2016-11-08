package com.inspius.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class VideoLinkJSON implements Serializable {
    public String url;
    public String type;
    public String length;
    public String subtitle;
}
