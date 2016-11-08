package com.inspius.yo_video.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.inspius.yo_video.R;
import com.inspius.yo_video.activity.MainActivity;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.AppRestClient;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.app.AppEnum;
import com.inspius.yo_video.base.BaseFragment;
import com.inspius.yo_video.listener.AccountDataListener;
import com.inspius.yo_video.model.CustomerModel;
import com.inspius.yo_video.model.DataCategoryJSON;
import com.inspius.yo_video.model.VideoJSON;
import com.inspius.yo_video.service.AppSession;

import java.util.List;

import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashFragment extends BaseFragment {
    public static final String TAG = SplashFragment.class.getSimpleName();
    boolean isDestroy = false;

    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Bind(R.id.circle_loading_view)
    AnimatedCircleLoadingView animatedCircleLoadingView;

    @Bind(R.id.imvLogo)
    ImageView imvLogo;

    @Bind(R.id.tvnVersion)
    TextView tvnVersion;

    int sizeLoad = 4;
    int currentLoad = 0;
    int currentPercent = 0;
    int duration = 30;

    @Override
    public int getLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onResume() {
        super.onResume();

        isDestroy = false;
    }

    @Override
    public void onInitView() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

            tvnVersion.setText(String.format(getString(R.string.text_fm_version), pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (getActivity() == null || isDestroy)
                        return;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imvLogo.setVisibility(View.GONE);
                            getVideoMostView();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changePercent(final int percent) {
        if (getActivity() == null || isDestroy)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    // sizeLoad = 1
    void getVideoMostView() {
        if (getActivity() == null || isDestroy)
            return;

        currentLoad = 0;
        startLoading();

        RPC.requestGetVideoAtHome(AppEnum.HOME_TYPE.MOST_VIEW, 1, new APIResponseListener() {
            @Override
            public void onError(String message) {
                getVideoLatest();
            }

            @Override
            public void onSuccess(Object results) {
                // parse data
                List<VideoJSON> listData = (List<VideoJSON>) results;
                AppSession.getInstance().setListVideoMostView(listData);

                startPercent(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        getVideoLatest();
                    }
                });
            }
        });
    }

    // sizeLoad = 2
    void getVideoLatest() {
        if (getActivity() == null || isDestroy)
            return;


        RPC.requestGetVideoAtHome(AppEnum.HOME_TYPE.LATEST, 1, new APIResponseListener() {
            @Override
            public void onError(String message) {
                getCategories();
            }

            @Override
            public void onSuccess(Object results) {
                // parse data
                List<VideoJSON> listData = (List<VideoJSON>) results;
                AppSession.getInstance().setListVideoLatest(listData);

                startPercent(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        getCategories();
                    }
                });
            }
        });
    }

    // sizeLoad = 3
    void getCategories() {
        if (getActivity() == null || isDestroy)
            return;

        RPC.requestGetCategories(new APIResponseListener() {
            @Override
            public void onError(String message) {
                requestAutoAuthentic();
            }

            @Override
            public void onSuccess(Object results) {
                DataCategoryJSON data = (DataCategoryJSON) results;
                AppSession.getInstance().setCategoryData(data);

                startPercent(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        requestAutoAuthentic();
                    }
                });
            }
        });
    }

    // sizeLoad = 4
    void requestAutoAuthentic() {
        mAccountDataManager.callAutoLoginRequest(getActivity(), new AccountDataListener() {
            @Override
            public void onError(String message) {
                parseAutoAuthentic();
            }

            @Override
            public void onSuccess(CustomerModel results) {
                parseAutoAuthentic();
            }
        });
    }

    void parseAutoAuthentic() {
        startPercent(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                onLoadDataFinish();
            }
        });
    }

    void startPercent(final Handler messageHandler) {
        if (getActivity() == null || isDestroy)
            return;

        currentLoad++;

        final int totalPercent = 100 * currentLoad / sizeLoad;
        if (currentPercent < totalPercent) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = currentPercent; i <= totalPercent; i++) {
                            Thread.sleep(duration);
                            if (getActivity() == null || isDestroy)
                                return;

                            changePercent(i);
                            if (i == totalPercent) {
                                currentPercent = totalPercent;
                                messageHandler.sendEmptyMessage(200);
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void onLoadDataFinish() {
        if (getActivity() == null || isDestroy)
            return;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_CATEGORIES);
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_VIDEO_MOST_VIEW);
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_VIDEO_LATEST);
        AppRestClient.cancelRequestsByTAG(AppConstant.RELATIVE_URL_LOGIN);

        isDestroy = true;
    }
}