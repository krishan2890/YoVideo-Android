package com.inspius.canyon.yo_video.fragment.notifications;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.ListNotificationAdapter;
import com.inspius.canyon.yo_video.base.BaseFragment;
import com.inspius.canyon.yo_video.fragment.VideoDetailFragment;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.NotificationListener;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.service.AppNotificationManager;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationListBaseFragment extends BaseFragment implements AdapterActionListener, NotificationListener {
    public static final String TAG = NotificationListBaseFragment.class.getSimpleName();

    public static NotificationListBaseFragment newInstance() {
        NotificationListBaseFragment fragment = new NotificationListBaseFragment();
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
    private List<DBNotification> listNotification;

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

        AppNotificationManager.getInstance().subscribeNotification(this);
        List<DBNotification> listData = AppNotificationManager.getInstance().getListNotification(0);
        updateListView(listData);
        stopAnimLoading();
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
        mAdapter.changeNotificationStatus(position, 1);
        mHostActivityInterface.addFragment(NotificationDetailFragment.newInstance(listNotification.get(position)), true);
    }

    @Override
    public void onNotificationInserted(DBNotification notification) {
        if (notification == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                listNotification.clear();
                List<DBNotification> listData = AppNotificationManager.getInstance().getListNotification(0);
                updateListView(listData);
            }
        });
    }

    @Override
    public void onNotificationSizeNotViewChanged(int totalNotView) {

    }

    void updateListView(List<DBNotification> data) {
        if (data == null || data.isEmpty())
            return;

        if (listNotification == null)
            listNotification = new ArrayList<>();

        listNotification.addAll(data);
        List<NotificationJSON> listData = new ArrayList<>();
        for (DBNotification model : data) {
            NotificationJSON notificationJSON = new NotificationJSON();
            notificationJSON.id = model.getId();
            notificationJSON.title = model.getTitle();
            notificationJSON.message = model.getMessage();
            notificationJSON.content = model.getContent();
            notificationJSON.status = model.getStatus();

            listData.add(notificationJSON);
        }
        mAdapter.add(listData);
    }
}
