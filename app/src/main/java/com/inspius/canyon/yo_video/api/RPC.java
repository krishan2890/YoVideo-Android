package com.inspius.canyon.yo_video.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.AppEnum;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.ImageObj;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.service.AppSession;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by it.kupi on 5/30/2015.
 */
public class RPC {
    public static final String LOG_TAG = RPC.class.getSimpleName();

    /* ======================================= CUSTOMER =======================================*/

    public static void requestAuthentic(final String username, final String password, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USERNAME, username);
        params.put(AppConstant.KEY_PASSWORD, password);


        AppRestClient.post(AppConstant.RELATIVE_URL_LOGIN, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestLoginFacebook(final String accessToken, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_ACCESST_TOKEN, accessToken);

        AppRestClient.post(AppConstant.RELATIVE_URL_LOGIN_FACE_BOOK, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestForgotPassword(final String email, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_EMAIL, email);

        AppRestClient.post(AppConstant.RELATIVE_URL_FORGOT_PASSWORD, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestRegister(final String username, final String email, final String password, final String passwordVerify, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USERNAME, username);
        params.put(AppConstant.KEY_EMAIL, email);
        params.put(AppConstant.KEY_PASSWORD, password);

        AppRestClient.post(AppConstant.RELATIVE_URL_REGISTER, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestUpdateCustomerInfo(final CustomerModel customerModel, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, String.valueOf(customerModel.id));
        params.put(AppConstant.KEY_EMAIL, customerModel.email);
        params.put(AppConstant.KEY_FIRSTNAME, customerModel.firstName);
        params.put(AppConstant.KEY_LASTNAME, customerModel.lastName);
        params.put(AppConstant.KEY_PHONE_NUMBER, customerModel.phone);
        params.put(AppConstant.KEY_ADDRESS, customerModel.address);
        params.put(AppConstant.KEY_CITY, customerModel.city);
        params.put(AppConstant.KEY_COUNTRY, customerModel.country);
        params.put(AppConstant.KEY_ZIP, customerModel.zip);

        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGEPROFILE, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    /**
     * @param accountID
     * @param imagePath
     * @param listener
     */
    public static void requestUpdateAvatar(int accountID, ImageObj imagePath, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, accountID);
        try {
            String type = imagePath.getMimeType();
            params.put(AppConstant.KEY_AVATAR, imagePath.getFile(), type);
        } catch (FileNotFoundException e) {
        }


        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGE_AVATAR, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        CustomerModel accountInfo = new Gson().fromJson(response.getString("content"), CustomerModel.class);
                        listener.onSuccess(accountInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestChangePass(final int accountID, final String currentPass, final String newPass, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, String.valueOf(accountID));
        params.put(AppConstant.KEY_OLD_PASS, currentPass);
        params.put(AppConstant.KEY_NEW_PASS, newPass);
        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGE_PASS, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        listener.onSuccess(response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
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

    public static void requestGetCategories(final APIResponseListener listener) {
        AppRestClient.get(AppConstant.RELATIVE_URL_CATEGORIES, null, new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        DataCategoryJSON data = new Gson().fromJson(response.getString("content"), DataCategoryJSON.class);
                        listener.onSuccess(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, null, listener);
            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                JSONObject jsonObject = new JSONObject(rawJsonData);
                return jsonObject;
            }
        });
    }

    public static void requestGetVideoAtHome(final AppEnum.HOME_TYPE homeType, final int pageNumber, final APIResponseListener listener) {
        String tag = AppConstant.RELATIVE_URL_VIDEO_LATEST;
        switch (homeType) {
            case LATEST:
                tag = AppConstant.RELATIVE_URL_VIDEO_LATEST;

                if (pageNumber == 1) {
                    List<VideoJSON> listData = AppSession.getInstance().getListVideoLatest();
                    if (listData != null && !listData.isEmpty()) {
                        listener.onSuccess(listData);
                        return;
                    }
                }
                break;
            case MOST_VIEW:
                tag = AppConstant.RELATIVE_URL_VIDEO_MOST_VIEW;

                if (pageNumber == 1) {
                    List<VideoJSON> listData = AppSession.getInstance().getListVideoMostView();
                    if (listData != null && !listData.isEmpty()) {
                        listener.onSuccess(listData);
                        return;
                    }
                }

                break;
        }
        final String url = String.format(tag, pageNumber, AppConstant.LIMIT_PAGE_HOMES);
        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getJSONObject("content").getString("videos"), type);

                        if (pageNumber == 1) {
                            switch (homeType) {
                                case LATEST:
                                    AppSession.getInstance().setListVideoLatest(listData);
                                    break;
                                case MOST_VIEW:
                                    AppSession.getInstance().setListVideoMostView(listData);
                                    break;
                            }
                        }

                        listener.onSuccess(listData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, null, listener);
            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                JSONObject jsonObject = new JSONObject(rawJsonData);
                return jsonObject;
            }
        });
    }

