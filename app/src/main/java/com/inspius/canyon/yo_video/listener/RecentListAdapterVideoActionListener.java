package com.inspius.canyon.yo_video.listener;

import com.inspius.canyon.yo_video.greendao.DBRecentVideo;
import com.inspius.canyon.yo_video.greendao.NewWishList;

/**
 * Created by Billy on 1/6/16.
 */
public interface RecentListAdapterVideoActionListener extends AdapterActionListener {
    void onPlayVideoListener(int position, DBRecentVideo model);
}
