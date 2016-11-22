package com.inspius.yo_video.fragment;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.base.BaseMainActivityInterface;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.model.VideoModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 1/7/16.
 */
public class PageVideoUploadFragment extends Fragment {
    public static final String TAG = PageVideoUploadFragment.class.getSimpleName();


    public static PageVideoUploadFragment newInstance(VideoModel model, boolean isAutoPlay) {
        PageVideoUploadFragment fragment = new PageVideoUploadFragment();
        fragment.videoModel = model;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

    @Bind(R.id.videoView)
    VideoView videoView;

    @Bind(R.id.relativeView)
    RelativeLayout relativeView;

    @Bind(R.id.relativeInfo)
    RelativeLayout relativeInfo;

    @Bind(R.id.imvPlay)
    ImageView imvPlay;

    @Bind(R.id.imvThumbnail)
    ImageView imvThumbnail;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView avloadingIndicatorView;

    VideoModel videoModel;
    private boolean isAutoPlay;

    protected BaseMainActivityInterface mActivityInterface;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof BaseMainActivityInterface)) {
            throw new ClassCastException("Hosting activity must implement DrawerActivityInterface");
        } else {
            mActivityInterface = (BaseMainActivityInterface) getActivity();
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_video_default)
                .showImageForEmptyUri(R.drawable.img_video_default)
                .showImageOnFail(R.drawable.img_video_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_video_upload, container, false);
        ButterKnife.bind(this, view);
        //TypefaceHelper.typeface(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onInitView();
    }

    @Override
    public void onPause() {
        super.onPause();

        mActivityInterface.setVisibleToolbar(true);
    }

    public void onInitView() {
        ImageLoader.getInstance().displayImage(videoModel.getImage(), imvThumbnail, options);


        startAnimLoading();
        imvPlay.setVisibility(View.GONE);
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(getContext());
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(videoModel.getVideoUrl());
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Logger.d("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                relativeView.setLayoutParams(lp2);
                relativeView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, videoView.getHeight());
                        relativeInfo.setLayoutParams(lp);
                    }
                });

                stopAnimLoading();
                if (isAutoPlay)
                    doPlay();
                else
                    imvPlay.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopAnimLoading();
                relativeInfo.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.d(TAG, newConfig.orientation + "");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActivityInterface.setVisibleToolbar(false);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            relativeView.setLayoutParams(lp2);
            relativeView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    relativeInfo.setLayoutParams(lp);
                }
            });
        } else {
            mActivityInterface.setVisibleToolbar(true);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            relativeView.setLayoutParams(lp2);
            relativeView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, videoView.getHeight());
                    relativeInfo.setLayoutParams(lp);
                }
            });
        }
    }

    void startAnimLoading() {
        avloadingIndicatorView.setVisibility(View.VISIBLE);
    }

    void stopAnimLoading() {
        avloadingIndicatorView.setVisibility(View.GONE);
    }

    @OnClick(R.id.imvPlay)
    void doPlay() {
        relativeInfo.setVisibility(View.GONE);
        videoView.start();
        imvPlay.setVisibility(View.GONE);
    }

    @OnClick(R.id.relativeInfo)
    void doRelativeInfo() {
        doPlay();
    }
}
