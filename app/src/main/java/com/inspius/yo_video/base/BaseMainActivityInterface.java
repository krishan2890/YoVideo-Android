package com.inspius.yo_video.base;

import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.listener.AccountDataListener;
import com.inspius.yo_video.model.VideoModel;

/**
 * Created by Billy on 12/9/15.
 */
public interface BaseMainActivityInterface extends ToolbarDefaultInterface {
    void showCroutonAlert(String croutonText);

    void showLoading(final String message);

    void hideLoading();

    void hideKeyBoard();

    void clearBackStackFragment();

    void toggleDrawer();

    void openLogin(AccountDataListener listener);

    void openRegister(AccountDataListener listener);

    void logout(APIResponseListener listener);

    void showInterstitialAds();

    void downloadVideo(VideoModel videoModel);
}
