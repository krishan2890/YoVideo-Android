package com.inspius.canyon.yo_video.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.fragment.WebViewPlayVimeoFragment;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerVimeoActivity extends CoreAppActivity {
    private VideoModel videoModel;
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vimeo);
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
        String urlVimeo = String.format("%s?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800", videoModel.getVideoUrl());

        addFragment(WebViewPlayVimeoFragment.newInstance(urlVimeo), true);

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

}
