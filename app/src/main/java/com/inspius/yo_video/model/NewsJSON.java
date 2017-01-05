package com.inspius.yo_video.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class NewsJSON implements Serializable {
    public int id;
    public String title;

    @JsonProperty("short_description")
    public String shortDescription;

    public String description;
    public String thumbnail;

    @JsonProperty("create_at")
    public long createAt;

    @JsonProperty("update_at")
    public long updateAt;

    @JsonProperty("view")
    public int viewCounter;
}
