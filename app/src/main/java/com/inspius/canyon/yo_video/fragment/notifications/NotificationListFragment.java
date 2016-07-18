package com.inspius.canyon.yo_video.fragment.notifications;

import android.os.Bundle;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseMainActivityInterface;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;

/**
 * Created by Billy on 1/14/16.
 */
public class NotificationListFragment extends NotificationListBaseFragment implements AdapterActionListener {
    public static final String TAG = NotificationListFragment.class.getSimpleName();


    public static NotificationListFragment newInstance() {
        NotificationListFragment fragment = new NotificationListFragment();
        return fragment;
    }

    protected BaseMainActivityInterface mActivityInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof BaseMainActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement DrawerActivityInterface");
        } else {
            mActivityInterface = (BaseMainActivityInterface) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.menu_notifications));
        mActivityInterface.setVisibleHeaderMenu(true);
        mActivityInterface.setVisibleHeaderSearch(false);
    }
}
