package com.inspius.yo_video.modules.module_video_detail_jw;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.helper.YouTubeUrlParser;

/**
 * Created by Billy on 12/1/16.
 */

public class MYoutubeVideoDetailActivity extends MBaseVideoDetailActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayer youTubePlayer;

    @Override
    void initPlayer() {
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();
        playerFragment.initialize(AppConfig.DEVELOPER_YOUTUBE_KEY, this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(youTubePlayer!=null)
                youTubePlayer.setFullscreen(true);
        } else
            youTubePlayer.setFullscreen(false);
    }

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        if (restored) {
            youTubePlayer.play();
        } else {
            String mYoutubeId = YouTubeUrlParser.getVideoId(videoModel.getVideoUrl());
            if (isAutoPlay)
                youTubePlayer.loadVideo(mYoutubeId);
            else
                youTubePlayer.cueVideo(mYoutubeId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(this, R.string.error_init_failure, Toast.LENGTH_LONG).show();
        }
    }
}
