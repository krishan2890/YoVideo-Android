package com.inspius.canyon.yo_video.helper;
//
//import android.app.Activity;
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.inspius.canyon.yo_video.app.AppConfig;
//import com.parse.Parse;
//import com.parse.ParseACL;
//import com.parse.ParseInstallation;
//
///**
// * Created by Ravi on 01/06/15.
// */
//public class ParseUtils {
//
//    private static String TAG = ParseUtils.class.getSimpleName();
//
//    public static boolean verifyParseConfiguration(Context context) {
//        if (TextUtils.isEmpty(AppConfig.PARSE_APPLICATION_ID) || TextUtils.isEmpty(AppConfig.PARSE_CLIENT_KEY)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public static void registerParse(Context context) {
//        Parse.enableLocalDatastore(context);
//
//        // initializing parse library
//        Parse.initialize(context, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
//
//        ParseInstallation.getCurrentInstallation().saveInBackground();
//
////        ParsePush.subscribeInBackground(AppConfig.PARSE_CHANNEL, new SaveCallback() {
////            @Override
////            public void done(ParseException e) {
////                Log.e(TAG, "Successfully subscribed to Parse!");
////            }
////        });
//
//        ParseACL defaultACL = new ParseACL();
//        defaultACL.setPublicReadAccess(true);
//        defaultACL.setPublicWriteAccess(true);
//        ParseACL.setDefaultACL(defaultACL, true);
//    }
//
//    public static void subscribeWithEmail(String email) {
//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//
//        installation.put("email", email);
//
//        installation.saveInBackground();
//
//        Log.e(TAG, "Subscribed with email: " + email);
//    }
//}
