package com.inspius.yo_video.modules.module_video_detail_jw;

import android.support.v4.app.FragmentTransaction;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

/**
 * Created by Billy on 12/1/16.
 */

public class MJWVideoDetailActivity extends MBaseVideoDetailActivity {
    JWPlayerSupportFragment playerFragment;

    @Override
    void initPlayer() {
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .file(videoModel.getVideoUrl())
                .autostart(isAutoPlay)
                .image(videoModel.getImage())
                .build();

        playerFragment = JWPlayerSupportFragment.newInstance(playerConfig);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        playerFragment.getPlayer().onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        playerFragment.getPlayer().onPause();
        super.onPause();
    }
}
