package com.inspius.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Billy on 1/5/16.
 */
public class DataHomeJSON {
    @SerializedName("latest")
    public List<VideoJSON> listVideoLatest;

    @SerializedName("most_view")
    public List<VideoJSON> listVideoMostView;
}
