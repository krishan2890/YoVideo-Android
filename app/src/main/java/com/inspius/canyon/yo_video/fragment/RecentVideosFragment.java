package com.inspius.canyon.yo_video.fragment;

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
import com.inspius.canyon.yo_video.service.RecentListManager;
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

    public static RecentVideosFragment newInstance() {
        RecentVideosFragment fragment = new RecentVideosFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_recent_video));
        mActivityInterface.setVisibleHeaderMenu(true);

       /* AppSession.getCategoryData(new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                dataCategory = (DataCategoryJSON) results;
                requestGetDataProduct();
            }
        });*/
        requestGetDataProduct();
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

        mAdapter = new GridVideoAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setAdapter(mAdapter);

        startAnimLoading();
    }

    @Override
    public void onPlayVideoListener(int position, VideoModel model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(model, true), true);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mHostActivityInterface.addFragment(VideoDetailFragment.newInstance((VideoModel) model, false), true);
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
        RPC.requestGetVideoRencent(mAccountDataManager.getAccountID(),new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
                if(tvnError!=null) {
                    tvnError.setVisibility(View.VISIBLE);
                    tvnError.setText(message);
                }
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<VideoJSON> data = (List<VideoJSON>) results;
                if (data == null)
                    return;

                updateGridVideo(data);
            }
        });
    }

    void updateGridVideo(List<VideoJSON> data) {
        mAdapter.clear();
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

        RecentListManager.getInstance().cancelRequestRecentVideos();
    }
}
