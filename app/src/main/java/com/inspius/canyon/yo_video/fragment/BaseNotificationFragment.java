package com.inspius.canyon.yo_video.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.ListNotificationAdapter;
import com.inspius.canyon.yo_video.base.BaseFragment;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.NotificationListener;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.service.AppNotificationManager;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Billy on 1/14/16.
 */
public class BaseNotificationFragment extends BaseFragment implements AdapterActionListener, NotificationListener {
    public static final String TAG = BaseNotificationFragment.class.getSimpleName();

    public static BaseNotificationFragment newInstance() {
        BaseNotificationFragment fragment = new BaseNotificationFragment();
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
        mAdapter.add(AppNotificationManager.getInstance().getData());
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
        NotificationJSON notificationJSON = (NotificationJSON) model;
        notificationJSON.status = 1;
        mAdapter.notifyDataSetChanged();

        AppNotificationManager.getInstance().changeStateNotification(position);
    }

    @Override
    public void onNotificationChanged(List<NotificationJSON> newData, List<NotificationJSON> listNotification) {
        mAdapter.clear();
        mAdapter.add(listNotification);
        stopAnimLoading();
    }

    @Override
    public void onNotificationNotReadChanged(int number) {

    }
}
