package com.inspius.canyon.yo_video.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.DownloadListAdapter;
import com.inspius.canyon.yo_video.api.AppRestClient;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.greendao.DBVideoDownload;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.WishListAdapterVideoActionListener;
import com.inspius.canyon.yo_video.service.DatabaseManager;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.inspius.coreapp.helper.IntentUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class DownloadListFragment extends BaseMainFragment implements AdapterActionListener {
    public static final String TAG = DownloadListFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    BasicGridLayoutManager mGridLayoutManager;
    DownloadListAdapter mAdapter = null;
    private int columns = 2;
    String currentUrl = "";
    int currentPage;

    public static DownloadListFragment newInstance() {
        DownloadListFragment fragment = new DownloadListFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_download));
        mActivityInterface.setVisibleHeaderMenu(true);

        requestGetDataProduct(1);
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

        mAdapter = new DownloadListAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestGetDataProduct(1);
            }
        });

        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                requestGetDataProduct(currentPage + 1);
            }
        });

        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBVideoDownload mVideo = (DBVideoDownload) model;
        Intent intent = IntentUtils.openVideo(mVideo.getPath());
        startActivity(intent);
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

    void requestGetDataProduct(int page) {
        if (page <= 1) {
            page = 1;
            mAdapter.clear();
        }
        List<DBVideoDownload> data = DatabaseManager.getInstance().listVideoDownload(page);
        stopAnimLoading();
        ultimateRecyclerView.setRefreshing(false);

        if (data == null || data.isEmpty() || mAdapter == null)
            return;

        currentPage = page;
        mAdapter.add(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppRestClient.cancelRequestsByTAG(currentUrl);
    }
}
