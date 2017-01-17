package com.inspius.yo_video.modules.module_video_detail_jw;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by Billy on 12/1/16.
 */

public class MWebVideoDetailActivity extends MBaseVideoDetailActivity {

    @Override
    void initPlayer() {
        MVideoThumbnailFragment playerFragment = MVideoThumbnailFragment.newInstance(videoModel, isAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
