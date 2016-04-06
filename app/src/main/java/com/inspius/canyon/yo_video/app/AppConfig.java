package com.inspius.canyon.yo_video.app;

import com.inspius.coreapp.config.CoreAppEnums;
import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by Billy on 9/3/15.
 */
public class AppConfig {

    /**
     * Change ENVIRONMENT to PRODUCTION for release app
     */
    public static final CoreAppEnums.Environment ENVIRONMENT = CoreAppEnums.Environment.DEV;

    /**
     * Change SHOW_ADS
     * true : show ads
     * false : hide ads
     */
    public static final boolean SHOW_ADS = true;

    /**
     * replace to your server
     */
    //public static final String BASE_URL = "http://demo.inspius.com/mobile/life-video/xml/";
    public static final String BASE_URL = "http://test.inspius.com/yovideo/";
    public static final String BASE_URL_AUTHEN = "http://test.inspius.com/yovideo/";

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
     * Parse
     */
    public static final String PARSE_APPLICATION_ID = "0QLX1Otjgf0Tbqie0Ur7P3w2ZPlRkybuPXVPeb3v";
    public static final String PARSE_CLIENT_KEY = "ZzJDCXI7ducISZgs8QBwJUiIJsCr8LfJb1OsdRVS";
    public static final int NOTIFICATION_ID = 100;

    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    public static final String CONFIG_CLIENT_ID = "IDAWbHJUt_vSw3b47DQFj5gCXb73Pkxgq_lfG-5L1yzFwFkyMrWA_kd3uOm08pqxs5PfOWBn_RGxOZJwcs";
    public static final String PAYPAL_CONFIG_MERCHANT_NAME = "Example Store";
    public static final String PAYPAL_CONFIG_PRIVACY_POLICY = "https://www.example.com/privacy";
    public static final String PAYPAL_CONFIG_USER_AGREEMENT = "https://www.example.com/legal";
}
