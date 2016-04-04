package com.inspius.canyon.yo_video.base;

import android.os.Bundle;

import com.inspius.canyon.yo_video.R;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class BaseMainFragment extends BaseFragment {
    protected BaseMainActivityInterface mActivityInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (!(getActivity() instanceof BaseMainActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement DrawerActivityInterface");
        } else {
            mActivityInterface = (BaseMainActivityInterface) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.setVisibleHeaderMenu(true);
        mActivityInterface.setVisibleHeaderSearch(true);
        mActivityInterface.updateHeaderTitle(getString(R.string.app_name));
    }

    @Override
    public void onPause() {
        super.onPause();

        mActivityInterface.hideKeyBoard();
    }
}
