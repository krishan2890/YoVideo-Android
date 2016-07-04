package com.inspius.canyon.yo_video.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.adapter.ListSlideMenuAdapter;
import com.inspius.canyon.yo_video.api.APIResponseListener;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.fragment.account.AccountOptionFragment;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.helper.Logger;
import com.inspius.canyon.yo_video.helper.XMLParser;
import com.inspius.canyon.yo_video.listener.AccountDataListener;
import com.inspius.canyon.yo_video.listener.AdapterActionListener;
import com.inspius.canyon.yo_video.listener.AnimateFirstDisplayListener;
import com.inspius.canyon.yo_video.listener.NotificationListener;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.inspius.canyon.yo_video.model.NotificationJSON;
import com.inspius.canyon.yo_video.model.SlideMenuModel;
import com.inspius.canyon.yo_video.service.AppNotificationManager;
import com.inspius.coreapp.CoreAppFragment;
import com.inspius.coreapp.helper.IntentUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 12/7/15.
 */
public class SlideMenuFragment extends BaseMainFragment implements AdapterActionListener, NotificationListener {
    public static final String TAG = SlideMenuFragment.class.getSimpleName();

    // Slide Menu
    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    @Bind(R.id.linearLogin)
    LinearLayout linearLogin;

    @Bind(R.id.linearNotLogin)
    LinearLayout linearNotLogin;

    @Bind(R.id.imvAvatar)
    ImageView imvAvatar;

    @Bind(R.id.tvnName)
    TextView tvnName;

    @Bind(R.id.tvnEmail)
    TextView tvnEmail;

    @Bind(R.id.tvnNumberNotification)
    TextView tvnNumberNotification;

    private List<SlideMenuModel> slideMenuList;
    private ListSlideMenuAdapter menuAdapter;

