package com.inspius.yo_video.listener;

import com.inspius.yo_video.greendao.DBRecentVideo;

/**
 * Created by Billy on 1/6/16.
 */
public interface RecentListAdapterVideoActionListener extends AdapterActionListener {
    void onPlayVideoListener(int position, DBRecentVideo model);
}
