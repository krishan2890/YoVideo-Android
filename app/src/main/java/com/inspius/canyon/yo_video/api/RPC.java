package com.inspius.canyon.yo_video.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.DataHomeJSON;
import com.inspius.canyon.yo_video.model.ImageObj;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by it.kupi on 5/30/2015.
 */
public class RPC {
    public static final String LOG_TAG = RPC.class.getSimpleName();

    /* ======================================= CUSTOMER =======================================*/

    public static void requestAuthentic(final String username, final String password, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_LOGIN;
        final String url = getAbsoluteUrl(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USERNAME, username);
                params.put(AppConstant.KEY_PASSWORD, password);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }
    public static void requestLoginFacebook(final String accessToken, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_LOGIN_FACE_BOOK;
        final String url = getAbsoluteUrl(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_ACCESST_TOKEN, accessToken);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
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

    public static void requestForgotPassword(final String email, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_FORGOT_PASSWORD;
        final String url = getAbsoluteUrlAuthen(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_EMAIL, email);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    public static void requestRegister(final String username, final String email, final String password, final String passwordVerify, final APIResponseListener listener) {
        //requestGetCustomer(listener);
        final String tag = AppConstant.RELATIVE_URL_REGISTER;
        final String url = getAbsoluteUrlAuthen(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USERNAME, username);
                params.put(AppConstant.KEY_EMAIL, email);
                params.put(AppConstant.KEY_PASSWORD, password);
                // params.put("confirmation", passwordVerify);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    public static void requestUpdateCustomerInfo(final CustomerModel customerModel, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_CHANGEPROFILE;
        final String url = getAbsoluteUrlAuthen(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USER_ID, String.valueOf(customerModel.id));
                params.put(AppConstant.KEY_EMAIL, customerModel.email);
                params.put(AppConstant.KEY_FIRSTNAME, customerModel.firstName);
                params.put(AppConstant.KEY_LASTNAME, customerModel.lastName);
                params.put(AppConstant.KEY_PHONE_NUMBER, customerModel.phone);
                params.put(AppConstant.KEY_ADDRESS, customerModel.address);
                params.put(AppConstant.KEY_CITY, customerModel.city);
                params.put(AppConstant.KEY_COUNTRY, customerModel.country);
                params.put(AppConstant.KEY_ZIP, customerModel.zip);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /**
     * @param accountID
     * @param imagePath
     * @param listener
     */
    public static void requestUpdateAvatar(int accountID, ImageObj imagePath, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, accountID);
//        params.put(AppConstant.KEY_AVATAR, new ByteArrayInputStream(imagePath.getImgBytes()),imagePath.getName());
        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGEAVATAR, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {
                try {
                    if (parseResponseData1(strResponse, listener)) {
                        JSONObject response = new JSONObject(strResponse);
                        CustomerModel customerModel = new ObjectMapper().readValues(new JsonFactory().createParser(response.getString("content")), CustomerModel.class).next();
                        listener.onSuccess(customerModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError1(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

//    /**
//     * @param accountID
//     * @param imagePath
//     * @param listener
//     */
//    public static void requestUpdateAvatar(final int accountID, final ImageObj imagePath, final APIResponseListener listener) {
//        final String tag = AppConstant.RELATIVE_URL_CHANGEAVATAR;
//        final String url = getAbsoluteUrlAuthen(tag);
//
//        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String strResponse) {
//                try {
//                    JSONObject response = new JSONObject(strResponse);
//
//                    boolean checkData = parseResponseData(response, listener);
//
//                    if (checkData) {
//                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
//                        listener.onSuccess(accountInfo);
//                    }
//                } catch (JSONException e) {
//                    listener.onError("Error data");
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        parseError(error, listener);
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", String.valueOf(accountID));
//                params.put("avatar", imagePath.getImgStr());
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
//    }


    public static void requestChangePass(final int accountID, final String currentPass, final String newPass, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_CHANGEPASS;
        final String url = getAbsoluteUrlAuthen(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USER_ID, String.valueOf(accountID));
                params.put(AppConstant.KEY_OLD_PASS, currentPass);
                params.put(AppConstant.KEY_NEW_PASS, newPass);
                //  params.put("confirmpass", confirmPass);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /* ======================================= CUSTOMER END=======================================*/

    /* ======================================= VIDEOS =======================================*/

    /**
     * Get categories
     * <p/>
     * //  * @param isGetDataCache
     *
     * @param listener
     */
   /* public static void requestGetCategories(boolean isGetDataCache, final APIResponseListener listener) {
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
    }*/
    public static void requestGetCategories(final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_CATEGORIES;
        final String url = getAbsoluteUrl(tag);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Logger.d(LOG_TAG, response.getString("content"));
                        DataCategoryJSON data = new Gson().fromJson(response.getString("content"), DataCategoryJSON.class);
                        listener.onSuccess(data);
                    }
                    Log.d("question", String.valueOf(response));
                } catch (JSONException e) {
                    listener.onError("Error data");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parseError(error, listener);
            }
        });
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /**
     * Get Videos at Home
     *
     * @param listener
     */
    public static void requestGetVideosHome(final APIResponseListener listener) {
        final String tag = String.format(AppConstant.RELATIVE_URL_DATA_HOME);
        final String url = getAbsoluteUrl(tag);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {

                        DataHomeJSON data = new Gson().fromJson(response.getString("content"), DataHomeJSON.class);
                        listener.onSuccess(data);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                });

        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);

       /* if (isGetDataCache) {
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
        }*/
    }

    public static void requestGetVideosByCategory(int categoryId, final APIResponseListener listener) {
        final String tag = String.format(AppConstant.RELATIVE_URL_VIDEO_CATEGORY, categoryId);
        final String url = getAbsoluteUrl(tag);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
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

    public static void updateVideoStatic(final int videoID, final String field, final int userID, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_UPDATE_STATIC;
        final String url = getAbsoluteUrlAuthen(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_VIDEO_ID, String.valueOf(videoID));
                params.put(AppConstant.KEY_FIELD, field);
                params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));
                //  params.put("confirmpass", confirmPass);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }
//AddVideotoWishlish

    public static void requestGetVideoToWishLish(final int userID, final int videoID, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_ADD_WISHLISH;
        final String url = getAbsoluteUrlAuthen(tag);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }
                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));
                params.put(AppConstant.KEY_VIDEO_ID, String.valueOf(videoID));

                //  params.put("confirmpass", confirmPass);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    //UploadVideo

    /**
     * Videos from category
     *
     * @param categoryId
     * @param pageNumber
     * @param listener
     */
//    public static void requestGetVideosByCategory(long categoryId, int pageNumber, final APIResponseListener listener) {
//        if (pageNumber > 1) {
//            // categoryId == 11 : vimeo
//            if (categoryId == 11)
//                requestGetVideosVimeo(listener);
//            else
//                requestGetMoreVideos(listener);
//
//            return;
//        }
//
//        // categoryId == 11 : vimeo
//        if (categoryId == 11)
//            requestGetVideosVimeo(listener);
//        else
//            requestGetVideos(listener);
//    }

    /**
     * Videos WishList
     *
     * @param listId
     * @param listener
     */
    public static void requestGetVideoWishListInfo(final List<Long> listId, final APIResponseListener listener) {
       /* requestGetVideos(new APIResponseListener() {
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
        });*/
        final String tag = AppConstant.RELATIVE_URL_DATA_HOME;
        final String url = getAbsoluteUrl(tag);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
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

    public static void requestGetVideoInWihsLish(final int userID, final APIResponseListener listener) {
        final String tag = String.format(AppConstant.RELATIVE_URL_WISHLISH, userID);
        final String url = getAbsoluteUrl(tag);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    //SearchByKeyWord
    public static void requestSearchVideoByKeyWord(final String keyWord, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_SEARCH_BY_KEYWORD;
        final String url = getAbsoluteUrl(tag);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_KEYWORD, keyWord);
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

    /**
     * Videos Recent
     *
     * @param listener
     */

    public static void requestGetVideoRencent(final int userID, final APIResponseListener listener) {
        final String tag = String.format(AppConstant.RELATIVE_URL_RENCENT, userID);
        final String url = getAbsoluteUrl(tag);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (JSONException e) {
                    listener.onError("Error data");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parseError(error, listener);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));
                return params;
            }
        };
        VolleySingleton.getInstance().addToRequestQueue(jsonObjReq, tag);
    }

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

    private static void parseError1(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse, final APIResponseListener listener) {
        listener.onError(throwable.getMessage());

        debugThrowable(LOG_TAG, throwable);
        if (errorResponse != null)
            debugResponse(LOG_TAG, rawJsonData);


    }

    protected static final void debugThrowable(String TAG, Throwable t) {
        if (!GlobalApplication.getInstance().isProductionEnvironment())
            return;

        if (t != null) {
            Logger.d(TAG, "AsyncHttpClient returned error");
            Logger.d(TAG, throwableToString(t));
        }
    }

    /**
     * @param t
     * @return
     */
    protected static String throwableToString(Throwable t) {
        if (t == null)
            return null;

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * @param TAG
     * @param response
     */
    protected static final void debugResponse(String TAG, String response) {
        if (!GlobalApplication.getInstance().isProductionEnvironment())
            return;

        if (response != null) {
            Logger.d(TAG, "Response data:");
            Logger.d(TAG, response);
        }
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

    private static boolean parseResponseData1(String response, final APIResponseListener listener) throws JSONException {
        if (response == null) {
            listener.onError("Data Parse Null");
            return false;
        }

        Logger.d(LOG_TAG, response.toString());
        return true;
    }

    private static boolean parseResponseData(JSONObject responseJSON, final APIResponseListener listener) throws JSONException {
        if (responseJSON == null) {
            listener.onError("Data Parse Null");
            return false;
        }

        Logger.d(LOG_TAG, responseJSON.toString());

        int code = responseJSON.getInt("code");
        if (code != AppConstant.RESPONSE_CODE_SUCCESS) {
            String msg = responseJSON.getString("message");
            listener.onError(msg);

            return false;
        }

        return true;
    }

    private static String getAbsoluteUrlAuthen(String relativeUrl) {
        return AppConfig.BASE_URL_AUTHEN + relativeUrl;
    }
    /* ======================================= COMMON END=======================================*/
}
