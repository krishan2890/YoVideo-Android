package com.inspius.yo_video.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspius.yo_video.app.GlobalApplication;
import com.inspius.yo_video.service.AccountDataManager;
import com.inspius.coreapp.CoreAppFragment;

import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Created by Billy on 12/3/15.
 */
public abstract class BaseFragment extends CoreAppFragment {
    protected Context mContext;
    protected GlobalApplication mApplication;
    protected AccountDataManager mAccountDataManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        mApplication = GlobalApplication.getInstance();
        mAccountDataManager = AccountDataManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createPersistentView(inflater, container, savedInstanceState, getLayout());
        ButterKnife.bind(this, view);
        //TypefaceHelper.typeface(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!hasInitializedRootView) {
            hasInitializedRootView = true;

            onInitView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Crouton.cancelAllCroutons();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public abstract int getLayout();

    public abstract void onInitView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
