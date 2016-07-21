package com.inspius.canyon.yo_video.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioPlayerActivity extends CoreAppActivity {
    private VideoModel videoModel;
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;
    @Bind(R.id.videoView)
    VideoView videoView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    // private Uri uri;
    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamio);
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
        if (videoModel.getVideoUrl() == "") {
            return;
        } else {
           // videoView.setVideoPath(path);
            videoView.setVideoURI(Uri.parse(videoModel.getVideoUrl()));
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        } else
            getSupportActionBar().show();
    }


    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
