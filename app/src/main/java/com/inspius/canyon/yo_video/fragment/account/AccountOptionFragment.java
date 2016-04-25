package com.inspius.canyon.yo_video.fragment.account;

import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.fragment.SlideMenuFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountOptionFragment extends BaseMainFragment {
    public static final String TAG = AccountOptionFragment.class.getSimpleName();

    @Bind(R.id.tvnVip)
    TextView tvnVip;

    public static AccountOptionFragment newInstance() {
        AccountOptionFragment fragment = new AccountOptionFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_account_option;
    }

    @Override
    public void onInitView() {

        if (mAccountDataManager.isVip())
            tvnVip.setVisibility(View.VISIBLE);
        else
            tvnVip.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.my_account_title));
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @OnClick(R.id.linerChangePass)
    void doChangePass() {
        mHostActivityInterface.addFragment(ChangePassFragment.newInstance(), true);
    }

    @OnClick(R.id.linerEditAccount)
    void doEditAccount() {
        mHostActivityInterface.addFragment(EditAccountFragment.newInstance(), true);
    }

    @OnClick(R.id.linerLogout)
    void doLogout() {
        mActivityInterface.showLoading("");
        mActivityInterface.logout(new APIResponseListener() {
            @Override
            public void onError(String message) {
                mActivityInterface.hideLoading();
                mActivityInterface.showCroutonAlert(message);
            }

            @Override
            public void onSuccess(Object results) {
                mActivityInterface.hideLoading();

                SlideMenuFragment fragmentSlideMenu = (SlideMenuFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentSlideMenu);
                fragmentSlideMenu.switchScreen();
            }
        });
    }

}
