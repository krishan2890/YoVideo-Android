package com.inspius.canyon.yo_video.fragment.notifications;

import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseFragment;
import com.inspius.canyon.yo_video.fragment.SplashFragment;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.service.AppNotificationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationDetailAppFragment extends BaseFragment {
    public static final String TAG = NotificationDetailAppFragment.class.getSimpleName();

    public static NotificationDetailAppFragment newInstance(DBNotification dbNotification) {
        NotificationDetailAppFragment fragment = new NotificationDetailAppFragment();
        fragment.dbNotification = dbNotification;
        return fragment;
    }

    @Bind(R.id.tvnContent)
    TextView tvnContent;

    public DBNotification dbNotification;

    @Override
    public int getLayout() {
        return R.layout.fragment_notification_detail_app;
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
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        if (dbNotification == null)
            return;

        tvnContent.setText(dbNotification.getContent());
    }
}
