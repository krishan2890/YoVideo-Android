package com.inspius.canyon.yo_video.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.malmstein.fenster.controller.FensterPlayerControllerVisibilityListener;
import com.malmstein.fenster.play.FensterVideoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerUploadActivity extends AppCompatActivity implements FensterPlayerControllerVisibilityListener {
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    private VideoModel videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_upload);
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

        String strHeader = videoModel.getTitle();
        if (TextUtils.isEmpty(strHeader))
            strHeader = "";

        tvnHeaderTitle.setText(strHeader);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initVideo();
    }

    private void initVideo() {
        findVideoFragment().setVisibilityListener(this);
        findVideoFragment().playExampleVideo();
    }

    private FensterVideoFragment findVideoFragment() {
        return (FensterVideoFragment) getFragmentManager().findFragmentById(R.id.play_demo_fragment);
    }

    @Override
    public void onControlsVisibilityChange(boolean value) {
        setSystemUiVisibility(value);

        if (value) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }
    }

    private void setSystemUiVisibility(final boolean visible) {
        int newVis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (!visible) {
            newVis |= View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(newVis);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(final int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
                    findVideoFragment().showFensterController();
                }
            }
        });
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }
}
