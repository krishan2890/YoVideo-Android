package com.inspius.yo_video.modules.news;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.model.ResponseJSON;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Billy on 1/5/17.
 */

public class MNewsRPC {
  /* ======================================= START MODULE =======================================*/

    public static void getNews(final int pageNumber, final APIResponseListener listener) {
        String fmUrl = MNewsConstant.RELATIVE_URL_GET_NEWS;
        String url = String.format(fmUrl, pageNumber, MNewsConstant.LIMIT_NEWS);

        AppRestClient.get(url, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        List<MNewsJSON> listData = new ObjectMapper().readValue(response.getContentString(), new TypeReference<List<MNewsJSON>>() {
                        });
                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getNewsByCategoryID(final int catID, final int pageNumber, final APIResponseListener listener) {
        String fmUrl = MNewsConstant.RELATIVE_URL_GET_NEWS_BY_CAT_ID;
        String url = String.format(fmUrl, catID, pageNumber, MNewsConstant.LIMIT_NEWS);

        AppRestClient.get(url, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        List<MNewsJSON> listData = new ObjectMapper().readValue(response.getContentString(), new TypeReference<List<MNewsJSON>>() {
                        });
                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getNewsByID(int id, final APIResponseListener listener) {
        String fmUrl = MNewsConstant.RELATIVE_URL_GET_NEWS_BY_ID;
        String url = String.format(fmUrl, id);

        AppRestClient.get(url, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        MNewsJSON data = new ObjectMapper().readValue(response.getContentString(), MNewsJSON.class);
                        listener.onSuccess(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getNewsCategories(final APIResponseListener listener) {
        AppRestClient.get(MNewsConstant.RELATIVE_URL_GET_NEWS_CATEGORIES, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        List<MNewsCategoryJSON> listData = new ObjectMapper().readValue(response.getContentString(), new TypeReference<List<MNewsCategoryJSON>>() {
                        });
                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void updateNewsViewCounter(int newsID, final APIResponseListener listener) {
        String fmUrl = MNewsConstant.RELATIVE_URL_GET_NEWS_UPDATE_VIEW;
        String url = String.format(fmUrl, newsID);
        AppRestClient.get(url, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        if (listener != null)
                            listener.onSuccess(response.getContentString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }
    /* ======================================= END MODULE =======================================*/

    private static ResponseJSON onResponse(String rawJsonData) throws IOException {
        ResponseJSON responseJSON = new ObjectMapper().readValue(rawJsonData, ResponseJSON.class);

        return responseJSON;
    }

    private static void onError(Throwable throwable, final APIResponseListener listener) {
        callbackError(throwable.getMessage(), listener);
    }

    private static void callbackError(String message, final APIResponseListener listener) {
        if (listener != null)
            listener.onError(message);
    }
}
