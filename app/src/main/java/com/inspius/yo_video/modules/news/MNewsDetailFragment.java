package com.inspius.yo_video.modules.news;

import android.content.Intent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.inspius.coreapp.helper.IntentUtils;
import com.inspius.yo_video.R;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.helper.ConnectionDetector;
import com.inspius.yo_video.helper.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsDetailFragment extends BaseMainFragment {
    public static final String TAG = MNewsDetailFragment.class.getSimpleName();

    public static MNewsDetailFragment newInstance(MNewsModel newsModel) {
        MNewsDetailFragment fragment = new MNewsDetailFragment();
        fragment.newsModel = newsModel;
        return fragment;
    }

    @Bind(R.id.tvnMessage)
    TextView tvnMessage;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.webView)
    WebView mWebView;

    protected String headerName;
    protected String urlContent;
    protected String urlShare;
    private MNewsModel newsModel;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_detail;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        headerName = newsModel.getTitle();
        urlContent = newsModel.getDetailPath();
        urlShare = newsModel.getDetailPath();

        startWebView(urlContent);
        MNewsRPC.updateNewsViewCounter(newsModel.getID(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.loadUrl(urlContent);

        mActivityInterface.updateHeaderTitle(headerName);
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null)
            mWebView.onPause();
    }

    void onShareClicked() {
        Intent intent = IntentUtils.shareText(headerName, urlShare);
        startActivity(intent);
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
        Logger.d("startWebView", url);
        // Create new webview Client to show progress dialog
        // When opening a link or click on link
        startAnimLoading();
        mWebView.setVisibility(View.INVISIBLE);
        tvnMessage.setVisibility(View.GONE);

        mWebView.setWebViewClient(new WebViewClient() {
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

            }

            public void onPageFinished(WebView view, String url) {
                // dismiss loading
                stopAnimLoading();
                if (mWebView != null)
                    mWebView.setVisibility(View.VISIBLE);

                tvnMessage.setVisibility(View.GONE);
                if (!(new ConnectionDetector(mContext)).isConnectingToInternet()) {
                    mWebView.setVisibility(View.GONE);
                    tvnMessage.setVisibility(View.VISIBLE);
                    tvnMessage.setText(getText(R.string.no_internet));
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                stopAnimLoading();
                if (mWebView != null)
                    mWebView.setVisibility(View.INVISIBLE);

                tvnMessage.setVisibility(View.VISIBLE);
                tvnMessage.setText("The web page opening slowly or not loading");
            }
        });

        // Javascript inabled on webview
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    @OnClick(R.id.tvnMessage)
    void doReload() {
        tvnMessage.setVisibility(View.GONE);
        mWebView.loadUrl(urlContent);
    }
}
