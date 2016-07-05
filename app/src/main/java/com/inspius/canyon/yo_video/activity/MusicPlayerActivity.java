package com.inspius.canyon.yo_video.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.model.VideoModel;

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
                        mpv.setMax(player.getDuration()/1000);
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
                mpv.setMax(player.getDuration()/1000);
                mpv.start();
                mpv.toggle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
