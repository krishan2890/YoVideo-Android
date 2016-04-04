package com.inspius.canyon.yo_video.base;

import android.os.Bundle;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class BaseLoginFragment extends BaseFragment {
    protected BaseLoginActivityInterface mActivityInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        if (!(getActivity() instanceof BaseLoginActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement DrawerActivityInterface");
        } else {
            mActivityInterface = (BaseLoginActivityInterface) getActivity();
        }
    }
}
