package com.inspius.canyon.yo_video.fragment.notifications;

import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseFragment;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.greendao.DBNotification;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.service.AppNotificationManager;

import butterknife.Bind;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationDetailFragment extends BaseMainFragment {
    public static final String TAG = NotificationDetailFragment.class.getSimpleName();
    public DBNotification notificationJSON;

    public static NotificationDetailFragment newInstance(DBNotification notificationJSON) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        fragment.notificationJSON = notificationJSON;
        return fragment;
    }

    @Bind(R.id.tvnContent)
    TextView tvnContent;

    @Override
    public int getLayout() {
        return R.layout.fragment_notification_detail;
    }

    @Override
    public void onInitView() {
        tvnContent.setText(notificationJSON.getContent());
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_notification_detail));
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }
}
