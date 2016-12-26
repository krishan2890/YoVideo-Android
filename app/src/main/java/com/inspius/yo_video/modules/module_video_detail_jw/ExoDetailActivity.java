package com.inspius.yo_video.modules.module_video_detail_jw;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by Billy on 12/1/16.
 */

public class ExoDetailActivity extends MBaseVideoDetailActivity {
    @Override
    void initPlayer() {
        ExoPlayerFragment playerFragment = ExoPlayerFragment.newInstance(videoModel, isAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
