package com.inspius.yo_video.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.ListVideoAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.listener.AdapterVideoActionListener;
import com.inspius.yo_video.model.CategoryJSON;
import com.inspius.yo_video.model.DataCategoryJSON;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.AppSession;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class ListVideoFragment extends BaseMainFragment implements AdapterVideoActionListener {
    public static final String TAG = ListVideoFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private LinearLayoutManager linearLayoutManager;
    private ListVideoAdapter mAdapter = null;
    private CategoryJSON categoryModel;
    int pageNumber;
    DataCategoryJSON dataCategory;

    public static ListVideoFragment newInstance(CategoryJSON categoryModel) {
        ListVideoFragment fragment = new ListVideoFragment();
        fragment.setArguments(new Bundler().putSerializable(AppConstant.KEY_BUNDLE_CATEGORY, categoryModel).get());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryModel = (CategoryJSON) getArguments().getSerializable(AppConstant.KEY_BUNDLE_CATEGORY);
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(categoryModel.name);
        mActivityInterface.setVisibleHeaderMenu(false);
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
        return R.layout.fragment_list_video;
    }

    @Override
    public void onInitView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_product).color(Color.TRANSPARENT).build());

        // ultimateRecyclerView.enableLoadmore();
        //  ultimateRecyclerView.setOnLoadMoreListener(this);

        mAdapter = new ListVideoAdapter();
        mAdapter.setAdapterActionListener(this);
        mAdapter.updateCategoryName(categoryModel);
        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

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

        dataCategory = AppSession.getInstance().getCategoryData();

        pageNumber = 1;
        startAnimLoading();
        requestGetDataProduct();
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel videoModel) {
        Intent intent = AppUtils.getIntentVideoDetail(mContext, videoModel, true);
        if (intent == null)
            return;

        startActivity(intent);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        VideoModel videoModel = (VideoModel) model;

        Intent intent = AppUtils.getIntentVideoDetail(mContext, videoModel, false);
        if (intent == null)
            return;

        startActivity(intent);
    }

    void startAnimLoading() {
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

        RPC.requestGetVideosByCategory(categoryModel.id, pageNumber, new APIResponseListener() {
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
                if (data == null || data.isEmpty())
                    return;

                if (pageNumber == 1)
                    mAdapter.clear();

                pageNumber++;
                updateDataProduct(data);
            }
        });
    }

    void updateDataProduct(List<VideoJSON> data) {
        List<VideoModel> listVideo = new ArrayList<>();
        for (VideoJSON productModel : data) {
            VideoModel vModel = new VideoModel(productModel);
            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, productModel.categoryId));
            listVideo.add(vModel);
        }

        mAdapter.add(listVideo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_VIDEO_CATEGORY);
    }
}
