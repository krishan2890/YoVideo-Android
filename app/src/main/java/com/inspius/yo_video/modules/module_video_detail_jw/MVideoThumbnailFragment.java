package com.inspius.yo_video.modules.module_video_detail_jw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.player.DailyMotionPlayerActivity;
import com.inspius.yo_video.player.MusicPlayerActivity;
import com.inspius.yo_video.player.WebViewPlayerActivity;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.yo_video.service.RecentListManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/16.
 */

public class MVideoThumbnailFragment extends Fragment {
    public static MVideoThumbnailFragment newInstance(VideoModel model, boolean isAutoPlay) {
        MVideoThumbnailFragment fragment = new MVideoThumbnailFragment();
        fragment.videoModel = model;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

    @Bind(R.id.imvThumbnail)
    ImageView imvThumbnail;

    @Bind(R.id.tvnTime)
    TextView tvnTime;

    @Bind(R.id.tvnVip)
    TextView tvnVip;

    protected VideoModel videoModel;
    private Context mContext;
    boolean isAutoPlay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_fragment_video_thumbnail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageLoader.getInstance().displayImage(videoModel.getImage(), imvThumbnail);
        tvnTime.setText(videoModel.getTimeRemain());

        if (videoModel.isVipPlayer())
            tvnVip.setVisibility(View.VISIBLE);
        else
            tvnVip.setVisibility(View.GONE);

        if (isAutoPlay)
            doPlayVideo();
    }

    @OnClick(R.id.relativePlay)
    void doPlayVideo() {
        if (videoModel == null)
            return;

        Intent intent = null;
        switch (videoModel.getVideoType()) {
            case YOUTUBE:
                break;

            case VIMEO:
                intent = new Intent(mContext, WebViewPlayerActivity.class);
                break;

            case FACEBOOK:
                intent = new Intent(mContext, WebViewPlayerActivity.class);
                break;

            case DAILY_MOTION:
                intent = new Intent(mContext, DailyMotionPlayerActivity.class);
                break;

            case MP3:
                intent = new Intent(mContext, MusicPlayerActivity.class);
                break;

            case JW_PLAYER:
                break;

            default:
                break;
        }

        RecentListManager recentListManager = RecentListManager.getInstance();
        if (recentListManager != null)
            recentListManager.addVideo(videoModel);

        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }
}
