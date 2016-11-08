package com.inspius.yo_video.listener;

import com.inspius.yo_video.model.CustomerModel;

/**
 * Created by Billy on 10/7/15.
 */
public interface AccountDataListener {
    public void onError(String message);

    public void onSuccess(CustomerModel results);
}
