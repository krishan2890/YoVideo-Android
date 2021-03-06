package com.inspius.yo_video.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.WishListAdapter;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.greendao.DBWishListVideo;
import com.inspius.yo_video.helper.AppUtils;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.listener.WishListAdapterVideoActionListener;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.DatabaseManager;
import com.inspius.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class WishListFragment extends BaseMainFragment implements WishListAdapterVideoActionListener {
    public static final String TAG = WishListFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    BasicGridLayoutManager mGridLayoutManager;
    WishListAdapter mAdapter = null;
    private int columns = 2;
    String currentUrl = "";

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_wishlist));
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

        mAdapter = new WishListAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setHasFixedSize(true);
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestGetDataProduct();
            }
        });

        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBWishListVideo wishlish = (DBWishListVideo) model;
        requestGetVideo(wishlish, false);
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
        List<DBWishListVideo> data = DatabaseManager.getInstance().listVideoAtWishList(mAccountDataManager.getAccountID());
        stopAnimLoading();

        if (data == null || data.isEmpty() || mAdapter == null)
            return;

        mAdapter.clear();
        mAdapter.add(data);
    }

    @Override
    public void onPlayVideoListener(int position, final DBWishListVideo wishList) {
        if (wishList == null || wishList.getVideoId() <= 0)
            return;

        requestGetVideo(wishList, true);
    }

    void requestGetVideo(final DBWishListVideo wishList, final boolean isAutoPlay) {
        if (wishList == null || wishList.getId() <= 0)
            return;

        currentUrl = String.format(AppConstant.RELATIVE_URL_VIDEO_BY_ID, wishList.getVideoId());

        final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, getString(R.string.msg_loading_common));
        RPC.requestGetVideoById(currentUrl, new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.hideLoading(loadingDialog);
            }

            @Override
            public void onSuccess(Object results) {
                DialogUtil.hideLoading(loadingDialog);

                VideoModel videoModel = new VideoModel((VideoJSON) results);
                videoModel.setCategoryName(wishList.getCategory());

                Intent intent = AppUtils.getIntentVideoDetail(mContext, videoModel, isAutoPlay);
                if (intent == null)
                    return;

                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppRestClient.cancelRequestsByTAG(currentUrl);
    }
}
