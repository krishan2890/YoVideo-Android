package com.inspius.canyon.yo_video.app;

/**
 * Created by Billy on 12/1/15.
 */
public class AppConstant {
    public static int RESPONSE_OK = 1;
    public static int RESULT_OK = -1;
    public static int PICK_IMAGE_REQUEST = 1;
    public static int RESPONSE_CODE_SUCCESS = 1;
    public static int REQUEST_ALBUM_PIC = 1;

    /**
     * Static Page
     */
    public static final String URL_PAGE_SHARE = "http://inspius.com/";
    public static final String URL_PAGE_ABOUT_US = "http://inspius.com/about-us/";
    public static final String URL_PAGE_TERM_CONDITION = "http://inspius.com/services/mobile-app-development/";

    /**
     * ID SERVICE
     */
    public static final String RELATIVE_URL_CUSTOMER = "api/login";
    public static final String RELATIVE_URL_LOGIN = "api/login";
    public static final String RELATIVE_URL_REGISTER = "api/register";
    public static final String RELATIVE_URL_FORGOT_PASSWORD = "api/forgot_password";
    public static final String RELATIVE_URL_CHANGEPASS = "api/change_password";
    public static final String RELATIVE_URL_UPDATE_STATIC = "api/updateStatistics";
    public static final String RELATIVE_URL_ADD_WISHLISH = "api/addToWishlist";
    public static final String RELATIVE_URL_WISHLISH = "api/getWishlist/%s";
    public static final String RELATIVE_URL_RENCENT = "api/getListRecentVideo/%s";
    public static final String RELATIVE_URL_SEARCH_BY_KEYWORD = "api/getListVideoByKeyword";
    public static final String RELATIVE_URL_CHANGEPROFILE = "api/change_profile";
    public static final String RELATIVE_URL_CHANGEAVATAR = "api/change_avatar";
    public static final String RELATIVE_URL_DATA_HOME = "api/getListVideoForHomepage";
  //  public static final String RELATIVE_URL_DATA_HOME = "home.json";
    //public static final String RELATIVE_URL_CATEGORIES = "categories.json";
    public static final String RELATIVE_URL_CATEGORIES = "api/categories";
    public static final String RELATIVE_URL_LIST_VIDEOS = "list-video.json";
    public static final String RELATIVE_URL_LIST_VIDEOS_VIMEO = "list-video-vimeo.json";
    public static final String RELATIVE_URL_MORE_VIDEOS = "more-videos.json";
    public static final String RELATIVE_URL_LIST_NOTIFICATIONS = "list-notifications.json";
    public static final String RELATIVE_URL_VIDEO_CATEGORY = "api/getListVideoByCategory/%s";

    /**
     * Key Bundle
     */
    public static final String KEY_BUNDLE_LIST_PRODUCT = "list_product";
    public static final String KEY_BUNDLE_AUTO_PLAY = "auto_play";
    public static final String KEY_BUNDLE_VIDEO = "video";
    public static final String KEY_BUNDLE_CATEGORY = "category";
    public static final String KEY_BUNDLE_URL_PAGE = "url-page";
    public static final String KEY_BUNDLE_TITLE = "title";
    public static final String KEY_BUNDLE_OPEN_REGISTER = "open_register";
    public static final String KEY_BUNDLE_VIDEO_YOUTUBE_ID = "KEY_VIDEO_ID";
    public static final String KEY_BUNDLE_NOTIFICATION_CONTENT = "content";
    /**
     * SharedPreferences
     */
    public static final String KEY_SHARED_PREF_LOGIN_USERNAME = "sp-login-username";
    public static final String KEY_SHARED_PREF_LOGIN_PASSWORD = "sp-login-password";
    public static final String KEY_SHARED_PREF_LOGIN_FACEBOOK = "sp-login-facebook";

    /**
     * Request Code
     */
    public static final int REQUEST_CODE_LOGIN = 111;
    public static final int REQUEST_CODE_DOWNLOAD = 112;
    public static final int REQUEST_CODE_PROFILE_SHARING = 113;


    /**
     * Key XML
     */
    public static final String KEY_ITEM = "element"; // parent node
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ICON = "icon";
    public static final String KEY_STATUS = "status";
}
