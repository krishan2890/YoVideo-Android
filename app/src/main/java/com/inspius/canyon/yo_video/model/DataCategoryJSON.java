package com.inspius.canyon.yo_video.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Billy on 1/6/16.
 */
public class DataCategoryJSON {
    @SerializedName("all_category")
    public List<CategoryJSON> listCategory;

    @SerializedName("top_category")
    public List<Long> listIdTopCategory;
}
