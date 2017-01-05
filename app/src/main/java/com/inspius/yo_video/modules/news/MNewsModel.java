package com.inspius.yo_video.modules.news;

import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.helper.TimeUtils;

/**
 * Created by Billy on 12/31/16.
 */

public class MNewsModel {
    private MNewsJSON newsJSON;
    private String updateAt;
    private String detailPath;

    public MNewsModel(MNewsJSON newsJSON) {
        this.newsJSON = newsJSON;

        updateAt = TimeUtils.getDateTimeFormat(newsJSON.updateAt);
        this.detailPath = String.format(AppRestClient.getAbsoluteUrl(MNewsConstant.RELATIVE_URL_GET_NEWS_DESCRIPTION_PAGE), newsJSON.id);
    }

    public String getUpdateAt() {
        return updateAt;
    }


    public String getThumbnail() {
        return newsJSON.thumbnail;
    }


    public String getTitle() {
        return newsJSON.title;
    }

    public String getDetailPath() {
        return detailPath;
    }

    public String getViewCounter() {
        return String.format("%s views", AppUtils.getStatsFormat(newsJSON.viewCounter));
    }

    public int getID() {
        return newsJSON.id;
    }
}
