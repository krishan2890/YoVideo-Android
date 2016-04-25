package com.inspius.canyon.yo_video.api;


import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.helper.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Admin on 30/1/2016.
 */
public class AppRestClient {
    public static final String TAG = AppRestClient.class.getSimpleName();

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_APP_ID = "AppId";
    private static final String HEADER_BASIC = "basic";

    public static void initAsyncHttpClient() {
//        client.setBasicAuth("username","password/token");

    }

    public static void cancelAllRequests() {
        client.cancelAllRequests(true);
    }

    public static void cancelRequestsByTAG(String TAG) {
        Logger.d("cancelRequestsByTAG", TAG);
        client.cancelRequestsByTAG(TAG, true);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(GlobalApplication.getAppContext(), getAbsoluteUrl(url), params, responseHandler).setTag(url);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(GlobalApplication.getAppContext(), getAbsoluteUrl(url), params,responseHandler).setTag(url);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(GlobalApplication.getAppContext(), getAbsoluteUrl(url), paramsToEntity(params, responseHandler), null, responseHandler).setTag(url);
    }

    private static HttpEntity paramsToEntity(RequestParams params, ResponseHandlerInterface responseHandler) {
        HttpEntity entity = null;

        try {
            if (params != null) {
                entity = params.getEntity(responseHandler);
            }
        } catch (IOException e) {
            if (responseHandler != null) {
                responseHandler.sendFailureMessage(0, null, null, e);
            } else {
                e.printStackTrace();
            }
        }

        return entity;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        String url = AppConfig.BASE_URL + relativeUrl;
        Logger.d(TAG, url);
        return url;
    }

}