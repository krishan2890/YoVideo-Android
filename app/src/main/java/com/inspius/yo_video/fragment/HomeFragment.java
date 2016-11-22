package com.inspius.yo_video.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.inspius.yo_video.R;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.AppEnum;
import com.inspius.yo_video.base.BaseMainFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class HomeFragment extends BaseMainFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private FragmentPagerItemAdapter mAdapter;


    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInitView() {
        initTabLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityInterface.updateHeaderTitle(getString(R.string.app_name));
        mActivityInterface.setVisibleHeaderMenu(true);
    }

    public void initTabLayout() {
        FragmentPagerItems pages = new FragmentPagerItems(getContext());
        if (getActivity() != null && isAdded()) {
            pages.add(FragmentPagerItem.of(getString(R.string.menu_latest), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(AppEnum.HOME_TYPE.LATEST)));
            pages.add(FragmentPagerItem.of(getString(R.string.menu_most_view), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(AppEnum.HOME_TYPE.MOST_VIEW)));

            mAdapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(), pages);
            viewPager.setAdapter(mAdapter);
            viewPagerTab.setViewPager(viewPager);
        }
    }
}
