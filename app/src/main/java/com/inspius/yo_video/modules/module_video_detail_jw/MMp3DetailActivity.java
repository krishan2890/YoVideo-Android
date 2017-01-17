package com.inspius.yo_video.modules.module_video_detail_jw;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by Billy on 12/1/16.
 */

public class MMp3DetailActivity extends MBaseVideoDetailActivity {
    @Override
    void initPlayer() {
        MMp3PlayerFragment playerFragment = MMp3PlayerFragment.newInstance(videoModel, isAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
