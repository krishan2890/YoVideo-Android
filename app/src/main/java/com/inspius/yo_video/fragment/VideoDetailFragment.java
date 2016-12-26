package com.inspius.yo_video.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inspius.coreapp.helper.IntentUtils;
import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.CommentActivity;
import com.inspius.yo_video.player.DailyMotionPlayerActivity;
import com.inspius.yo_video.player.ExoPlayerActivity;
import com.inspius.yo_video.player.JWPlayerActivity;
import com.inspius.yo_video.player.MusicPlayerActivity;
import com.inspius.yo_video.activity.VideoDetailActivity;
import com.inspius.yo_video.player.VitamioPlayerActivity;
import com.inspius.yo_video.player.WebViewPlayerActivity;
import com.inspius.yo_video.player.YoutubePlayerActivity;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseFragment;
import com.inspius.yo_video.greendao.DBVideoDownload;
import com.inspius.yo_video.greendao.DBWishListVideo;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.DatabaseManager;
import com.inspius.yo_video.service.DownloadRequestQueue;
import com.inspius.yo_video.service.RecentListManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class VideoDetailFragment extends BaseFragment {
    public static final String TAG = VideoDetailFragment.class.getSimpleName();
    private VideoDetailActivity mVideoDetailActivity;

    public static VideoDetailFragment newInstance(VideoModel videoModel, boolean isAutoPlay) {
        VideoDetailFragment fragment = new VideoDetailFragment();
        fragment.videoModel = videoModel;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

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

    @Bind(R.id.imvThumbnail)
    ImageView imvThumbnail;

    @Bind(R.id.tvnTime)
    TextView tvnTime;

    @Bind(R.id.tvnVip)
    TextView tvnVip;

    @Bind(R.id.ad_view)
    AdView mAdView;

    @Bind(R.id.tvnSeries)
    TextView tvnSeries;

    VideoModel videoModel;
    boolean isAutoPlay;

    Fragment videoFragment;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoDetailActivity = (VideoDetailActivity) getActivity();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_video_default)
                .showImageForEmptyUri(R.drawable.img_video_default)
                .showImageOnFail(R.drawable.img_video_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.destroy();
        }

        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_UPDATE_STATIC);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        boolean isCanBack = true;
        if (videoFragment != null) {
            switch (videoModel.getVideoType()) {
                case UPLOAD:
                    isCanBack = true;
                    break;

                case YOUTUBE:
                    PageVideoYoutubeFragment youtubeFragment = (PageVideoYoutubeFragment) videoFragment;
                    isCanBack = youtubeFragment.onBackPress();
                    break;
            }
        }

        if (!isCanBack)
            return true;

        return mHostActivityInterface.popBackStack();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_detail;
    }

    @Override
    public void onInitView() {
        if (videoModel == null)
            return;

        tvnTitle.setText(videoModel.getTitle());
        tvnSeries.setText(videoModel.getSeries());
        tvnAuthor.setText(videoModel.getAuthor());
        tvnDescription.setText(Html.fromHtml(videoModel.getDescription()));
        tvnViewNumber.setText(videoModel.getViewNumber() + "views");

        boolean isWishList = DatabaseManager.getInstance().existVideoAtWithList((long) videoModel.getVideoId());
        updateStateViewWishList(isWishList);

        DBVideoDownload dbVideoDownload = DatabaseManager.getInstance().getVideoDownloadByVideoID(videoModel.getVideoId());
        if (dbVideoDownload != null)
            updateStateDownloadButton(true);
        else
            updateStateDownloadButton(false);

        ImageLoader.getInstance().displayImage(videoModel.getImage(), imvThumbnail, options);
        tvnTime.setText(videoModel.getTimeRemain());

        if (videoModel.isVipPlayer())
            tvnVip.setVisibility(View.VISIBLE);
        else
            tvnVip.setVisibility(View.GONE);

        if (AppConfig.SHOW_ADS_BANNER) {
            /**
             * Show Banner Ads
             */

            AdRequest adRequest;

            if (mApplication.isProductionEnvironment()) {
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

            /**
             * Show Interstitial Ads
             */
            mVideoDetailActivity.showInterstitialAds();
        } else {
            mAdView.setVisibility(View.GONE);
        }

        if (isAutoPlay)
            doPlayVideo();

        RPC.updateVideoStatic(videoModel.getVideoId(), "view", mAccountDataManager.getAccountID(), new APIResponseListener() {
            @Override
            public void onError(String message) {
                Logger.d("fail", "fail");
            }

            @Override
            public void onSuccess(Object results) {
                Logger.d("success", "success");
            }
        });

        getLikeStatus();
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
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));

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

    @OnClick(R.id.relativePlay)
    void doPlayVideo() {
        if (videoModel == null)
            return;

        if (!isCustomerPlayOrDownloadVideo())
            return;

        RecentListManager recentListManager = RecentListManager.getInstance();
        if (recentListManager != null)
            recentListManager.addVideo(videoModel);

        Intent intent;
        switch (videoModel.getVideoType()) {
            case YOUTUBE:
                intent = new Intent(mContext, YoutubePlayerActivity.class);
                break;

            case VIMEO:
                intent = new Intent(mContext, WebViewPlayerActivity.class);
                break;

            case FACEBOOK:
                intent = new Intent(mContext, WebViewPlayerActivity.class);
                break;

            case DAILY_MOTION:
                intent = new Intent(mContext, DailyMotionPlayerActivity.class);
                break;

            case MP3:
                intent = new Intent(mContext, MusicPlayerActivity.class);
                break;

            case JW_PLAYER:
                intent = new Intent(mContext, JWPlayerActivity.class);
                break;

            default:
//                intent = new Intent(mContext, VitamioPlayerActivity.class);
                intent = new Intent(mContext, ExoPlayerActivity.class);
                intent.setData(Uri.parse(videoModel.getVideoUrl()))
                        // .putExtra(ExoPlayerActivity.EXTENSION_EXTRA, "mp4")
                        .setAction(ExoPlayerActivity.ACTION_VIEW);
                break;
        }

        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);

        startActivity(intent);
    }

    boolean isCustomerPlayOrDownloadVideo() {
        if (videoModel.isVipPlayer() && !mAccountDataManager.isVip()) {
            if (!mAccountDataManager.isLogin()) {
                DialogUtil.showMessageLogin(mContext, getString(R.string.msg_request_login_vip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mActivityInterface.openLogin(null);
                    }
                });
            } else if (!mAccountDataManager.isVip()) {
                DialogUtil.showMessageVip(mContext, getString(R.string.msg_need_vip), new DialogInterface.OnClickListener() {
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

    @OnClick(R.id.tvnSeries)
    void doClickSeries() {
        String series = tvnSeries.getText().toString();
        if (series.equalsIgnoreCase("No Series"))
            return;

        mHostActivityInterface.addFragment(SeriesFragment.getInstance(videoModel), false);
    }

    @OnClick(R.id.imvDownload)
    void doDownload() {
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));

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
                DialogUtil.showMessageBox(mContext, "Download Unsupported File Formats");
                break;
        }
    }

    @OnClick(R.id.imvComment)
    void doComment() {
//        mHostActivityInterface.addFragment(CommentFragment.newInstance(videoModel), true);
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }

    @OnClick(R.id.imvLike)
    void doLike() {
        if (!mAccountDataManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));
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