    SlideMenuModel slideMenuModelSelected;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.inspius_logo)
                .showImageForEmptyUri(R.drawable.inspius_logo)
                .showImageOnFail(R.drawable.inspius_logo)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        AppNotificationManager.getInstance().subscribeNotification(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppNotificationManager.getInstance().unSubscribeNotification(this);
    }

    @Override
    public int getLayout() {
        return R.layout.screen_sliding_menu;
    }

    @Override
    public void onInitView() {
        tvnNumberNotification.setText("0");

        initMenu();
        updateStateLogin();

        slideMenuModelSelected = slideMenuList.get(0);
        switchScreen();

        requestAutoAuthentic();

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void initMenu() {
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(mContext, "xml/menu.xml"); // getting XML
        Document doc = parser.getDomElement(xml); // getting DOM element
        slideMenuList = new ArrayList<>();
        NodeList nl = doc.getElementsByTagName(AppConstant.KEY_ITEM);
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            Element e = (Element) nl.item(i);

            if (parser.getValue(e, AppConstant.KEY_STATUS).equalsIgnoreCase("1")) {

                SlideMenuModel model = new SlideMenuModel();

                // adding each child node to HashMap key => value
                model.id = parser.getValue(e, AppConstant.KEY_ID);
                model.title = parser.getValue(e, AppConstant.KEY_NAME);
                model.type = parser.getValue(e, AppConstant.KEY_TYPE);
                String strIcon = parser.getValue(e, AppConstant.KEY_ICON);
                model.icon = String.format(getString(R.string.text_fm_icon_assets), strIcon);
                model.isActived = false;

                // adding HashList to ArrayList
                slideMenuList.add(model);
            }
        }

        Logger.d(TAG, String.valueOf(slideMenuList.size()));

        ultimateRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.divider_height_list_menu).colorResId(R.color.divider_color_list_menu).build());

        menuAdapter = new ListSlideMenuAdapter(slideMenuList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(menuAdapter);
    }

    void requestAutoAuthentic() {
        mAccountDataManager.callAutoLoginRequest(getActivity(), new AccountDataListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onSuccess(CustomerModel results) {
                updateStateLogin();
            }
        });
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    public void updateStateLogin() {
        boolean isLogin = mAccountDataManager.isLogin();
        if (isLogin) {
            linearNotLogin.setVisibility(View.GONE);
            linearLogin.setVisibility(View.VISIBLE);

            tvnName.setText(String.format("%s %s", mAccountDataManager.getCustomerModel().firstName, mAccountDataManager.getCustomerModel().lastName));
            tvnEmail.setText(mAccountDataManager.getCustomerModel().email);
            ImageLoader.getInstance().displayImage(mAccountDataManager.getCustomerModel().avatar, imvAvatar, options, animateFirstListener);

            AppNotificationManager.getInstance().requestGetNotification();
        } else {
            linearNotLogin.setVisibility(View.VISIBLE);
            linearLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClickListener(int position, Object model) {
        mActivityInterface.toggleDrawer();

        SlideMenuModel slideMenuModel = (SlideMenuModel) model;
        String title = slideMenuModel.type;

        if (title.equalsIgnoreCase("share")) {
            Intent intent = IntentUtils.shareText(getString(R.string.app_name), AppConstant.URL_PAGE_SHARE);
            startActivity(intent);

            return;
        } else if (title.equalsIgnoreCase("rate")) {
            startActivity(IntentUtils.openPlayStore(mContext));
            return;
        }

        this.slideMenuModelSelected = slideMenuModel;
        switchScreen();
    }

    public void switchScreen() {
        if (slideMenuModelSelected == null || slideMenuModelSelected.isActived)
            return;

        for (SlideMenuModel slideMenuModel : slideMenuList) {
            if (slideMenuModel.title.equalsIgnoreCase(slideMenuModelSelected.title))
                slideMenuModel.isActived = true;
            else
                slideMenuModel.isActived = false;
        }

        menuAdapter.notifyDataSetChanged();

        String type = slideMenuModelSelected.type;
        CoreAppFragment fragment = null;
        if (type.equalsIgnoreCase("home_page")) {
            fragment = HomeFragment.newInstance();
        } else if (type.equalsIgnoreCase("categories")) {
            fragment = CategoriesFragment.newInstance();
        } else if (type.equalsIgnoreCase("wishlist")) {
            fragment = WishListFragment.newInstance();
        } else if (type.equalsIgnoreCase("recent_videos")) {
            fragment = RecentVideosFragment.newInstance();
        } else if (type.equalsIgnoreCase("download")) {
        } else if (type.equalsIgnoreCase("about_us")) {
            fragment = WebViewFragment.newInstance(AppConstant.URL_PAGE_ABOUT_US, slideMenuModelSelected.title);
//            fragment = WebViewFragment.newInstance("https://player.vimeo.com/video/7980975", slideMenuModelSelected.title);
        } else if (type.equalsIgnoreCase("term_condition")) {
            fragment = WebViewFragment.newInstance(AppConstant.URL_PAGE_TERM_CONDITION, slideMenuModelSelected.title);
        }

        if (fragment != null) {
            mActivityInterface.clearBackStackFragment();
            mHostActivityInterface.addFragment(fragment, true);
        }
    }

    @OnClick(R.id.buttonLogin)
    void doLogin() {
        mActivityInterface.openLogin(null);
    }

    @OnClick(R.id.buttonRegister)
    void doRegister() {
        mActivityInterface.openRegister(null);
    }

//    @OnClick(R.id.imvLogout)
//    void doLogout() {
//        mActivityInterface.logout(null);
//    }

    @OnClick(R.id.relativeNotification)
    void doNotification() {
        slideMenuModelSelected.isActived = false;
        for (SlideMenuModel slideMenuModel : slideMenuList) {
            slideMenuModel.isActived = false;
        }
        menuAdapter.notifyDataSetChanged();

        mActivityInterface.toggleDrawer();
        mActivityInterface.clearBackStackFragment();
        mHostActivityInterface.addFragment(MainListNotificationFragment.newInstance(), true);
    }

    @OnClick(R.id.imvSetting)
    void doSetting() {
        slideMenuModelSelected.isActived = false;
        for (SlideMenuModel slideMenuModel : slideMenuList) {
            slideMenuModel.isActived = false;
        }
        menuAdapter.notifyDataSetChanged();

        mActivityInterface.toggleDrawer();
        mActivityInterface.clearBackStackFragment();
        mHostActivityInterface.addFragment(AccountOptionFragment.newInstance(), true);
    }

    @OnClick(R.id.imvAvatar)
    void doChangeAvatar() {
        Intent intent = IntentUtils.pickImage();
        startActivityForResult(intent, AppConstant.REQUEST_ALBUM_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.REQUEST_ALBUM_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                mActivityInterface.showLoading("Updating avatar...");
                mAccountDataManager.callUpdateAvatar(getContext(), data, new APIResponseListener() {
                    @Override
                    public void onError(String message) {
                        mActivityInterface.hideLoading();
                        DialogUtil.showMessageBox(getContext(), message);
                    }

                    @Override
                    public void onSuccess(Object results) {
                        mActivityInterface.hideLoading();
                        ImageLoader.getInstance().displayImage(mAccountDataManager.getCustomerModel().avatar, imvAvatar, options, animateFirstListener);
                    }
                });
            }
        }
    }

    @Override
    public void onNotificationNotReadChanged(int number) {
        tvnNumberNotification.setText(String.valueOf(number));
    }

    @Override
    public void onNotificationChanged(List<NotificationJSON> newData, List<NotificationJSON> listNotification) {

    }
}
