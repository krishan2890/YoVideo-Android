package com.inspius.yo_video.modules.news;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.inspius.coreapp.widget.HackyViewPager;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.TabSetting;
import com.inspius.yo_video.base.BaseMainFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
public class MNewsSlideFragment extends BaseMainFragment {
    public static final String TAG = MNewsSlideFragment.class.getSimpleName();

    public static MNewsSlideFragment newInstance() {
        MNewsSlideFragment fragment = new MNewsSlideFragment();
        return fragment;
    }

    @Bind(R.id.viewpager)
    HackyViewPager viewpager;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private FragmentPagerItemAdapter adapter;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_slide;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        final FragmentPagerItems pages = new FragmentPagerItems(mContext);
        for (int titleResId : TabSetting.CUSTOM_NEWS_TAB.tabs()) {
            FragmentPagerItem fragmentPagerItem = TabSetting.CUSTOM_NEWS_TAB.getFragmentPagerItem(mContext, titleResId);
            pages.add(fragmentPagerItem);
        }

        adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);

        viewpager.setAdapter(adapter);
        viewpagerTab.setViewPager(viewpager);

        viewpager.setLocked(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_news));
        mActivityInterface.setVisibleHeaderMenu(true);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
