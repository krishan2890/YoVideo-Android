package com.inspius.yo_video.fragment.notifications;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.adapter.ListNotificationAdapter;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.greendao.DBNotification;
import com.inspius.yo_video.listener.AdapterActionListener;
import com.inspius.yo_video.listener.NotificationListener;
import com.inspius.yo_video.model.NotificationJSON;
import com.inspius.yo_video.service.AppNotificationManager;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationListFragment extends BaseMainFragment implements AdapterActionListener, NotificationListener {
    public static final String TAG = NotificationListFragment.class.getSimpleName();

    public static NotificationListFragment newInstance() {
        NotificationListFragment fragment = new NotificationListFragment();
        return fragment;
    }

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    @Bind(R.id.tvnError)
    TextView tvnError;

    private LinearLayoutManager linearLayoutManager;
    private ListNotificationAdapter mAdapter = null;
    int page;

    @Override
    public int getLayout() {
        return R.layout.fragment_list_notification;
    }

    @Override
    public void onInitView() {
        ultimateRecyclerView.setHasFixedSize(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ultimateRecyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_product).color(Color.TRANSPARENT).build());

        mAdapter = new ListNotificationAdapter();
        mAdapter.setAdapterActionListener(this);

        linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(mAdapter);

        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        // setting load more Recycler View
        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                loadMoreData();
            }
        });

        AppNotificationManager.getInstance().subscribeNotification(this);

        reloadData();
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_notifications));
        mActivityInterface.setVisibleHeaderMenu(true);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppNotificationManager.getInstance().unSubscribeNotification(this);
    }

    void startAnimLoading() {
        if (tvnError != null)
            tvnError.setVisibility(View.GONE);

        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        if (avloadingIndicatorView != null)
            avloadingIndicatorView.setVisibility(View.GONE);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        DBNotification notification = (DBNotification) model;
        mHostActivityInterface.addFragment(NotificationDetailFragment.newInstance(notification), true);
    }

    @Override
    public void onNotificationInserted(DBNotification notification) {
        if (notification == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reloadData();
            }
        });
    }

    void reloadData() {
        startAnimLoading();
        mAdapter.clear();
        page = 0;
        List<DBNotification> listData = AppNotificationManager.getInstance().getListNotification(page);
        stopAnimLoading();

        if (listData == null || listData.isEmpty())
            return;
        updateListView(listData);
    }

    void loadMoreData() {
        List<DBNotification> listData = AppNotificationManager.getInstance().getListNotification(page);
        ultimateRecyclerView.setRefreshing(false);
        if (listData == null || listData.isEmpty())
            return;

        updateListView(listData);
    }

    @Override
    public void onNotificationSizeChanged(int totalNotView) {

    }

    void updateListView(List<DBNotification> data) {
        if (data == null || data.isEmpty())
            return;
        mAdapter.add(data);
        page++;
    }
}
