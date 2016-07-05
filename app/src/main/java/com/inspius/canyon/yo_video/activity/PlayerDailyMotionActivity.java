package com.inspius.canyon.yo_video.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.helper.DMWebVideoView;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerDailyMotionActivity extends CoreAppActivity {
    private VideoModel videoModel;
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;
    @Bind(R.id.dmWebVideoView)
    DMWebVideoView videoView;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymotion);
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
            finish();

        tvnHeaderTitle.setText(videoModel.getTitle());
        videoView.setVideoId("x26hv6c");
        videoView.loadUrl(videoModel.getVideoUrl());
        //videoView.setAutoPlay(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
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
