package com.inspius.yo_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.GridVideoAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.AppEnum;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.listener.AdapterVideoActionListener;
import com.inspius.yo_video.model.DataCategoryJSON;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.AppSession;
import com.inspius.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class PageVideoHomeFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = PageVideoHomeFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    private int columns = 2;
    int pageNumber;
    DataCategoryJSON dataCategory;
    AppEnum.HOME_TYPE type;

    public static Bundle arguments(AppEnum.HOME_TYPE type) {
        return new Bundler().putSerializable(AppConstant.KEY_BUNDLE_TYPE, type).get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.type = (AppEnum.HOME_TYPE) getArguments().getSerializable(AppConstant.KEY_BUNDLE_TYPE);
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

        mAdapter = new GridVideoAdapter(new ArrayList<VideoModel>());
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (type) {
                    case LATEST:
                        AppSession.getInstance().getListVideoLatest().clear();
                        break;
                    case MOST_VIEW:
                        AppSession.getInstance().getListVideoMostView().clear();
                        break;
                }

                pageNumber = 1;
                requestGetData();
            }
        });
        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetData();
            }
        });

        ultimateRecyclerView.setAdapter(mAdapter);

        startAnimLoading();

        dataCategory = AppSession.getInstance().getCategoryData();
    }

    @Override
    public void onResume() {
        super.onResume();

        pageNumber = 1;
        requestGetData();
    }

    void requestGetData() {
        // check page number
        if (pageNumber < 1)
            pageNumber = 1;

        RPC.requestGetVideoAtHome(type, pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();
                if (ultimateRecyclerView == null)
                    return;
                ultimateRecyclerView.setRefreshing(false);
                // parse data
                List<VideoJSON> listData = (List<VideoJSON>) results;

                // check end data
                if (listData == null || listData.isEmpty())
                    return;

                // check first get data
                if (pageNumber == 1)
                    mAdapter.clear();

                // update data to view
                pageNumber++;
                updateData(listData);
            }
        });
    }

    void updateData(List<VideoJSON> listData) {
        List<VideoModel> listVideo = new ArrayList<VideoModel>();
        for (VideoJSON model : listData) {
            VideoModel vModel = new VideoModel(model);
            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, model.categoryId));
            listVideo.add(vModel);
        }
        mAdapter.insert(listVideo);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(model, true), true);
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
    public void onItemClickListener(int position, Object model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        switch (type) {
            case LATEST:
                AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_VIDEO_LATEST);
                break;
            case MOST_VIEW:
                AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_VIDEO_MOST_VIEW);
                break;
        }
    }
}