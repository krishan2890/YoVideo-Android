package com.inspius.canyon.yo_video.api;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.inspius.canyon.yo_video.app.GlobalApplication;

/**
 * Created by Billy on 10/23/15.
 */
public class VolleySingleton {
    public static final String TAG = VolleySingleton.class.getSimpleName();

    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(GlobalApplication.getAppContext());
    }

    public static VolleySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        if (TextUtils.isEmpty(tag))
            tag = TAG;

        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelByTag(String tag) {
        getRequestQueue().cancelAll(tag);
    }

    public void cancelAllRequest() {
        getRequestQueue().cancelAll(GlobalApplication.getAppContext());
    }
}
