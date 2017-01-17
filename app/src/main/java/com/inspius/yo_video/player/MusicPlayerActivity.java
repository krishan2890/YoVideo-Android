package com.inspius.yo_video.player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.model.VideoModel;

import co.mobiwise.library.MusicPlayerView;

/**
 * Created by Admin on 30/6/2016.
 */
public class MusicPlayerActivity extends Activity {

    private VideoModel videoModel;
    MusicPlayerView mpv;
    TextView textViewSong;
    private MediaPlayer player;
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        textViewSong = (TextView) findViewById(R.id.textViewSong);
        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            finish();

        mpv.setCoverURL(videoModel.getImage());
        if (videoModel.getTitle() != null)
            textViewSong.setText(videoModel.getTitle());

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
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }
}
