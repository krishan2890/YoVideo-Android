package com.inspius.canyon.yo_video.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.api.VolleySingleton;
import com.inspius.canyon.yo_video.app.AppConfig;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.app.GlobalApplication;
import com.inspius.canyon.yo_video.base.BaseMainActivityInterface;
import com.inspius.canyon.yo_video.fragment.SearchFragment;
import com.inspius.canyon.yo_video.fragment.SlideMenuFragment;
import com.inspius.canyon.yo_video.listener.AccountDataListener;
import com.inspius.canyon.yo_video.service.AccountDataManager;
import com.inspius.canyon.yo_video.service.DownloadIntentService;
import com.inspius.coreapp.CoreAppActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.sromku.simple.fb.SimpleFacebook;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends CoreAppActivity implements BaseMainActivityInterface {
    public static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(AppConfig.CONFIG_ENVIRONMENT)
            .clientId(AppConfig.CONFIG_CLIENT_ID)
            .merchantName(AppConfig.PAYPAL_CONFIG_MERCHANT_NAME)
            .merchantPrivacyPolicyUri(Uri.parse(AppConfig.PAYPAL_CONFIG_PRIVACY_POLICY))
            .merchantUserAgreementUri(Uri.parse(AppConfig.PAYPAL_CONFIG_USER_AGREEMENT));

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Bind(R.id.imvHeaderMenu)
    ImageView imvHeaderMenu;

    @Bind(R.id.imvHeaderSearch)
    ImageView imvHeaderSearch;

    boolean isCanBack;
    private ProgressDialog loadingDialog;

    private SimpleFacebook mSimpleFacebook;
    private AccountDataListener listenerLoginRequest;
    private SlideMenuFragment fragmentSlideMenu;
    private AccountDataManager accountDataManager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        accountDataManager = AccountDataManager.getInstance();
        fragmentSlideMenu = (SlideMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentSlideMenu);
        setupActionBar();

        if (AppConfig.SHOW_ADS) {
            // Create the InterstitialAd and set the adUnitId.
            mInterstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

            // Loading ads
            startLoadInterstitialAds();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    startLoadInterstitialAds();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSimpleFacebook != null)
            mSimpleFacebook.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQUEST_CODE_LOGIN) {
            // Make sure Login the request was successful
            if (resultCode == Activity.RESULT_OK) {
                fragmentSlideMenu.updateStateLogin();
                if (listenerLoginRequest != null)
                    listenerLoginRequest.onSuccess(accountDataManager.getCustomerModel());

                listenerLoginRequest = null;
            }
        } else if (requestCode == AppConstant.REQUEST_CODE_DOWNLOAD) {
            /**
             * Custom Download Video
             */
            switch (resultCode) {
                case DownloadIntentService.INVALID_URL_CODE:
                    handleInvalidURL();
                    break;
                case DownloadIntentService.ERROR_CODE:
                    handleError(data);
                    break;
                case DownloadIntentService.RESULT_CODE:
                    handleDownload(data);
                    break;
            }
            handleDownload(data);
        }
    }

    /**
     * @param data
     */
    private void handleDownload(Intent data) {

    }

    /**
     * @param data
     */
    private void handleError(Intent data) {
        // whatever you want
    }

    /**
     *
     */
    private void handleInvalidURL() {
        // whatever you want
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));

        super.onDestroy();

        ButterKnife.unbind(this);
        VolleySingleton.getInstance().cancelAllRequest();
    }

    /**
     * Sets up the action bar.
     */
    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        if (isCanBack)
            onBackPressed();
        else
            toggleDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @OnClick(R.id.imvHeaderSearch)
    void doSearch() {
        addFragment(SearchFragment.newInstance(), true);
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public void setVisibleHeaderMenu(boolean isMenuVisible) {
        isCanBack = !isMenuVisible;
        if (isMenuVisible) {
            imvHeaderMenu.setImageResource(R.drawable.ic_menu);
            setSlideMenuEnabled(true);
        } else {
            imvHeaderMenu.setImageResource(R.drawable.ic_back);
            setSlideMenuEnabled(false);
        }
    }

    /**
     * Toggles opening/closing the drawer.
     */

    @Override
    public void toggleDrawer() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public void setSlideMenuEnabled(boolean isEnable) {
        if (isEnable)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        else
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Update Header Titler
     *
     * @param name
     */
    public void updateHeaderTitle(String name) {
        if (name == null)
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name.toUpperCase(Locale.getDefault()));
    }


    @Override
    public void showCroutonAlert(final String croutonText) {
        Style croutonStyle = Style.ALERT;
        showCroutonAlert(croutonStyle, croutonText, R.id.toolbar);
    }

    private void showCroutonAlert(Style croutonStyle, final String croutonText, int idAlternateViewGroup) {
        Crouton.cancelAllCroutons();

        final Crouton crouton;

        if (idAlternateViewGroup > 0) {
            crouton = Crouton.makeText(this, croutonText, croutonStyle);
        } else {
            crouton = Crouton.makeText(this, croutonText, croutonStyle, idAlternateViewGroup);
        }
        crouton.show();
    }

    @Override
    public void showLoading(String message) {
        if (isFinishing())
            return;

        if (loadingDialog != null && loadingDialog.isShowing())
            return;

        try {
            if (loadingDialog == null) {
                loadingDialog = new ProgressDialog(this);
                loadingDialog.setCancelable(false);
            }
            if (!message.isEmpty())
                loadingDialog.setMessage(message);

            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideLoading() {
        if (isFinishing())
            return;

        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void clearBackStackFragment() {
        clearBackStack();
    }

    @Override
    public void setVisibleToolbar(boolean visibleToolbar) {
        if (visibleToolbar)
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();
    }

    @Override
    public void setVisibleHeaderSearch(boolean isMenuVisible) {
        if (isMenuVisible)
            imvHeaderSearch.setVisibility(View.VISIBLE);
        else
            imvHeaderSearch.setVisibility(View.GONE);
    }

    @Override
    public void openLogin(AccountDataListener listener) {
        this.listenerLoginRequest = listener;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_LOGIN);
    }

    @Override
    public void openRegister(AccountDataListener listener) {
        this.listenerLoginRequest = listener;

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_OPEN_REGISTER, true);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_LOGIN);
    }

    @Override
    public void logout(final APIResponseListener listener) {
        accountDataManager.callLogout(this, new APIResponseListener() {
            @Override
            public void onError(String message) {
                if (listener != null)
                    listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                fragmentSlideMenu.updateStateLogin();
                if (listener != null)
                    listener.onSuccess(results);
            }
        });
    }

    @Override
    public void showInterstitialAds() {
        if (!AppConfig.SHOW_ADS)
            return;

        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startLoadInterstitialAds();
        }
    }

    private void startLoadInterstitialAds() {
        if (!AppConfig.SHOW_ADS)
            return;

        AdRequest adRequest;

        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            if (GlobalApplication.getInstance().isProductionEnvironment()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
            }

            mInterstitialAd.loadAd(adRequest);
        }
    }
}