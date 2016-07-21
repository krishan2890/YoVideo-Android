package com.inspius.canyon.yo_video.listener;

import com.inspius.canyon.yo_video.greendao.DBWishListVideo;

/**
 * Created by Billy on 1/6/16.
 */
public interface WishListAdapterVideoActionListener extends AdapterActionListener {
    void onPlayVideoListener(int position, DBWishListVideo model);
}
