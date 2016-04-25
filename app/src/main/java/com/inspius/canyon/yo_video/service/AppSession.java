package com.inspius.canyon.yo_video.service;

import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;

/**
 * Created by Billy on 1/11/16.
 */
public class AppSession {
    private static AppSession mInstance;

    public static synchronized AppSession getInstance() {
        if (mInstance == null)
            mInstance = new AppSession();

        return mInstance;
    }

    private static DataCategoryJSON categoryData;

    public static void getCategoryData(final APIResponseListener listener) {
        if (categoryData != null)
            listener.onSuccess(categoryData);
        else
            RPC.requestGetCategories(new APIResponseListener() {
                @Override
                public void onError(String message) {
                    listener.onError(message);
                }

                @Override
                public void onSuccess(Object results) {
                    categoryData = (DataCategoryJSON) results;
                    updateDataCategoryJSON(categoryData);
                    listener.onSuccess(categoryData);
                }
            });
    }

    public static void updateDataCategoryJSON(DataCategoryJSON data) {
        categoryData = data;
    }
}
