package com.inspius.canyon.yo_video.listener;

import com.inspius.canyon.yo_video.greendao.NewWishList;
import com.inspius.canyon.yo_video.model.VideoModel;

/**
 * Created by Billy on 1/6/16.
 */
public interface WishLishAdapterVideoActionListener extends AdapterActionListener {
    void onPlayVideoListener(int position, NewWishList model);
}
