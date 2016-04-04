package com.inspius.canyon.yo_video.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "WISH_LIST".
 */
public class WishList {

    private Long id;
    private long videoId;
    private String name;

    public WishList() {
    }

    public WishList(Long id) {
        this.id = id;
    }

    public WishList(Long id, long videoId, String name) {
        this.id = id;
        this.videoId = videoId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}