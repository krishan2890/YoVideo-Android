package com.inspius.yo_video.modules.news;

import android.content.Context;

import com.inspius.yo_video.R;
import com.inspius.yo_video.modules.news.MNewsCategoriesPageFragment;
import com.inspius.yo_video.modules.news.MNewsLatestPageFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

/**
 * Created by Billy on 11/3/16.
 */

public enum MTabNewsSetting {
    CUSTOM_NEWS_TAB() {
        @Override
        public int[] tabs() {
            return new int[]{
                    R.string.news_latest,
                    R.string.news_categories
            };
        }

        @Override
        public FragmentPagerItem getFragmentPagerItem(Context context, int titleResId) {
            FragmentPagerItem fragmentPagerItem = null;

            switch (titleResId) {
                case R.string.news_latest:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), MNewsLatestPageFragment.class);
                    break;
                case R.string.news_categories:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), MNewsCategoriesPageFragment.class);
                    break;

                default:
                    fragmentPagerItem = FragmentPagerItem.of(context.getString(titleResId), MNewsLatestPageFragment.class);
                    break;
            }

            return fragmentPagerItem;
        }

        @Override
        public void setup(SmartTabLayout layout) {
            super.setup(layout);
        }
    };

    public void setup(final SmartTabLayout layout) {
        //Do nothing.
    }

    public int[] tabs() {
        return new int[]{};
    }

    public FragmentPagerItem getFragmentPagerItem(Context context, int titleResId) {
        return null;
    }
}
