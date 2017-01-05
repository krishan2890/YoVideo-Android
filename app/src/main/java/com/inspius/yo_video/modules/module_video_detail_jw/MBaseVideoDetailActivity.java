package com.inspius.yo_video.modules.module_video_detail_jw;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inspius.coreapp.helper.IntentUtils;
import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.CommentActivity;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.greendao.DBVideoDownload;
import com.inspius.yo_video.greendao.DBWishListVideo;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.AccountDataManager;
import com.inspius.yo_video.service.DatabaseManager;
import com.inspius.yo_video.service.DownloadRequestQueue;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/16.
 */

public abstract class MBaseVideoDetailActivity extends AppCompatActivity {
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Bind(R.id.tvnTitle)
    TextView tvnTitle;

    @Bind(R.id.tvnAuthor)
    TextView tvnAuthor;

    @Bind(R.id.tvnDescription)
    TextView tvnDescription;

    @Bind(R.id.tvnViewNumber)
    TextView tvnViewNumber;

    @Bind(R.id.imvAddToWishList)
    ImageView imvAddToWishList;

    @Bind(R.id.imvDownload)
    ImageView imvDownload;

    @Bind(R.id.imvLike)
    ImageView imvLike;

    @Bind(R.id.ad_view)
    AdView mAdView;

    @Bind(R.id.tvnSeries)
    TextView tvnSeries;

    @Bind(R.id.linearContent)
    LinearLayout linearContent;

    @Bind(R.id.container)
    FrameLayout frameContainer;

    protected VideoModel videoModel;
    protected boolean isAutoPlay;
    protected int containerViewId = R.id.container;

    private AccountDataManager mAccountDataManager;

    abstract void initPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_video_detail_jw);
        ButterKnife.bind(this);

        mAccountDataManager = AccountDataManager.getInstance();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() == null)
            return;

        Bundle arguments = getIntent().getExtras();
        if (arguments == null)
            return;

        if (!arguments.containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) arguments.getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        isAutoPlay = arguments.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);

        initInfo();
        initAds();
        initPlayer();
    }

    void initInfo() {
        tvnTitle.setText(videoModel.getTitle());
        tvnSeries.setText(videoModel.getSeries());
        tvnAuthor.setText(videoModel.getAuthor());
        tvnDescription.setText(Html.fromHtml(videoModel.getDescription()));
        tvnViewNumber.setText(videoModel.getViewNumber() + "views");

        if (mAccountDataManager.isLogin()) {
            boolean isWishList = DatabaseManager.getInstance().existVideoAtWithList((long) videoModel.getVideoId());
            updateStateViewWishList(isWishList);
        } else
            updateStateViewWishList(false);

        DBVideoDownload dbVideoDownload = DatabaseManager.getInstance().getVideoDownloadByVideoID(videoModel.getVideoId());
        if (dbVideoDownload != null)
            updateStateDownloadButton(true);
        else
            updateStateDownloadButton(false);

        getLikeStatus();
    }

    void initAds() {
        if (AppConfig.SHOW_ADS_BANNER) {
            /**
             * Show Banner Ads
             */

            AdRequest adRequest;

            if (GlobalApplication.getInstance().isProductionEnvironment()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
            }

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    if (mAdView != null)
                        mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    mAdView.setVisibility(View.GONE);
                }
            });
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvnHeaderTitle.setText(videoModel.getTitle());
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }


    boolean isCustomerPlayOrDownloadVideo() {
        if (videoModel.isVipPlayer() && !mAccountDataManager.isVip()) {
            if (!mAccountDataManager.isLogin()) {
                DialogUtil.showMessageBox(this, getString(R.string.msg_request_login_vip));
            } else if (!mAccountDataManager.isVip()) {
                DialogUtil.showMessageVip(this, getString(R.string.msg_need_vip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // implement payment
                    }
                });
            }
            return false;
        }

        return true;
    }

    @OnClick(R.id.imvDownload)
    void doDownload() {
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(this, getString(R.string.msg_request_login));

            return;
        }

        if (!isCustomerPlayOrDownloadVideo())
            return;

        if (imvDownload.isSelected())
            return;

        updateStateDownloadButton(true);
        switch (videoModel.getVideoType()) {
            case UPLOAD:
                /**
                 * Download default Os
                 */

                DownloadRequestQueue.getInstance().downloadVideo(videoModel);

                /**
                 * Custom Download Manager
                 */

//                mActivityInterface.downloadVideo(videoModel);

                break;

            case MP3:
                DownloadRequestQueue.getInstance().downloadVideo(videoModel);
                break;

            default:
                updateStateDownloadButton(false);
                DialogUtil.showMessageBox(this, "Download Unsupported File Formats");
                break;
        }
    }

    @OnClick(R.id.imvComment)
    void doComment() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }

    @OnClick(R.id.imvLike)
    void doLike() {
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(this, getString(R.string.msg_request_login));
            return;
        }

        RPC.requestLikeVideo(videoModel.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                String response = (String) results;
                try {
                    JSONObject content = new JSONObject(response);
                    if (content.getString("action").equalsIgnoreCase("Liked"))
                        updateStateLikeButton(true);
                    else
                        updateStateLikeButton(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

        RPC.updateVideoStatic(mAccountDataManager.getAccountID(), "share", videoModel.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onSuccess(Object results) {
            }
        });
    }

    @OnClick(R.id.imvAddToWishList)
    void doAddWishList() {
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(this, getString(R.string.msg_request_login));

            return;
        }

        boolean isWishList = imvAddToWishList.isSelected();
        if (isWishList) {
            DatabaseManager.getInstance().deleteVideoAtWishListByVideoId((long) videoModel.getVideoId());
        } else {
            DBWishListVideo dbWishList = new DBWishListVideo();
            dbWishList.setVideoId(videoModel.getVideoId());
            dbWishList.setCategory(videoModel.getCategoryName());
            dbWishList.setName(videoModel.getTitle());
            dbWishList.setImage(videoModel.getImage());
            dbWishList.setLink(videoModel.getVideoUrl());
            dbWishList.setSeries(videoModel.getSeries());
            dbWishList.setView(videoModel.getViewNumber());
            dbWishList.setUserID(mAccountDataManager.getAccountID());

            DatabaseManager.getInstance().insertVideoToWishList(dbWishList);
        }

        updateStateViewWishList(!isWishList);
    }

    void updateStateViewWishList(boolean isWishList) {
        if (!mAccountDataManager.isLogin()) {
            imvAddToWishList.setSelected(false);
        } else
            imvAddToWishList.setSelected(isWishList);
    }

    void updateStateDownloadButton(boolean isDownload) {
        imvDownload.setSelected(isDownload);
    }

    void updateStateLikeButton(boolean isLiked) {
        imvLike.setSelected(isLiked);
    }

    void getLikeStatus() {
        if (!mAccountDataManager.isLogin()) {
            updateStateLikeButton(false);
            return;
        }

        RPC.requestGetLikeStatusVideo(videoModel.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                String response = (String) results;
                try {
                    JSONObject content = new JSONObject(response);
                    if (content.getString("action").equalsIgnoreCase("Liked"))
                        updateStateLikeButton(true);
                    else
                        updateStateLikeButton(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
