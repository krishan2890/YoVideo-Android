package com.inspius.canyon.yo_video.fragment.account;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseLoginFragment;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.listener.AccountDataListener;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/15.
 */
public class LoginFragment extends BaseLoginFragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    @Bind(R.id.editTextUsername)
    EditText editTextUserName;

    @Bind(R.id.editTextPassword)
    EditText editTextPassword;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle("");
    }

    boolean checkLoginField(String username, String pass) {
        if (TextUtils.isEmpty(username)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_username));
            return false;
        }

        if (TextUtils.isEmpty(pass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_pass));

            return false;
        }

        if (pass.length() < 6 || pass.length() > 20) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_password_error_size));
            return false;
        }

        return true;
    }

    @OnClick(R.id.buttonLogin)
    void doLogin() {
        final String username = editTextUserName.getText().toString();
        final String password = editTextPassword.getText().toString();
        if (checkLoginField(username, password)) {
            requestLogin(username, password);
        }
    }

    void requestLogin(String username, String password) {
        mActivityInterface.showLoading(getString(R.string.msg_authentic_loading));
        mAccountDataManager.callLoginRequest(username, password, new AccountDataListener() {
            @Override
            public void onError(String message) {
                mActivityInterface.hideLoading();
                DialogUtil.showErrorMessage(mContext, getString(R.string.msg_cannot_sign_in), message);
            }

            @Override
            public void onSuccess(CustomerModel results) {
                mActivityInterface.hideLoading();

                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    //event click button Create Account
    @OnClick(R.id.buttonCreateAC)
    void doCreateAcc() {
        mHostActivityInterface.addFragment(RegisterFragment.newInstance(), true);
    }

    //event click textView Forgot Password
    @OnClick(R.id.textViewForgotPass)
    void doForgotPass() {
        mHostActivityInterface.addFragment(ForgotPassFragment.newInstance(), true);
    }

    @OnClick(R.id.buttonLoginFacebook)
    void doFacebook() {
        mActivityInterface.hideKeyBoard();
        final OnLoginListener onLoginListener = new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                requestLoginFacebook(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String reason) {

            }
        };

        SimpleFacebook.getInstance(getActivity()).login(onLoginListener);
    }

    void requestLoginFacebook(String accessToken) {
        mActivityInterface.showLoading(getString(R.string.msg_authentic_loading));
        mAccountDataManager.requestLoginWithFacebook(getActivity(), accessToken, new AccountDataListener() {
            @Override
            public void onError(String message) {
                mActivityInterface.hideLoading();
                DialogUtil.showErrorMessage(mContext, getString(R.string.msg_cannot_sign_in), message);
            }

            @Override
            public void onSuccess(CustomerModel results) {
                mActivityInterface.hideLoading();

                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivityInterface.hideKeyBoard();
    }
}
