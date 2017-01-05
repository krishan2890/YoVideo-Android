package com.inspius.yo_video.modules.news;

import android.view.View;

import com.inspius.yo_video.R;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.listener.AdapterActionListener;
import com.inspius.yo_video.model.NewsCategoryJSON;
import com.inspius.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.grid.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 11/3/16.
 */

public class MNewsCategoriesPageFragment extends BaseMainFragment implements AdapterActionListener {
    public static final String TAG = MNewsCategoriesPageFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private BasicGridLayoutManager mGridLayoutManager;
    private int columns = 3;
    private ListNewsCategoriesAdapter mAdapter = null;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_news_categories_slide_page;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        // init RecyclerView
        initRecyclerView();
        // load data
        requestGetData();
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_news));
        mActivityInterface.setVisibleHeaderMenu(true);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    void initRecyclerView() {
        ultimateRecyclerView.setHasFixedSize(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_news_categories);
        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));
        mAdapter = new ListNewsCategoriesAdapter();
        mAdapter.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapter);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);

        ultimateRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        NewsCategoryJSON categoryJSON = (NewsCategoryJSON) model;
        mHostActivityInterface.addFragment(MNewsListByCategoryFragment.newInstance(categoryJSON), true);
    }

    void requestGetData() {
        startAnimLoading();
        RPC.getNewsCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();

                List<NewsCategoryJSON> data = (List<NewsCategoryJSON>) results;
                if (data == null || data.isEmpty())
                    return;

                mAdapter.add(data);
            }
        });
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);

        if (ultimateRecyclerView != null)
            ultimateRecyclerView.setRefreshing(false);
    }
}
