package com.inspius.yo_video.modules.news;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class MNewsCategoryJSON implements Serializable {
    public int id;
    public String thumbnail;
    public String icon;
    public String title;

    @JsonProperty("create_at")
    public long createAt;

    @JsonProperty("update_at")
    public long updateAt;
}
