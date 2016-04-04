package com.inspius.canyon.yo_video.api;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.DataHomeJSON;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by it.kupi on 5/30/2015.
 */
public class RPC {
    public static final String LOG_TAG = RPC.class.getSimpleName();

    /* ======================================= CUSTOMER =======================================*/

    public static void requestAuthentic(String username, String password, final APIResponseListener listener) {
        requestGetCustomer(listener);
    }

    public static void requestGetCustomer(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_CUSTOMER;
        final String url = getAbsoluteUrl(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(tag, response);

                CustomerModel data = new Gson().fromJson(response, CustomerModel.class);
                listener.onSuccess(data);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    public static void requestForgotPassword(String email, final APIResponseListener listener) {
        requestGetCustomer(listener);
    }

    public static void requestRegister(String username, String email, String password, String passwordVerify, final APIResponseListener listener) {
        requestGetCustomer(listener);
    }

    public static void requestUpdateCustomerInfo(CustomerModel customerModel, final APIResponseListener listener) {
        requestGetCustomer(listener);
    }

    public static void requestChangePass(int accountID, String currentPass, String newPass, String confirmPass, final APIResponseListener listener) {
        requestGetCustomer(listener);
    }

    /* ======================================= CUSTOMER END=======================================*/

    /* ======================================= VIDEOS =======================================*/

    /**
     * Get categories
     *
     * @param isGetDataCache
     * @param listener
     */
    public static void requestGetCategories(boolean isGetDataCache, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_CATEGORIES;
        final String url = getAbsoluteUrl(tag);

        if (isGetDataCache) {
            Cache cache = VolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(getCacheKey(Request.Method.GET, url));
            if (entry != null) {
                try {
                    String data = new String(entry.data, "UTF-8");
                    DataCategoryJSON dataCategoryJSON = new Gson().fromJson(data, DataCategoryJSON.class);
                    listener.onSuccess(dataCategoryJSON);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                requestGetCategories(!isGetDataCache, listener);
        } else {

            StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.d(tag, response);

                    DataCategoryJSON data = new Gson().fromJson(response, DataCategoryJSON.class);
                    listener.onSuccess(data);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            parseError(error, listener);
                        }
                    });

            jsonObjReq.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
        }
    }

    /**
     * Get Videos at Home
     *
     * @param isGetDataCache
     * @param listener
     */
    public static void requestGetVideosHome(boolean isGetDataCache, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_DATA_HOME;
        final String url = getAbsoluteUrl(tag);

        if (isGetDataCache) {
            Cache cache = VolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(getCacheKey(Request.Method.GET, url));
            if (entry != null) {
                try {
                    String data = new String(entry.data, "UTF-8");
                    DataHomeJSON dataHomeModel = new Gson().fromJson(data, DataHomeJSON.class);
                    listener.onSuccess(dataHomeModel);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else
                requestGetVideosHome(!isGetDataCache, listener);
        } else {
            StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.d(tag, response);

                    DataHomeJSON dataHomeModel = new Gson().fromJson(response, DataHomeJSON.class);
                    listener.onSuccess(dataHomeModel);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            parseError(error, listener);
                        }
                    });

            jsonObjReq.setShouldCache(true);
            VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
        }
    }

    /**
     * Videos from category
     *
     * @param categoryId
     * @param pageNumber
     * @param listener
     */
    public static void requestGetVideosByCategory(long categoryId, int pageNumber, final APIResponseListener listener) {
        if (pageNumber > 1) {
            // categoryId == 11 : vimeo
            if (categoryId == 11)
                requestGetVideosVimeo(listener);
            else
                requestGetMoreVideos(listener);

            return;
        }

        // categoryId == 11 : vimeo
        if (categoryId == 11)
            requestGetVideosVimeo(listener);
        else
            requestGetVideos(listener);
    }

