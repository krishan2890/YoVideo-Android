package com.inspius.canyon.yo_video.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.activity.MainActivity;
import com.inspius.canyon.yo_video.activity.PlayerUploadActivity;
import com.inspius.canyon.yo_video.activity.PlayerYoutubeActivity;
import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.AppEnum;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.greendao.WishList;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.service.RecentListManager;
import com.inspius.canyon.yo_video.service.WishListManager;
import com.inspius.coreapp.helper.IntentUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

public class VideoDetailFragment extends BaseMainFragment {
    public static final String TAG = VideoDetailFragment.class.getSimpleName();

    public static VideoDetailFragment newInstance(VideoModel videoModel, boolean isAutoPlay) {
        VideoDetailFragment fragment = new VideoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        bundle.putBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, isAutoPlay);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.tvnTitle)
    TextView tvnTitle;

    @Bind(R.id.tvnAuthor)
    TextView tvnAuthor;

    @Bind(R.id.tvnDescription)
    TextView tvnDescription;

    @Bind(R.id.tvnSeries)
    TextView tvnSeries;

    @Bind(R.id.tvnViewNumber)
    TextView tvnViewNumber;

    @Bind(R.id.imvAddToWishList)
    ImageView imvAddToWishList;

    @Bind(R.id.imvDownload)
    ImageView imvDownload;

    @Bind(R.id.imvThumbnail)
    ImageView imvThumbnail;

    @Bind(R.id.tvnTime)
    TextView tvnTime;

    @Bind(R.id.tvnVip)
    TextView tvnVip;

    @Bind(R.id.ad_view)
    AdView mAdView;

    VideoModel videoModel;
    boolean isAutoPlay;

    WishList wishList;
    Fragment videoFragment;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoModel = (VideoModel) getArguments().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);

        isAutoPlay = getArguments().getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY);

        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.img_video_default)
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

        String headerName = videoModel.getCategoryName();
        if (TextUtils.isEmpty(headerName))
            headerName = "";

        mActivityInterface.updateHeaderTitle(headerName);
        mActivityInterface.setVisibleHeaderMenu(false);

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

        if (isCanBack)
            mHostActivityInterface.popBackStack();

        return true;
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
        tvnViewNumber.setText(videoModel.getViewNumber());

        if (mAccountDataManager.isLogin()) {
            wishList = WishListManager.getInstance().exitWishList(videoModel.getVideoId());
            if (wishList != null)
                imvAddToWishList.setSelected(true);
        }

        RecentListManager recentListManager = RecentListManager.getInstance();
        if (recentListManager.exitWishList(videoModel.getVideoId()) == null) {
            recentListManager.addVideo(videoModel);
        }

        ImageLoader.getInstance().displayImage(videoModel.getImage(), imvThumbnail, options);
        tvnTime.setText(videoModel.getTimeRemain());

        if (videoModel.isVipPlayer())
            tvnVip.setVisibility(View.VISIBLE);
        else
            tvnVip.setVisibility(View.GONE);

        if (AppConfig.SHOW_ADS) {
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
            mActivityInterface.showInterstitialAds();
        } else {
            mAdView.setVisibility(View.GONE);
        }

        if (isAutoPlay)
            doPlayVideo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Logger.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Logger.i("ProfileSharingExample", authorization_code);
                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                mContext,
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Logger.e("ProfileSharingExample", "an extremely unlikely failure occurred: ");
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Logger.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Logger.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    @OnClick(R.id.imvShare)
    void doShare() {
        Intent intent = IntentUtils.shareText(getString(R.string.app_name), videoModel.getSocialLink());
        startActivity(intent);
    }

    @OnClick(R.id.imvAddToWishList)
    void doAddWishList() {
        if (!mAccountDataManager.isLogin()) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_request_login));
            return;
        }

        boolean isExitWishList = imvAddToWishList.isSelected();
        if (isExitWishList)
            WishListManager.getInstance().deleteVideo(wishList);
        else
            wishList = WishListManager.getInstance().addVideo(videoModel);

        imvAddToWishList.setSelected(!isExitWishList);
    }

    @OnClick(R.id.relativePlay)
    void doPlayVideo() {
        if (videoModel == null)
            return;

        if (!isCustomerPlayOrDownloadVideo())
            return;

        Intent intent = null;
        if (videoModel.getVideoType() == AppEnum.VIDEO_TYPE.UPLOAD)
            intent = new Intent(getActivity(), PlayerUploadActivity.class);
        else if (videoModel.getVideoType() == AppEnum.VIDEO_TYPE.YOUTUBE)
            intent = new Intent(getActivity(), PlayerYoutubeActivity.class);
        else if (videoModel.getVideoType() == AppEnum.VIDEO_TYPE.VIMEO) {
            String urlVimeo = String.format("http://player.vimeo.com/video/%s?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800", videoModel.getVideoUrl());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlVimeo)));
            return;
        }

        if (intent == null)
            return;

        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }

    boolean isCustomerPlayOrDownloadVideo() {
        if (videoModel.isVipPlayer() && !mAccountDataManager.isVip()) {
            if (!mAccountDataManager.isLogin()) {
                DialogUtil.showMessageLogin(mContext, getString(R.string.msg_request_login_vip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivityInterface.openLogin(null);
                    }
                });
            } else if (!mAccountDataManager.isVip()) {
                DialogUtil.showMessageVip(mContext, getString(R.string.msg_need_vip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPaypalProfileSharing();
                    }
                });
            }
            return false;
        }

        return true;
    }

    @OnClick(R.id.imvDownload)
    void doDownload() {
        if (!isCustomerPlayOrDownloadVideo())
            return;

        if (imvDownload.isSelected())
            return;

        imvDownload.setSelected(true);
        switch (videoModel.getVideoType()) {
            case UPLOAD:
                /**
                 * Download default Os
                 */
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoModel.getVideoUrl()));
                request.setTitle(videoModel.getTitle());
                request.setDescription("File is being downloading...");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                String nameOfFile = URLUtil.guessFileName(videoModel.getVideoUrl(), null, MimeTypeMap.getFileExtensionFromUrl(videoModel.getVideoUrl()));

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);

                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                /**
                 * Custom Download Manager
                 */

//                PendingIntent pendingResult = getActivity().createPendingResult(
//                        AppConstant.REQUEST_CODE_DOWNLOAD, new Intent(), 0);
//                Intent intent = new Intent(getActivity(), DownloadIntentService.class);
//                intent.putExtra(DownloadIntentService.URL_EXTRA, videoModel.getVideoUrl());
//                intent.putExtra(DownloadIntentService.PENDING_RESULT_EXTRA, pendingResult);
//                getActivity().startService(intent);
                break;

            case YOUTUBE:

                break;
        }
    }


    void onPaypalProfileSharing() {
        Intent intent = new Intent(mContext, PayPalProfileSharingActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, MainActivity.paypalConfig);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());

        startActivityForResult(intent, AppConstant.REQUEST_CODE_PROFILE_SHARING);
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

        mAccountDataManager.setIsVipAccount(true);
    }
}