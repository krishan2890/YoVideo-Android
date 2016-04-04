package com.inspius.canyon.yo_video.api;

/**
 * Created by it.kupi on 5/30/2015.
 */
public interface APIResponseListener {
    public void onError(String message);
    public void onSuccess(Object results);
}
