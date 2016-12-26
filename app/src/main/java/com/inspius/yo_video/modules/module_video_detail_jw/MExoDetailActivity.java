package com.inspius.yo_video.modules.module_video_detail_jw;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by Billy on 12/1/16.
 */

public class MExoDetailActivity extends MBaseVideoDetailActivity {
    @Override
    void initPlayer() {
        MExoPlayerFragment playerFragment = MExoPlayerFragment.newInstance(videoModel, isAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
