package com.inspius.canyon.yo_video.fragment;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.service.AppNotificationManager;

import butterknife.OnClick;

/**
 * Created by Billy on 1/14/16.
 */
public class AppListNotificationFragment extends BaseNotificationFragment implements AdapterActionListener {
    public static final String TAG = AppListNotificationFragment.class.getSimpleName();

    public static AppListNotificationFragment newInstance() {
        AppListNotificationFragment fragment = new AppListNotificationFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_list_notification;
    }

    @Override
    public boolean onBackPressed() {
        mHostActivityInterface.addFragment(SplashFragment.newInstance(), true);
        return true;
    }

    @OnClick(R.id.imvHeaderMenu)
    void doBack() {
        onBackPressed();
    }

    @Override
    public void onInitView() {
        super.onInitView();

        startAnimLoading();
        AppNotificationManager.getInstance().requestGetNotification();
    }
}
