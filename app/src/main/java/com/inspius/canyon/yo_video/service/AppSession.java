package com.inspius.canyon.yo_video.service;

import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;

import java.util.ArrayList;
import java.util.List;

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
    private List<VideoJSON> listVideoMostView;
    private List<VideoJSON> listVideoLatest;

    public DataCategoryJSON getCategoryData() {
        if (categoryData == null) {
            categoryData = new DataCategoryJSON();
            categoryData.listCategory = new ArrayList<>();
            categoryData.listIdTopCategory = new ArrayList<>();
        }

        return categoryData;
    }

    public void setCategoryData(DataCategoryJSON categoryData) {
        this.categoryData = categoryData;
    }

    public void setListVideoLatest(List<VideoJSON> listVideoLatest) {
        this.listVideoLatest = listVideoLatest;
    }

    public List<VideoJSON> getListVideoLatest() {
        return listVideoLatest;
    }

    public void setListVideoMostView(List<VideoJSON> listVideoMostView) {
        this.listVideoMostView = listVideoMostView;
    }

    public List<VideoJSON> getListVideoMostView() {
        return listVideoMostView;
    }
}
