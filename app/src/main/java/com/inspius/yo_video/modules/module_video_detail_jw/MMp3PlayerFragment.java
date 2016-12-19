package com.inspius.yo_video.modules.module_video_detail_jw;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.model.VideoModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import co.mobiwise.library.MusicPlayerView;

/**
 * Created by Billy on 12/1/16.
 */

public class MMp3PlayerFragment extends Fragment {
    public static MMp3PlayerFragment newInstance(VideoModel model, boolean isAutoPlay) {
        MMp3PlayerFragment fragment = new MMp3PlayerFragment();
        fragment.videoModel = model;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

    protected VideoModel videoModel;
    boolean isAutoPlay;

    MusicPlayerView mpv;
    private MediaPlayer player;
    int length;
    ImageView imvThumbnail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_fragment_mp3_player, container, false);
        mpv = (MusicPlayerView) view.findViewById(R.id.mpv);
        imvThumbnail = (ImageView) view.findViewById(R.id.imvThumbnail);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (TextUtils.isEmpty(videoModel.getImage()))
            mpv.setCoverDrawable(R.drawable.img_video_default);
        else
            mpv.setCoverURL(videoModel.getImage());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_video_default)
                .showImageForEmptyUri(R.drawable.img_video_default)
                .showImageOnFail(R.drawable.img_video_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoader.getInstance().displayImage(videoModel.getImage(), imvThumbnail, options);

        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player == null) {
                    try {
                        playAudio(videoModel.getVideoUrl());

                        int duration = player.getDuration() / 1000;
                        if (duration <= 0)
                            duration = getDuration();

                        if (duration > 0)
                            mpv.setMax(duration);

                        mpv.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mpv.isRotating() && player != null) {
                        mpv.stop();
                        length = player.getCurrentPosition();
                        player.pause();

                    } else if (!mpv.isRotating() && player != null) {
                        mpv.start();
                        //  player.seekTo(length);
                        player.start();
                    }
                }
            }
        });

        if (isAutoPlay) {
            if (player == null) {
                try {
                    playAudio(videoModel.getVideoUrl());
                    int duration = player.getDuration() / 1000;
                    if (duration <= 0)
                        duration = getDuration();

                    if (duration > 0)
                        mpv.setMax(duration);

                    mpv.start();
                    mpv.toggle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int getDuration() {
        String[] timef = videoModel.getTimeRemain().split(":");
        int temp = 0;
        if (timef.length == 2) {
            int minute = Integer.parseInt(timef[0]);
            int second = Integer.parseInt(timef[1]);
            temp = second + (60 * minute);
        } else if (timef.length == 3) {
            int hour = Integer.parseInt(timef[0]);
            int minute = Integer.parseInt(timef[1]);
            int second = Integer.parseInt(timef[2]);
            temp = second + (60 * minute) + hour * 60 * 60;
        }

        return temp;
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }

    private void killMediaPlayer() {
        if (player != null) {
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }
}
