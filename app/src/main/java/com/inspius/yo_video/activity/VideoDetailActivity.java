package com.inspius.yo_video.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.inspius.coreapp.CoreAppActivity;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.fragment.VideoDetailFragment;
import com.inspius.yo_video.model.VideoModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoDetailActivity extends CoreAppActivity {
    private ProgressDialog loadingDialog;
    private VideoModel videoModel;
    private InterstitialAd mInterstitialAd;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        boolean isAutoPlay = getIntent().getExtras().getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);
        setupActionBar();

        mHostActivityImplement.addFragment(VideoDetailFragment.newInstance(videoModel, isAutoPlay), true);

        initAds();
    }

    void initAds() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.admod_app_id));
        
        if (AppConfig.SHOW_ADS_INTERSTITIAL) {
            // Create the InterstitialAd and set the adUnitId.
            mInterstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

            // Loading ads
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    //requestNewInterstitial();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();
                }
            });
            //requestNewInterstitial();
        }
    }

    public void showInterstitialAds() {
        if (!AppConfig.SHOW_ADS_INTERSTITIAL)
            return;

        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            requestNewInterstitial();
        }
    }

    private void requestNewInterstitial() {
        if (!AppConfig.SHOW_ADS_INTERSTITIAL)
            return;

        AdRequest adRequest;
        if (GlobalApplication.getInstance().isProductionEnvironment()) {
            adRequest = new AdRequest.Builder().build();
        } else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        }

        mInterstitialAd.loadAd(adRequest);
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showLoading(String message) {
        if (isFinishing())
            return;

        if (loadingDialog != null && loadingDialog.isShowing())
            return;

        try {
            if (loadingDialog == null) {
                loadingDialog = new ProgressDialog(this);
                loadingDialog.setCancelable(false);
            }
            if (!message.isEmpty())
                loadingDialog.setMessage(message);

            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoading() {
        if (isFinishing())
            return;

        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @Override
    public void handleBackPressInThisActivity() {
        finish();
    }
}
