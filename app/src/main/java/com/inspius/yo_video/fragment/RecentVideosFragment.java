package com.inspius.yo_video.fragment;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.RecentListAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.greendao.DBRecentVideo;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.listener.RecentListAdapterVideoActionListener;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.AppSession;
import com.inspius.yo_video.service.RecentListManager;
import com.inspius.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class RecentVideosFragment extends BaseMainFragment implements RecentListAdapterVideoActionListener {
    public static final String TAG = RecentVideosFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    BasicGridLayoutManager mGridLayoutManager;
    RecentListAdapter mAdapter = null;
    private int columns = 2;
    String currentUrlGetVideoDetail = "";

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
        } else {
            requestGetDataProduct();
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
        return R.layout.fragment_grid_video;
    }

    @Override
    public void onInitView() {
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new RecentListAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setAdapter(mAdapter);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecentListManager.getInstance().loadData();
                requestGetDataProduct();
            }
        });
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBRecentVideo video = (DBRecentVideo) model;
        requestGetVideo(video, false);
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

        startAnimLoading();
        mAdapter.clear();
        List<DBRecentVideo> data = RecentListManager.getInstance().getRecentVideo();
        stopAnimLoading();

        if (data == null || data.isEmpty() || mAdapter == null)
            return;

        mAdapter.add(data);
    }

    @Override
    public void onPlayVideoListener(int position, final DBRecentVideo wishList) {
        if (wishList == null || wishList.getVideoId() <= 0)
            return;

        requestGetVideo(wishList, true);
    }

    void requestGetVideo(final DBRecentVideo video, final boolean isAutoPlay) {
        if (video == null || video.getId() <= 0)
            return;

        currentUrlGetVideoDetail = String.format(AppConstant.RELATIVE_URL_VIDEO_BY_ID, video.getVideoId());

        final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, getString(R.string.msg_loading_common));
        RPC.requestGetVideoById(currentUrlGetVideoDetail, new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.hideLoading(loadingDialog);
            }

            @Override
            public void onSuccess(Object results) {
                DialogUtil.hideLoading(loadingDialog);

                VideoModel videoModel = new VideoModel((VideoJSON) results);
                videoModel.setCategoryName(video.getCategory());
                mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(videoModel, isAutoPlay), true);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppRestClient.cancelRequestsByTAG(currentUrlGetVideoDetail);
    }
}
