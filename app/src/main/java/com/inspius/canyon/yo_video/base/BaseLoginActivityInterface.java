package com.inspius.canyon.yo_video.base;

/**
 * Created by Billy on 12/9/15.
 */
public interface BaseLoginActivityInterface {
    void updateHeaderTitle(String headerTitle);

    void showCroutonAlert(String croutonText);

    void showLoading(final String message);

    void hideLoading();

    void hideKeyBoard();
}
