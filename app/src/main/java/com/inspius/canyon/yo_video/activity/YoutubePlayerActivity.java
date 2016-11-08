package com.inspius.canyon.yo_video.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.fragment.PageVideoYoutubeFragment;
import com.inspius.canyon.yo_video.model.VideoModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YoutubePlayerActivity extends AppCompatActivity {
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    private VideoModel videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_youtube);
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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PageVideoYoutubeFragment videoFragment = PageVideoYoutubeFragment.newInstance(videoModel, true);
        ft.replace(R.id.container, videoFragment).commit();
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
        onBackPressed();
    }
}
