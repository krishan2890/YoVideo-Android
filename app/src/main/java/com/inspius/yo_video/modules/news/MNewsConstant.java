package com.inspius.yo_video.modules.news;

/**
 * Created by Billy on 1/5/17.
 */

public class MNewsConstant {
    public static int LIMIT_NEWS = 15;

    public static final String RELATIVE_URL_GET_NEWS = "/api/getListNews/%s/%s";
    public static final String RELATIVE_URL_GET_NEWS_BY_CAT_ID = "/api/getNewsByCategoryID/%s/%s/%s";
    public static final String RELATIVE_URL_GET_NEWS_BY_ID = "/api/getNewsByID/%s";
    public static final String RELATIVE_URL_GET_NEWS_CATEGORIES = "/api/getNewsCategories";
    public static final String RELATIVE_URL_GET_NEWS_UPDATE_VIEW = "/api/updateViewNewsCounter/%s";
    public static final String RELATIVE_URL_GET_NEWS_DESCRIPTION_PAGE = "/news/getNewsPage/%s";
}
