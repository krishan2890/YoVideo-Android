package com.inspius.canyon.yo_video.model;

import java.io.Serializable;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationJSON implements Serializable {
    public long id;
    public String title;
    public String message;
    public String content;
    public int status;
}