    /**
     * Videos WishList
     *
     * @param listId
     * @param listener
     */
    public static void requestGetVideoWishListInfo(final List<Long> listId, final APIResponseListener listener) {
        requestGetVideos(new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                List<VideoJSON> listData = (List<VideoJSON>) results;
                List<VideoJSON> dataResponse = new ArrayList<>();
                for (long id : listId) {
                    for (VideoJSON model : listData) {
                        if (model.id == id)
                            dataResponse.add(model);
                    }
                }

                listener.onSuccess(dataResponse);
            }
        });
    }

    /**
     * Videos Recent
     *
     * @param listId
     * @param listener
     */
    public static void requestGetRecentVideoInfo(final List<Long> listId, final APIResponseListener listener) {
        requestGetVideos(new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                List<VideoJSON> listData = (List<VideoJSON>) results;
                List<VideoJSON> dataResponse = new ArrayList<>();
                for (long id : listId) {
                    for (VideoJSON model : listData) {
                        if (model.id == id)
                            dataResponse.add(model);
                    }
                }

                listener.onSuccess(dataResponse);
            }
        });
    }

    /**
     * Search Video
     *
     * @param pageNumber
     * @param listener
     */
    public static void requestSearchVideo(int pageNumber, final APIResponseListener listener) {
        if (pageNumber > 1) {
            requestGetMoreVideos(listener);

            return;
        }

        requestGetVideos(listener);
    }

    /* ======================================= VIDEOS END=======================================*/

    /* ======================================= OTHER =======================================*/

    /**
     * List Notifications
     *
     * @param listener
     */
    public static void requestGetListNotifications(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_LIST_NOTIFICATIONS;
        final String url = getAbsoluteUrl(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(tag, response);

                Type type = new TypeToken<List<NotificationJSON>>() {
                }.getType();
                List<NotificationJSON> data = null;
                data = new Gson().fromJson(response, type);
                listener.onSuccess(data);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /**
     * List videos
     *
     * @param listener
     */
    private static void requestGetVideos(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_LIST_VIDEOS;
        final String url = getAbsoluteUrl(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(tag, response);

                Type type = new TypeToken<List<VideoJSON>>() {
                }.getType();
                List<VideoJSON> listData = new Gson().fromJson(response, type);
                listener.onSuccess(listData);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    private static void requestGetVideosVimeo(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_LIST_VIDEOS_VIMEO;
        final String url = getAbsoluteUrl(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(tag, response);

                Type type = new TypeToken<List<VideoJSON>>() {
                }.getType();
                List<VideoJSON> listData = new Gson().fromJson(response, type);
                listener.onSuccess(listData);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /**
     * More videos
     *
     * @param listener
     */
    private static void requestGetMoreVideos(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_MORE_VIDEOS;
        final String url = getAbsoluteUrl(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.d(tag, response);

                Type type = new TypeToken<List<VideoJSON>>() {
                }.getType();
                List<VideoJSON> listCategory = null;
                listCategory = new Gson().fromJson(response, type);
                listener.onSuccess(listCategory);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /* ======================================= OTHER END =======================================*/

    /* ======================================= COMMON =======================================*/


    /**
     * Cancel request by tag
     *
     * @param tag
     */
    public static void cancelRequestByTag(String tag) {
        VolleySingleton.getInstance().cancelByTag(tag);
    }

    /**
     * Get Service Url
     *
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return AppConfig.BASE_URL + relativeUrl;
    }


    /**
     * Get error info
     *
     * @param error
     * @param listener
     */
    private static void parseError(VolleyError error, final APIResponseListener listener) {
        String msg = VolleyErrorHelper.getMessage(error, GlobalApplication.getAppContext());
        listener.onError(msg);
    }

    /**
     * support get cache data
     *
     * @param mMethod
     * @param mUrl
     * @return
     */
    private static String getCacheKey(int mMethod, String mUrl) {
        return mMethod + ":" + mUrl;
    }

    /* ======================================= COMMON END=======================================*/
}
