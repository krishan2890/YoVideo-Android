package com.inspius.yo_video.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;
import com.longtailvideo.jwplayer.JWPlayerFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class JWPlayerActivity extends AppCompatActivity {
    public static final String TAG = JWPlayerActivity.class.getSimpleName();

    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    JWPlayerFragment playerFragment;

    private VideoModel videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_jw);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        tvnHeaderTitle.setText(videoModel.getTitle());

        playerFragment = (JWPlayerFragment) getFragmentManager().findFragmentById(R.id.playerFragment);
        playVideo();
    }

    void playVideo() {
        String path = videoModel.getVideoUrl();

        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "Please edit MediaPlayer Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show();

            return;
        }

        JWPlayerView playerView = playerFragment.getPlayer();

        PlaylistItem video = new PlaylistItem(path);

        // Load a stream into the player
        playerView.load(video);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
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

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
//        playerFragment.getPlayer().onDestroy();
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
