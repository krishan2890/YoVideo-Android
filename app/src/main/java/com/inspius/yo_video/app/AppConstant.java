package com.inspius.yo_video.app;

/**
 * Created by Billy on 12/1/15.
 */
public class AppConstant {
    public static int RESPONSE_CODE_SUCCESS = 1;
    public static int REQUEST_ALBUM_PIC = 1;
    public static int LIMIT_PAGE_HOMES = 10;
    public static int LIMIT_GET_COMMENTS = 10;
    public static int API_TIMEOUT = 20000;

    /**
     * Static Page
     */
    public static final String URL_PAGE_SHARE = "http://inspius.com/";
    public static final String URL_PAGE_ABOUT_US = "http://inspius.com/about-us/";
    public static final String URL_PAGE_TERM_CONDITION = "http://inspius.com/services/mobile-app-development/";
    public static final String URL_PAGE_BUY_NEWS_MODULE = "http://store.inspius.com/downloads/yovideo-android-news-module/";

    /**
     * ID SERVICE
     */
    public static final String RELATIVE_URL_LOGIN = "api/login";
    public static final String RELATIVE_URL_LOGIN_FACE_BOOK = "api/loginFacebook";
    public static final String RELATIVE_URL_REGISTER = "api/register";
    public static final String RELATIVE_URL_FORGOT_PASSWORD = "api/forgot_password";
    public static final String RELATIVE_URL_CHANGE_PASS = "api/change_password";
    public static final String RELATIVE_URL_UPDATE_STATIC = "api/updateStatistics";

    public static final String RELATIVE_URL_RECENT = "api/getListRecentVideo/%s/%s/%s";
    public static final String RELATIVE_URL_SEARCH_BY_KEYWORD = "api/getListVideoByKeyword";
    public static final String RELATIVE_URL_CHANGEPROFILE = "api/change_profile";
    public static final String RELATIVE_URL_CHANGE_AVATAR = "api/change_avatar";

    public static final String RELATIVE_URL_CATEGORIES = "api/categories";
    public static final String RELATIVE_URL_VIDEO_CATEGORY = "api/getListVideoByCategory/%s/%s/%s";
    public static final String RELATIVE_URL_VIDEO_BY_ID = "api/getVideoById/%s";
    public static final String RELATIVE_URL_VIDEO_MOST_VIEW = "api/getListVideoMostView/%s/%s";
    public static final String RELATIVE_URL_VIDEO_LATEST = "api/getListVideoLasted/%s/%s";
    public static final String RELATIVE_URL_SERIES = "api/getVideosBySeries";
    public static final String RELATIVE_URL_GET_COMMENTS = "api/getListCommentVideo/%s/%s/%s";
    public static final String RELATIVE_URL_INSERT_COMMENT = "api/insertCommentVideo";
    public static final String RELATIVE_URL_LIKE_VIDEO = "api/likevideo";
    public static final String RELATIVE_URL_GET_LIKE_STATUS = "api/getLikeVideoStatus/%s/%s";

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
    public static final String KEY_BUNDLE_TYPE = "type";

    public static final String KEY_NOTIFICATION_TITLE = "notification_title";
    public static final String KEY_NOTIFICATION_MESSAGE = "notification_message";
    public static final String KEY_NOTIFICATION_CONTENT = "notification_content";
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
    public static final String HEADER_CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ICON = "icon";
    public static final String KEY_STATUS = "status";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ACCESST_TOKEN = "access-token";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PHONE_NUMBER = "phonenumber";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_OLD_PASS = "old_password";
    public static final String KEY_NEW_PASS = "new_password";
    public static final String KEY_VIDEO_ID = "video_id";
    public static final String KEY_COMMENT_TEXT = "comment_text";
    public static final String KEY_FIELD = "field";
    public static final String KEY_KEYWORD = "keyword";
    public static final String KEY_LIMIT = "limit";
    public static final String KEY_PAGENUMBER = "page";
    public static final String KEY_BUNDLE_SERIES = "series";
    public static final String KEY_FIRST_OPEN_APP = "first-open-app";

    /**
     * Date, Time format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
}
