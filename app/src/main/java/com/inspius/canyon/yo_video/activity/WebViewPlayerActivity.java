package com.inspius.canyon.yo_video.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.coreapp.helper.IntentUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 7/21/2016.
 */
public class WebViewPlayerActivity extends AppCompatActivity {
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.webView)
    WebView webView;

    private VideoModel videoModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_player);
        ButterKnife.bind(this);

        // action bar
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

        String url = "";
        switch (videoModel.getVideoType()) {
            case VIMEO:
                url = String.format("%s?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800", videoModel.getVideoUrl());
                break;
            case FACEBOOK:
                url = String.format("http://test.inspius.com/yovideo/api/playFacebookVideo?video_url=%s", videoModel.getVideoUrl());
                break;
        }

        if (TextUtils.isEmpty(url))
            return;

        startWebView(url);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else
            getSupportActionBar().show();
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    public void startWebView(String url) {
        startAnimLoading();
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient() {
            //ProgressDialog progressDialog;

            // If you will not use this method link links are opeen in new brower
            // not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (view != null)
                    view.loadUrl(url);
                return true;
            }

            // Show loader on link load
            public void onLoadResource(WebView view, String url) {
                // show Loading

            }

            public void onPageFinished(WebView view, String url) {
                // dismiss loading
                stopAnimLoading();
                if (webView != null)
                    webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                stopAnimLoading();
                if (webView != null)
                    webView.setVisibility(View.INVISIBLE);
            }
        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @OnClick(R.id.imvShare)
    void doShare() {
        if (videoModel == null)
            return;

        String urlShare = videoModel.getSocialLink();
        if (TextUtils.isEmpty(urlShare))
            urlShare = videoModel.getVideoUrl();

        if (TextUtils.isEmpty(urlShare))
            return;

        Intent intent = IntentUtils.shareText(getString(R.string.app_name), urlShare);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (webView != null)
            webView.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (webView != null)
            webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null)
            webView.destroy();
    }
}