    public static void requestGetVideosByCategory(int categoryId, int pageNumber, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_VIDEO_CATEGORY;
        final String url = String.format(tag, categoryId, pageNumber, AppConstant.LIMIT_PAGE_HOMES);
        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, null, listener);
            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                JSONObject jsonObject = new JSONObject(rawJsonData);
                return jsonObject;
            }
        });
    }

    public static void updateVideoStatic(final int videoID, final String field, final int userID, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_VIDEO_ID, String.valueOf(videoID));
        params.put(AppConstant.KEY_FIELD, field);
        params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));

        AppRestClient.post(AppConstant.RELATIVE_URL_UPDATE_STATIC, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        if (listener != null)
                            listener.onSuccess(response.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestSearchVideoByKeyWord(final String keyWord, final int pageNumber, final int limit, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_KEYWORD, keyWord);
        params.put(AppConstant.KEY_PAGENUMBER, String.valueOf(pageNumber));
        params.put(AppConstant.KEY_LIMIT, String.valueOf(limit));


        AppRestClient.post(AppConstant.RELATIVE_URL_SEARCH_BY_KEYWORD, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);

                    boolean checkData = parseResponseData(response, listener);

                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    public static void requestGetVideoBySeries(final String series, final int pageNumber, final int limit, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_BUNDLE_SERIES, series);
        params.put(AppConstant.KEY_PAGENUMBER, String.valueOf(pageNumber));
        params.put(AppConstant.KEY_LIMIT, String.valueOf(limit));

        AppRestClient.post(AppConstant.RELATIVE_URL_SERIES, params, new BaseJsonHttpResponseHandler<String>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, String strResponse) {

                try {
                    JSONObject response = new JSONObject(strResponse);
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, errorResponse, listener);
            }

            @Override
            protected String parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return rawJsonData;
            }
        });
    }

    /**
     * Videos Recent
     *
     * @param listener
     */

    public static void requestGetVideoRecent(final int userID, int pageNumber, final APIResponseListener listener) {
        final String tag = AppConstant.RELATIVE_URL_RECENT;
        final String url = String.format(tag, userID, pageNumber, AppConstant.LIMIT_PAGE_HOMES);
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, String.valueOf(userID));
        AppRestClient.get(url, params, new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        Type type = new TypeToken<List<VideoJSON>>() {
                        }.getType();
                        List<VideoJSON> listData = new Gson().fromJson(response.getString("content"), type);

                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, null, listener);
            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                JSONObject jsonObject = new JSONObject(rawJsonData);
                return jsonObject;
            }
        });
    }

    /* ======================================= VIDEOS END=======================================*/

    /* ======================================= OTHER =======================================*/

    public static void requestGetVideoById(String currentUrl, final APIResponseListener listener) {
        AppRestClient.get(currentUrl, null, new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                try {
                    boolean checkData = parseResponseData(response, listener);
                    if (checkData) {
                        VideoJSON listData = new Gson().fromJson(response.getString("content"), VideoJSON.class);
                        listener.onSuccess(listData);
                    }
                    Log.d("question", String.valueOf(response));

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error Parse Data");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {
                parseError(statusCode, headers, throwable, rawJsonData, null, listener);
            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                JSONObject jsonObject = new JSONObject(rawJsonData);
                return jsonObject;
            }
        });
    }
    /* ======================================= OTHER END =======================================*/

    /* ======================================= COMMON =======================================*/


    private static void parseError(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, String errorResponse, final APIResponseListener listener) {
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
        if (GlobalApplication.getInstance().isProductionEnvironment())
            return;

        if (response != null) {
            Logger.d(TAG, "Response data:");
            Logger.d(TAG, response);
        }
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


    /* ======================================= COMMON END=======================================*/
}