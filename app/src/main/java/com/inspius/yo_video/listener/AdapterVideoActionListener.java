package com.inspius.yo_video.listener;

import com.inspius.yo_video.model.VideoModel;

/**
 * Created by Billy on 1/6/16.
 */
public interface AdapterVideoActionListener extends AdapterActionListener {
    void onPlayVideoListener(int position, VideoModel model);
}
