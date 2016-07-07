package com.inspius.canyon.yo_video.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerFacebookActivity extends CoreAppActivity {
    private VideoModel videoModel;

    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_facebook);
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
       // String urlFacebook = String.format("%s?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800", videoModel.getVideoUrl());
        String urlFacebook = String.format("http://test.inspius.com/yovideo/api/playFacebookVideo?video_url=%s", videoModel.getVideoUrl());
        startWebView(urlFacebook);
    }

    public void startWebView(String url) {

        // Create new webview Client to show progress dialog
        // When opening a link or click on link

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
         * webView.getSettings().setLoadWithOverviewMode(true);
		 * webView.getSettings().setUseWideViewPort(true);
		 * webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		 * webView.setScrollbarFadingEnabled(false);
		 * webView.getSettings().setBuiltInZoomControls(true);
		 */

		/*
         * String summary =
		 * "<html><body>You scored <b>192</b> points.</body></html>";
		 * webview.loadData(summary, "text/html", null);
		 */

        // Load link in webview
        webView.loadUrl(url);
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
