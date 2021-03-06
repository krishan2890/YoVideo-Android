package com.inspius.yo_video.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.AppEnum;
import com.inspius.yo_video.helper.ImageUtil;
import com.inspius.yo_video.helper.SharedPrefUtils;
import com.inspius.yo_video.listener.AccountDataListener;
import com.inspius.yo_video.model.CustomerModel;
import com.inspius.yo_video.model.ImageObj;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import java.util.Random;


/**
 * Created by Billy on 11/9/15.
 */
public class AccountDataManager {
    private static AccountDataManager mInstance;
    private AppEnum.LOGIN_TYPE stateLogin = AppEnum.LOGIN_TYPE.NOT_LOGIN;
    private CustomerModel customerModel;
    private Random rd = new Random();

    public static synchronized AccountDataManager getInstance() {
        if (mInstance == null)
            mInstance = new AccountDataManager();

        return mInstance;
    }

    private void updateLoginSystem(String username, String password) {
        stateLogin = AppEnum.LOGIN_TYPE.SYSTEM;
        SharedPrefUtils.saveToPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_USERNAME, username);
        updatePassword(password);
    }

    private void updateLoginFacebook(String accessToken) {
        stateLogin = AppEnum.LOGIN_TYPE.FACEBOOK;

        SharedPrefUtils.saveToPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_FACEBOOK, accessToken);
    }

    private void updatePassword(String password) {
        SharedPrefUtils.saveToPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_PASSWORD, password);
    }

    public void callLogout(Activity activity, APIResponseListener listener) {
        switch (stateLogin) {
            case NOT_LOGIN:
                listener.onSuccess(true);
                RecentListManager.removeInstance();
                break;

            case SYSTEM:
                stateLogin = AppEnum.LOGIN_TYPE.NOT_LOGIN;

                SharedPrefUtils.removeFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_USERNAME);
                SharedPrefUtils.removeFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_PASSWORD);
                listener.onSuccess(true);

                RecentListManager.removeInstance();
                break;

            case FACEBOOK:
                logoutFacebook(activity, listener);
                break;
        }
    }

    public String getUsername() {
        return SharedPrefUtils.getFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_USERNAME, "");
    }

    public String getPassword() {
        return SharedPrefUtils.getFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_PASSWORD, "");
    }

    public String getFacebookAccessToken() {
        return SharedPrefUtils.getFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_FACEBOOK, "");
    }

    public boolean isLogin() {
        if (stateLogin == AppEnum.LOGIN_TYPE.NOT_LOGIN)
            return false;

        return true;
    }

    public boolean isVip() {
        if (!isLogin())
            return false;
        return (customerModel.vip == 1) ? true : false;
    }

    public void setIsVipAccount(boolean isVipAccount) {
        int vip = (isVipAccount) ? 1 : 0;
        customerModel.vip = vip;
    }

    public AppEnum.LOGIN_TYPE getLoginCacheType() {
        String email = getUsername();
        String password = getPassword();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
            return AppEnum.LOGIN_TYPE.SYSTEM;

        if (!TextUtils.isEmpty(getFacebookAccessToken()))
            return AppEnum.LOGIN_TYPE.FACEBOOK;

        return AppEnum.LOGIN_TYPE.NOT_LOGIN;
    }

    public boolean callAutoLoginRequest(Activity activity, final AccountDataListener listener) {
        AppEnum.LOGIN_TYPE type = getLoginCacheType();

        switch (type) {
            case NOT_LOGIN:
                if (listener != null)
                    listener.onError("Not Exit Account");
                break;

            case SYSTEM:
                String email = getUsername();
                String password = getPassword();
                callLoginRequest(email, password, listener);
                break;

            case FACEBOOK:
                String accessToken = getFacebookAccessToken();
                requestLoginWithFacebook(activity, accessToken, listener);
                break;
        }

        return true;
    }

    public void callLoginRequest(final String username, final String password, final AccountDataListener listener) {
        RPC.requestAuthentic(username, password, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stateLogin = AppEnum.LOGIN_TYPE.NOT_LOGIN;

                if (listener != null)
                    listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                parseLoginSystemSuccess(username, password, results, listener);
            }
        });
    }

    public void requestLoginWithFacebook(Activity activity, final String accessToken, final AccountDataListener listener) {
        RPC.requestLoginFacebook(accessToken, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stateLogin = AppEnum.LOGIN_TYPE.NOT_LOGIN;

                if (listener != null)
                    listener.onError(message);
            }

            @Override
            public void onSuccess(final Object results) {
                parseLoginFacebookSuccess(accessToken, results, listener);
            }
        });

    }

    void logoutFacebook(Activity activity, final APIResponseListener listener) {
        final OnLogoutListener onLogoutListener = new OnLogoutListener() {
            @Override
            public void onLogout() {
                stateLogin = AppEnum.LOGIN_TYPE.NOT_LOGIN;
                SharedPrefUtils.removeFromPrefs(AppConstant.KEY_SHARED_PREF_LOGIN_FACEBOOK);
                listener.onSuccess(true);
                RecentListManager.removeInstance();
            }
        };

        SimpleFacebook.getInstance(activity).logout(onLogoutListener);
    }

    public void callNewAccount(final String username, final String email, final String password, String passwordConfirmation, final AccountDataListener listener) {
        RPC.requestRegister(username, email, password, passwordConfirmation, new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                parseLoginSystemSuccess(email, password, results, listener);
            }
        });
    }

    public void callUpdateCustomer(CustomerModel customerUpdate, final AccountDataListener listener) {
        customerUpdate.id = getAccountID();

        RPC.requestUpdateCustomerInfo(customerUpdate, new APIResponseListener() {
            @Override
            public void onError(String message) {

                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                customerModel = (CustomerModel) (results);
                listener.onSuccess(customerModel);
            }
        });
    }

    public void callUpdateAvatar(final Context context, final Intent data, final APIResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageObj imageObj = ImageUtil.getOutputMediaFile(context, data.getData());
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putSerializable("imageObj", imageObj);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }
            }

            private final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    ImageObj imageObj = (ImageObj) msg.getData().getSerializable("imageObj");

                    if (imageObj == null)
                        return;

                    RPC.requestUpdateAvatar(getAccountID(), imageObj, new APIResponseListener() {
                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }

                        @Override
                        public void onSuccess(Object results) {
                            if (results != null)
                                customerModel = (CustomerModel) (results);

                            listener.onSuccess(customerModel);
                        }
                    });
                }
            };
        }).start();
    }

    private void parseLoginSystemSuccess(String email, String password, Object results, AccountDataListener listener) {
        updateLoginSystem(email, password);
        customerModel = (CustomerModel) (results);

        if (listener != null)
            listener.onSuccess(customerModel);

        RecentListManager.newInstance(customerModel.id);
    }

    private void parseLoginFacebookSuccess(String accessToken, Object results, AccountDataListener listener) {
        updateLoginFacebook(accessToken);
        customerModel = (CustomerModel) (results);

        if (listener != null)
            listener.onSuccess(customerModel);

        RecentListManager.newInstance(customerModel.id);
    }

    public CustomerModel getCustomerModel() {
        if (!isLogin())
            return null;

        return customerModel;
    }

    public int getAccountID() {
        if (!isLogin() || customerModel == null)
            return -1;

        return customerModel.id;
    }

}
