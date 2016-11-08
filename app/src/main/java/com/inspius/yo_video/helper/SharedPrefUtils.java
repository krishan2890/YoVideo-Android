package com.inspius.yo_video.helper;

import android.content.SharedPreferences;

import com.inspius.yo_video.app.GlobalApplication;

public class SharedPrefUtils {
    String TAG = SharedPrefUtils.class.getSimpleName();

    private static SharedPreferences getSharePreferenceInstance() {
        return GlobalApplication.getInstance().getSharedPreferences();
    }

    public static void saveToPrefs(String key, String value) {
        final SharedPreferences.Editor editor = getSharePreferenceInstance().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveToPrefs(String key, int value) {
        final SharedPreferences.Editor editor = getSharePreferenceInstance().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveToPrefs(String key, boolean value) {
        final SharedPreferences.Editor editor = getSharePreferenceInstance().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by
     * given key. Default value will be returned of no value found or error
     * occurred.
     *
     * @param key          Key to find value against
     * @param defaultValue Value to return if no dataWishList found against given key
     * @return Return the value found against given key, default if not found or
     * any error occurs
     */
    public static String getFromPrefs(String key, String defaultValue) {
        try {
            return getSharePreferenceInstance().getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static int getFromPrefs(String key, int defaultValue) {
        try {
            return getSharePreferenceInstance().getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean getBooleanFromPrefs(String key, boolean defaultValue) {
        try {
            return getSharePreferenceInstance().getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * @param key Key to delete from SharedPreferences
     */
    public static void removeFromPrefs(String key) {
        final SharedPreferences.Editor editor = getSharePreferenceInstance().edit();
        editor.remove(key);
        editor.commit();
    }
}
