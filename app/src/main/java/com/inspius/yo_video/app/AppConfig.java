package com.inspius.yo_video.app;

import com.inspius.coreapp.config.CoreAppEnums;

/**
 * Created by Billy on 9/3/15.
 */
public class AppConfig {

    /**
     * Change ENVIRONMENT to PRODUCTION for release app
     */
    public static final CoreAppEnums.Environment ENVIRONMENT = CoreAppEnums.Environment.DEV;

    /**
     * Change SHOW_ADS_INTERSTITIAL
     * true : show ads
     * false : hide ads
     */
    public static final boolean SHOW_ADS_BANNER = true;
    public static final boolean SHOW_ADS_INTERSTITIAL = false;

    /**
     * replace to your server
     */
    public static final String BASE_URL = "http://test.inspius.com/yovideo/index.php/";

    /**
     * Please replace this with a valid API key which is enabled for the
     * YouTube Data API v3 service. Go to the
     * <a href=”https://code.google.com/apis/console/“>Google APIs Console</a> to
     * register a new developer key.
     */
    public static final String DEVELOPER_YOUTUBE_KEY = "AIzaSyCtXSa9YTzjN2tIMWt-aOet-uAlVrmjtAk";

    /**
     * Facebook
     */
    public static final String FACEBOOK_APP_ID = "694268657341835";
    public static final String FACEBOOK_APP_NAMESPACE = "video_template";

    /**
     * App Intro
     */
    public static final boolean IS_SHOW_INTRO_APP = true; // show intro app
    public static final boolean IS_FIRST_OPEN_INTRO_APP = true; // show first time open app


    /**
     * PLUGIN CONFIG
     */

    public static final AppEnum.VIDEO_DETAIL_SCREEN VIDEO_DETAIL_SCREEN= AppEnum.VIDEO_DETAIL_SCREEN.DEFAULT;
}
