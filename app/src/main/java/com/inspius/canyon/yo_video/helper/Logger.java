package com.inspius.canyon.yo_video.helper;

import android.util.Log;

import com.inspius.canyon.yo_video.app.GlobalApplication;

public class Logger {
    public static boolean DEBUG_MODE = !GlobalApplication.getInstance().isProductionEnvironment();
    public static boolean DEBUG_DB = !GlobalApplication.getInstance().isProductionEnvironment();

    public static void e(String TAG, String msg) {
        if (DEBUG_MODE && msg != null)
            Log.e(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (DEBUG_MODE && msg != null)
            Log.d(TAG, msg);
    }

    public static void i(String TAG, String msg) {
        if (DEBUG_MODE && msg != null)
            Log.i(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        if (DEBUG_MODE && msg != null)
            Log.w(TAG, msg);

    }

    public static void e(String TAG, String msg, boolean hasLOG) {
        if (DEBUG_MODE && msg != null)
            Log.e(TAG, msg);

    }

    public static void d(String TAG, String msg, boolean hasLOG) {
        if (DEBUG_MODE && msg != null)
            Log.d(TAG, msg);

    }

    public static void i(String TAG, String msg, boolean hasLOG) {
        if (DEBUG_MODE && msg != null)
            Log.i(TAG, msg);

    }

    public static void w(String TAG, String msg, boolean hasLOG) {
        if (DEBUG_MODE && msg != null)
            Log.w(TAG, msg);

    }

    public static void logDB(String TAG, String msg) {
        if (DEBUG_DB && msg != null)
            Log.e(TAG, msg);
    }
}