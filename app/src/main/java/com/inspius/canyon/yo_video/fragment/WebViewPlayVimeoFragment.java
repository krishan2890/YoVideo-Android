package com.inspius.canyon.yo_video.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseFragment;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class WebViewPlayVimeoFragment extends BaseFragment {
    public static final String TAG = WebViewPlayVimeoFragment.class.getSimpleName();

    public static WebViewPlayVimeoFragment newInstance(String urlPage) {
        WebViewPlayVimeoFragment fragment = new WebViewPlayVimeoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_BUNDLE_URL_PAGE, urlPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.webView)
    WebView webView;

    private String urlPage;

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(AppConstant.KEY_BUNDLE_URL_PAGE))
            return;

        urlPage = bundle.getString(AppConstant.KEY_BUNDLE_URL_PAGE);

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_webview_vimeo;
    }

    @Override
    public void onInitView() {
        if (TextUtils.isEmpty(urlPage))
            return;

        startWebView(urlPage);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }


    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mActivityInterface.updateHeaderTitle(headerName);
//        mActivityInterface.setVisibleHeaderMenu(true);
        webView.loadUrl(urlPage);

    }



    public void startWebView(String url) {

        // Create new webview Client to show progress dialog
        // When opening a link or click on link
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
        //webView.loadUrl(url);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
    }



}
