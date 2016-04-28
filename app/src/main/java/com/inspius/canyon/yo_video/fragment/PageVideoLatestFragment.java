package com.inspius.canyon.yo_video.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.service.AppSession;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class PageVideoLatestFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = PageVideoLatestFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    private int columns = 2;
    int pageNumber;
    DataCategoryJSON dataCategory;

    @Override
    public void onResume() {
        super.onResume();



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

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                requestGetData();
            }
        });

        // setting load more Recycler View
        ultimateRecyclerView.enableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetData();
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
                pageNumber=1;
                requestGetData();
            }
        });
    }

    void requestGetData() {
        // check page number
        if (pageNumber < 1)
            pageNumber = 1;
        RPC.requestGetVideoLatest(pageNumber, new APIResponseListener() {
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
        mAdapter.add(listVideo);
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
        Logger.d("aaaa", String.valueOf(((VideoModel) model).getVideoId()));
    }
}
