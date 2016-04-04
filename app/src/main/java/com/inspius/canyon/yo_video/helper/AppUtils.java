package com.inspius.canyon.yo_video.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.model.CategoryJSON;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;

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
}
