package com.inspius.canyon.yo_video.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.service.AppSession;
import com.inspius.canyon.yo_video.service.RecentListManager;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class RecentVideosFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = RecentVideosFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    private int columns = 2;
    DataCategoryJSON dataCategory;
    int pageNumber;

    public static RecentVideosFragment newInstance() {
        RecentVideosFragment fragment = new RecentVideosFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_recent_video));
        mActivityInterface.setVisibleHeaderMenu(true);

        if (!mAccountDataManager.isLogin()) {
            stopAnimLoading();
            tvnError.setVisibility(View.VISIBLE);
            tvnError.setText(getString(R.string.msg_request_login));
            return;
        }
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_recent_list_video;
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
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                requestGetDataProduct();
            }
        });
        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetDataProduct();
            }
        });
        ultimateRecyclerView.setAdapter(mAdapter);

        startAnimLoading();
        AppSession.getCategoryData(new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                dataCategory = (DataCategoryJSON) results;
                requestGetDataProduct();
            }
        });
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(model, true), true);
    }

    void startAnimLoading() {
        tvnError.setVisibility(View.GONE);
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    void requestGetDataProduct() {
        if (pageNumber < 1)
            pageNumber = 1;
        RPC.requestGetVideoRencent(mAccountDataManager.getAccountID(), pageNumber, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();

            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                if (ultimateRecyclerView == null)
                    return;
                ultimateRecyclerView.setRefreshing(false);

                List<VideoJSON> data = (List<VideoJSON>) results;
                // check end data
                if (data == null || data.isEmpty())
                    return;

                // check first get data
                if (pageNumber == 1)
                    mAdapter.clear();

                // update data to view
                pageNumber++;

                updateGridVideo(data);
            }
        });
    }

    void updateGridVideo(List<VideoJSON> data) {
        List<VideoModel> listVideo = new ArrayList<>();
        for (VideoJSON productModel : data) {
            VideoModel vModel = new VideoModel(productModel);
            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, productModel.categoryId));
            listVideo.add(vModel);
        }
        mAdapter.insert(listVideo);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RecentListManager.getInstance().cancelRequestRecentVideos();
    }
}
