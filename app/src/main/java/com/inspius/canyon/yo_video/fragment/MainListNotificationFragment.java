package com.inspius.canyon.yo_video.fragment;

import android.os.Bundle;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseMainActivityInterface;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;

/**
 * Created by Billy on 1/14/16.
 */
public class MainListNotificationFragment extends BaseNotificationFragment implements AdapterActionListener {
    public static final String TAG = MainListNotificationFragment.class.getSimpleName();


    public static MainListNotificationFragment newInstance() {
        MainListNotificationFragment fragment = new MainListNotificationFragment();
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
        mActivityInterface.setVisibleHeaderSearch(true);
    }
}
