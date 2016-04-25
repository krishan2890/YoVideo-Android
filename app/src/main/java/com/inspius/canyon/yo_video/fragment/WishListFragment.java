package com.inspius.canyon.yo_video.fragment;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.adapter.WishLishAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.greendao.NewWishList;
import com.inspius.canyon.yo_video.helper.AppUtils;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.listener.WishLishAdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.service.AppSession;
import com.inspius.canyon.yo_video.service.DatabaseManager;
import com.inspius.canyon.yo_video.service.WishListManager;
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
public class WishListFragment extends BaseMainFragment implements WishLishAdapterVideoActionListener {
    public static final String TAG = WishListFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    BasicGridLayoutManager mGridLayoutManager;
    WishLishAdapter mAdapter = null;
    private int columns = 2;
    DataCategoryJSON dataCategory;

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
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapter = new WishLishAdapter();
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
    public void onItemClickListener(int position, Object model) {
        NewWishList wishlishModel = (NewWishList) model;
        requestGetVideoDetail(wishlishModel.getVideoId());
    }

    private void requestGetVideoDetail(int id) {
        if (id <= 0)
            return;

        final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, "Loading");

        RPC.requestGetVideoById(id, new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.hideLoading(loadingDialog);
            }

            @Override
            public void onSuccess(Object results) {
                DialogUtil.hideLoading(loadingDialog);

                VideoModel videoModel = new VideoModel((VideoJSON) results);
                mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(videoModel,false), true);
            }
        });
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
//        WishListManager.getInstance().getWishListDetail(new APIResponseListener() {
//            @Override
//            public void onError(String message) {
//                stopAnimLoading();
//                tvnError.setVisibility(View.VISIBLE);
//                tvnError.setText(message);
//            }
//
//            @Override
//            public void onSuccess(Object results) {
//                stopAnimLoading();
//
//                List<VideoJSON> data = (List<VideoJSON>) results;
//                if (data == null)
//                    return;
//
//                updateGridVideo(data);
//            }
//        });
        startAnimLoading();

        List<NewWishList> data = DatabaseManager.getInstance(mContext).listVideoAtWishList();
        stopAnimLoading();

        if (data == null || data.isEmpty() || mAdapter == null)
            return;
        mAdapter.clear();
        mAdapter.add(data);
    }

//    void updateGridVideo(List<VideoJSON> data) {
//        mAdapter.clear();
//        List<VideoModel> listVideo = new ArrayList<>();
//        for (VideoJSON productModel : data) {
//            VideoModel vModel = new VideoModel(productModel);
//            vModel.setCategoryName(AppUtils.getCategoryName(dataCategory, productModel.categoryId));
//            listVideo.add(vModel);
//        }
//
//        mAdapter.add(listVideo);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        WishListManager.getInstance().cancelRequestWishList();
    }

    @Override
    public void onPlayVideoListener(int position, NewWishList model) {

        NewWishList wishList = (NewWishList) model;
        if (wishList.getVideoId() <= 0)
            return;

        final ProgressDialog loadingDialog = DialogUtil.showLoading(mContext, "Loading");

        RPC.requestGetVideoById(wishList.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.hideLoading(loadingDialog);
            }

            @Override
            public void onSuccess(Object results) {
                DialogUtil.hideLoading(loadingDialog);

                VideoModel videoModel = new VideoModel((VideoJSON) results);
                mHostActivityInterface.addFragment(VideoDetailFragment.newInstance(videoModel,true), true);
            }
        });
    }
}
