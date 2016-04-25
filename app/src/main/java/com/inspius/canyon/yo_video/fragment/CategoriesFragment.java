package com.inspius.canyon.yo_video.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.GridAllCategoryAdapter;
import com.inspius.canyon.yo_video.adapter.ListTopCategoryAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.RPC;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.model.CategoryJSON;
import com.inspius.canyon.yo_video.model.DataCategoryJSON;
import com.inspius.canyon.yo_video.widget.GridDividerDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.BasicGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Bind;

/**
 * Created by Billy on 12/1/15.
 */
public class CategoriesFragment extends BaseMainFragment implements AdapterActionListener {
    public static final String TAG = CategoriesFragment.class.getSimpleName();

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.urvTopCategories)
    UltimateRecyclerView urvTopCategories;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    private BasicGridLayoutManager mGridLayoutManager;
    private GridAllCategoryAdapter mAdapterAllCategory = null;
    private ListTopCategoryAdapter mAdapterTopCategory = null;
    private int columns = 2;

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_categories));
        mActivityInterface.setVisibleHeaderMenu(true);
    }


    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_categories;
    }

    @Override
    public void onInitView() {
        ultimateRecyclerView.setHasFixedSize(false);
        urvTopCategories.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            urvTopCategories.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // All Category
        boolean includeEdge = true;
        int spacing = getResources().getDimensionPixelSize(R.dimen.divider_grid_video);

        ultimateRecyclerView.addItemDecoration(
                new GridDividerDecoration(columns, spacing, includeEdge));

        mAdapterAllCategory = new GridAllCategoryAdapter();
        mAdapterAllCategory.setAdapterActionListener(this);

        mGridLayoutManager = new BasicGridLayoutManager(getContext(), columns, mAdapterAllCategory);
        ultimateRecyclerView.setLayoutManager(mGridLayoutManager);
        ultimateRecyclerView.setAdapter(mAdapterAllCategory);


        // Top Categories

        //urvTopCategories.addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_product).color(Color.TRANSPARENT).build());
        mAdapterTopCategory = new ListTopCategoryAdapter();
        mAdapterTopCategory.setAdapterActionListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        urvTopCategories.setLayoutManager(linearLayoutManager);
        urvTopCategories.setAdapter(mAdapterTopCategory);

        requestGetData();
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        CategoryJSON categoryJSON = (CategoryJSON) model;
        mHostActivityInterface.addFragment(ListVideoFragment.newInstance(categoryJSON), true);
    }

    void startAnimLoading() {
        if (avloadingIndicatorView != null)
        avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
        avloadingIndicatorView.setVisibility(View.GONE);
    }

    void requestGetData() {
        startAnimLoading();
        RPC.requestGetCategories( new APIResponseListener() {
            @Override
            public void onError(String message) {
                stopAnimLoading();
            }

            @Override
            public void onSuccess(Object results) {
                stopAnimLoading();
                DataCategoryJSON data = (DataCategoryJSON) results;
                if (data == null || data.listCategory == null)
                    return;
                for (CategoryJSON model : data.listCategory) {
                    if (model.enable == 1)
                        mAdapterAllCategory.insert(model);
                }
                if (data.listIdTopCategory != null) {
                    for (long id : data.listIdTopCategory)
                        for (CategoryJSON model : data.listCategory)
                            if (model.id == id) {
                                if(model.enable==1)
                                mAdapterTopCategory.insert(model);
                                break;
                            }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RPC.cancelRequestByTag(AppConstant.RELATIVE_URL_CATEGORIES);
    }
}
