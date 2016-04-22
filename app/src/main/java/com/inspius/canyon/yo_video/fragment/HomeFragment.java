package com.inspius.canyon.yo_video.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.model.DataHomeJSON;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.wang.avi.AVLoadingIndicatorView;

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

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private FragmentPagerItemAdapter mAdapter;
    int pageNumber;

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
        startAnimLoading();

        RPC.requestGetVideosHome(new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();
                DataHomeJSON dataHome = (DataHomeJSON) results;
                initTabLayout(dataHome);
            }
        });
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityInterface.updateHeaderTitle(getString(R.string.app_name));
        mActivityInterface.setVisibleHeaderMenu(true);
    }

    public void initTabLayout(DataHomeJSON dataHome) {
        FragmentPagerItems pages = new FragmentPagerItems(getContext());
        if (getActivity() != null && isAdded()) {
            pages.add(FragmentPagerItem.of(getString(R.string.menu_latest), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(dataHome.listVideoLatest)));
            pages.add(FragmentPagerItem.of(getString(R.string.menu_most_view), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(dataHome.listVideoMostView)));

            mAdapter = new FragmentPagerItemAdapter(
                    getChildFragmentManager(), pages);
            viewPager.setAdapter(mAdapter);
            viewPagerTab.setViewPager(viewPager);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_DATA_HOME);
    }
}
