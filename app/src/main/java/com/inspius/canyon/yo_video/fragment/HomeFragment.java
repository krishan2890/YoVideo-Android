package com.inspius.canyon.yo_video.fragment;

import android.os.Bundle;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class HomeFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    //    @Bind(R.id.viewpagertab)
//    SmartTabLayout viewPagerTab;
//
//    @Bind(R.id.viewpager)
//    ViewPager viewPager;
//
//    @Bind(R.id.avloadingIndicatorView)
//    AVLoadingIndicatorView avloadingIndicatorView;
//
//    private FragmentPagerItemAdapter mAdapter;
//
//    @Override
//    public String getTagText() {
//        return TAG;
//    }
//
//    @Override
//    public int getLayout() {
//        return R.layout.fragment_home;
//    }
//
//    @Override
//    public void onInitView() {
//        startAnimLoading();
//        RPC.requestGetVideosHome(true, new APIResponseListener() {
//            @Override
//            public void onError(String message) {
//                stopAnimLoading();
//            }
//
//            @Override
//            public void onSuccess(Object results) {
//                stopAnimLoading();
//
//                DataHomeJSON dataHome =new DataHomeJSON();
//                dataHome.listVideoLatest= (List<VideoJSON>) results;
//                initTabLayout(dataHome);
//            }
//        });
//    }
//
//    void startAnimLoading() {
//        avloadingIndicatorView.setVisibility(View.VISIBLE);
//    }
//
//    void stopAnimLoading() {
//        avloadingIndicatorView.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mActivityInterface.updateHeaderTitle(getString(R.string.app_name));
//        mActivityInterface.setVisibleHeaderMenu(true);
//    }
//
//    public void initTabLayout(DataHomeJSON dataHome) {
//        FragmentPagerItems pages = new FragmentPagerItems(getContext());
//        pages.add(FragmentPagerItem.of(getString(R.string.menu_latest), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(dataHome.listVideoLatest)));
//        pages.add(FragmentPagerItem.of(getString(R.string.menu_most_view), PageVideoHomeFragment.class, PageVideoHomeFragment.arguments(dataHome.listVideoMostView)));
//
//        mAdapter = new FragmentPagerItemAdapter(
//                getChildFragmentManager(), pages);
//
//        viewPager.setAdapter(mAdapter);
//        viewPagerTab.setViewPager(viewPager);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_DATA_HOME);
//    }
    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    List<VideoModel> listVideo;
    private int columns = 2;

//    public static Bundle arguments(List<VideoJSON> listProduct) {
//        return new Bundler().putSerializable(AppConstant.KEY_BUNDLE_LIST_PRODUCT, (Serializable) listProduct).get();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //   List<VideoJSON> listProduct = (List<VideoJSON>) getArguments().getSerializable(AppConstant.KEY_BUNDLE_LIST_PRODUCT);
//        listVideo = new ArrayList<>();
//        for (VideoJSON productModel : listProduct) {
//            listVideo.add(new VideoModel(productModel));
//        }
    }

    @Override
    public int getLayout() {
        return R.layout.page_video;
    }

    @Override
    public void onInitView() {
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new GridVideoAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);


        RPC.requestGetVideosHome(new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                List<VideoJSON> listProduct = (List<VideoJSON>) results;
                listVideo = new ArrayList<>();
                for (VideoJSON productModel : listProduct) {
                    listVideo.add(new VideoModel(productModel));
                }
                mAdapter.add(listVideo);
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);
        RPC.requestGetCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                DataCategoryJSON data = (DataCategoryJSON) results;
                if (data == null || data.listCategory == null)
                    return;

                mAdapter.updateCategoryName(data.listCategory);
            }
        });
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(model, true), true);
//        Intent intent;
//        if (model.getVideoType() == AppEnum.VIDEO_TYPE.UPLOAD) {
//            intent = new Intent(getActivity(), PlayerUploadActivity.class);
//        } else {
//            intent = new Intent(getActivity(), PlayerYoutubeActivity.class);
//        }
//        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, model);
//
//        startActivity(intent);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }
}
