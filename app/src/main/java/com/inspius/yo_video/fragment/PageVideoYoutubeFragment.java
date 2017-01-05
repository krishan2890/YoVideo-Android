package com.inspius.yo_video.fragment;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.helper.YouTubeUrlParser;
import com.inspius.yo_video.model.VideoModel;

/**
 * Created by Billy on 1/7/16.
 */
public class PageVideoYoutubeFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {
    public static final String TAG = PageVideoYoutubeFragment.class.getSimpleName();
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    public static PageVideoYoutubeFragment newInstance(VideoModel model, boolean isAutoPlay) {
        PageVideoYoutubeFragment youTubeFragment = new PageVideoYoutubeFragment();
        final Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID, YouTubeUrlParser.getVideoId(model.getVideoUrl()));
        bundle.putBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, isAutoPlay);
        youTubeFragment.setArguments(bundle);
        return youTubeFragment;
    }

    private String mVideoId;
    private boolean isAutoPlay;
    private boolean isFullScreen;
    private YouTubePlayer youTubePlayer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        final Bundle arguments = getArguments();

        if (bundle != null && bundle.containsKey(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID)) {
            mVideoId = bundle.getString(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID);
            isAutoPlay = bundle.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY);

        } else if (arguments != null && arguments.containsKey(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID)) {
            mVideoId = arguments.getString(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID);
            isAutoPlay = arguments.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY);
        }

        initialize(AppConfig.DEVELOPER_YOUTUBE_KEY, this);
    }

    /**
     * Set the video id and initialize the player
     * This can be used when including the Fragment in an XML layout
     *
     * @param videoId The ID of the video to play
     */
    public void setVideoId(final String videoId) {
        mVideoId = videoId;
        initialize(AppConfig.DEVELOPER_YOUTUBE_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        int controlFlags = youTubePlayer.getFullscreenControlFlags();
        getActivity().setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        youTubePlayer.setFullscreenControlFlags(controlFlags);

        if (mVideoId != null) {
            if (restored) {
                youTubePlayer.play();
            } else {
                if (isAutoPlay)
                    youTubePlayer.loadVideo(mVideoId);
                else
                    youTubePlayer.cueVideo(mVideoId);
            }
        }

        youTubePlayer.setOnFullscreenListener(this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(getActivity(), R.string.error_init_failure, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(AppConstant.KEY_BUNDLE_VIDEO_YOUTUBE_ID, mVideoId);
    }

    @Override
    public void onFullscreen(boolean b) {
        isFullScreen = b;
        Logger.d(TAG, String.valueOf(isFullScreen));
    }

    public boolean onBackPress() {
        Logger.d(TAG, String.valueOf(isFullScreen));
        if (isFullScreen) {
            if (youTubePlayer != null)
                youTubePlayer.setFullscreen(false);

            return false;
        }

        return true;
    }
}
