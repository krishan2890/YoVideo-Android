package com.inspius.yo_video.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.helper.Logger;

/**
 * Created by Billy on 11/11/16.
 */

public class ResponseJSON {
    public int code;
    public String message;
    public JsonNode content;

    @JsonRawValue
    public String getContentString() {
        // default raw value: null or "[]"
        return content == null ? null : content.toString();
    }

    public JsonNode getContentNode() {
        return content;
    }

    public void setContent(JsonNode node) {
        this.content = node;
    }

    public boolean isResponseSuccessfully(APIResponseListener listener) {
        Logger.d("Response From Api", toString());

        if (code == AppConstant.RESPONSE_CODE_SUCCESS)
            return true;

        if (listener != null)
            listener.onError(message);

        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("code : " + code + "\n");
        stringBuilder.append("message : " + message + "\n");
        stringBuilder.append("content : " + content + "\n");
        return stringBuilder.toString();
    }
}

