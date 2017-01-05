package com.inspius.yo_video.api;


import android.os.Looper;

import com.inspius.yo_video.app.AppConfig;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.helper.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Admin on 30/1/2016.
 */
public class AppRestClient {
    public static final String TAG = AppRestClient.class.getSimpleName();

    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static AsyncHttpClient synClient = new SyncHttpClient();

    public static void initAsyncHttpClient() {
//        asyncClient.setBasicAuth("username", "password/token");
        getClient().setTimeout(AppConstant.API_TIMEOUT);
    }

    public static void cancelAllRequests() {
        asyncClient.cancelAllRequests(true);
        synClient.cancelAllRequests(true);
    }

    public static void cancelRequestsByTAG(String TAG) {
        asyncClient.cancelRequestsByTAG(TAG, true);
    }

    public static void cancelAllRequestsSyncHttpClient() {
        synClient.cancelAllRequests(true);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        get(url, null, responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler).setTag(url);
    }

    public static void download(String url, FileAsyncHttpResponseHandler responseHandler) {
        getClient().get(GlobalApplication.getAppContext(), url, responseHandler).setTag(url);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler).setTag(url);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().put(GlobalApplication.getAppContext(), getAbsoluteUrl(url), paramsToEntity(params, responseHandler), null, responseHandler).setTag(url);
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

    public static String getAbsoluteUrl(String relativeUrl) {
        String url = AppConfig.BASE_URL + relativeUrl;
        Logger.d(TAG, url);
        return url;
    }

    public static void addHeader(String header, String value) {
        asyncClient.addHeader(header, value);
    }

    public static void removeHeader(String header) {
        asyncClient.removeHeader(header);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return synClient;
        return asyncClient;
    }
}
