package com.inspius.yo_video.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;

import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.VideoDetailActivity;
import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.model.CategoryJSON;
import com.inspius.yo_video.model.DataCategoryJSON;
import com.inspius.yo_video.model.VideoModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;

import com.inspius.yo_video.modules.module_video_detail_jw.MJWVideoDetailActivity;
import com.inspius.yo_video.modules.module_video_detail_jw.MMp3DetailActivity;
import com.inspius.yo_video.modules.module_video_detail_jw.MWebVideoDetailActivity;
import com.inspius.yo_video.modules.module_video_detail_jw.MYoutubeVideoDetailActivity;
import com.inspius.yo_video.player.ExoPlayerActivity;

/**
 * Created by Billy on 1/6/16.
 */
public class AppUtils {
    private static final int[] colorTopCategories = {R.color.category_1, R.color.category_2, R.color.category_3, R.color.category_4, R.color.category_5};

    public static int getBackgroundIconTopCategory(int pos) {
        if (pos >= 0 && pos < colorTopCategories.length)
            return colorTopCategories[pos];

        return colorTopCategories[(new Random()).nextInt(colorTopCategories.length)];
    }

    public static String getCategoryName(DataCategoryJSON dataCategory, long categoryID) {
        String categoryName = "";
        if (dataCategory == null || dataCategory.listCategory == null || dataCategory.listCategory.isEmpty())
            return String.valueOf(categoryID);

        for (CategoryJSON category : dataCategory.listCategory) {
            if (categoryID == category.id) {
                categoryName = category.name;
                break;
            }
        }

        if (TextUtils.isEmpty(categoryName))
            categoryName = GlobalApplication.getAppContext().getString(R.string.other);

        return categoryName;
    }

    public static String getStatsFormat(String value) {
        try {
            DecimalFormat digitformat = new DecimalFormat("###,###,###,###");
            return digitformat.format(Long.valueOf(value));
        } catch (NumberFormatException numberFormatExp) {
            return value;
        }
    }

    /**
     * show hash key
     *
     * @param context
     */
    public static void printHashKey(Context context) {
        if (GlobalApplication.getInstance().isProductionEnvironment())
            return;
        try {
            String TAG = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Logger.d(TAG, "keyHash: " + keyHash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getFacebookProfilePicture(String userID) {
        return "https://graph.facebook.com/" + userID + "/picture?type=large";
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

            return false;
        }

        return true;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static Intent getIntentVideoDetail(Context mContext, VideoModel videoModel, boolean isAutoPlay) {
        Intent intent = null;
        switch (AppConfig.VIDEO_DETAIL_SCREEN) {
            case PLUGIN_DETAIL_JW:
                intent = getIntentVideoDetailJW(mContext, videoModel, isAutoPlay);
                break;

            default:
                intent = getIntentVideoDetailDefault(mContext, videoModel);
                break;
        }
        return intent;
    }

    private static Intent getIntentVideoDetailDefault(Context mContext, VideoModel videoModel) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);

        return intent;
    }

    public static Intent getIntentVideoDetailJW(Context mContext, VideoModel videoModel, boolean isAutoPlay) {
        Intent intent = null;
        switch (videoModel.getVideoType()) {
            case YOUTUBE:
                intent = new Intent(mContext, MYoutubeVideoDetailActivity.class);
                break;

            case UPLOAD:
                intent = new Intent(mContext, MJWVideoDetailActivity.class);
                break;

            case MP3:
                intent = new Intent(mContext, MMp3DetailActivity.class);
                break;

            default:
                intent = new Intent(mContext, MWebVideoDetailActivity.class);
                break;
        }

        intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, isAutoPlay);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);

        return intent;
    }
}
