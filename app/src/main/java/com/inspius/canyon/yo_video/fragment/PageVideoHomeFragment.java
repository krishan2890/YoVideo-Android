package com.inspius.canyon.yo_video.fragment;

import android.os.Bundle;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridVideoAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.listener.AdapterVideoActionListener;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.model.VideoJSON;
import com.inspius.canyon.yo_video.model.VideoModel;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;

import java.io.Serializable;
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

    BasicGridLayoutManager mGridLayoutManager;
    GridVideoAdapter mAdapter = null;
    List<VideoModel> listVideo;
    private int columns = 2;

    public static Bundle arguments(List<VideoJSON> listProduct) {
        return new Bundler().putSerializable(AppConstant.KEY_BUNDLE_LIST_PRODUCT, (Serializable) listProduct).get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<VideoJSON> listProduct = (List<VideoJSON>) getArguments().getSerializable(AppConstant.KEY_BUNDLE_LIST_PRODUCT);
        listVideo = new ArrayList<>();
        for (VideoJSON productModel : listProduct) {
            listVideo.add(new VideoModel(productModel));
        }
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
        ultimateRecyclerView.setSaveEnabled(true);
        ultimateRecyclerView.setClipToPadding(false);

        ultimateRecyclerView.setAdapter(mAdapter);

        RPC.requestGetCategories( new APIResponseListener() {
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

        mAdapter.insert(listVideo);
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
        Logger.d("aaaa",String.valueOf(((VideoModel) model).getVideoId()));
    }
}
