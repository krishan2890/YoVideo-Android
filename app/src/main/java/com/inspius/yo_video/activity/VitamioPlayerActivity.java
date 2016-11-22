package com.inspius.yo_video.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.fragment.VideoDetailFragment;
import com.inspius.yo_video.helper.Logger;
import com.inspius.yo_video.model.VideoModel;
import com.inspius.coreapp.CoreAppActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioPlayerActivity extends CoreAppActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnTimedTextListener {
    public static final String TAG = VitamioPlayerActivity.class.getSimpleName();

    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @Bind(R.id.videoView)
    VideoView videoView;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.subtitle_view)
    TextView mSubtitleView;

    private VideoModel videoModel;
    private long mPosition = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamio);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        tvnHeaderTitle.setText(videoModel.getTitle());

        playVideo();
    }

    void playVideo() {
        String path = videoModel.getVideoUrl();

        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "Please edit MediaPlayer Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show();

            return;
        }

        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(this);
//        videoView.setOnTimedTextListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // optional need Vitamio 4.0
        mediaPlayer.setPlaybackSpeed(1.0f);

//                    String subtitlePath = videoModel.getVideoSub();
//        String subtitlePath = Environment.getExternalStorageDirectory() + "/toystory.srt";
//
//        if (!TextUtils.isEmpty(subtitlePath)) {
//            videoView.addTimedTextSource(subtitlePath);
//            videoView.setTimedTextShown(true);
//
//            int textTrackIndex = findTrackIndexFor(
//                    MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, mediaPlayer.getTrackInfo());
//
//            if (textTrackIndex >= 0) {
//                mediaPlayer.selectTrack(textTrackIndex);
//            } else {
//                Logger.d(TAG, "Cannot find text track!");
//            }
//        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTimedText(String text) {
        mSubtitleView.setText(text);
    }

    @Override
    public void onTimedTextUpdate(byte[] pixels, int width, int height) {

    }

//    private String getSubtitleFile(VideoModel video) {
//        String fileName = video.getVideoId() + "";
//        File subtitleFile = getFileStreamPath(fileName);
//        if (subtitleFile.exists()) {
//            Logger.d(TAG, "Subtitle already exists");
//            return subtitleFile.getAbsolutePath();
//        }
//        Logger.d(TAG, "Subtitle does not exists, copy it from res/raw");
//
//        // Copy the file from the res/raw folder to your app folder on the
//        // device
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        try {
//            inputStream = getResources().openRawResource(resId);
//            outputStream = new FileOutputStream(subtitleFile, false);
//            copyFile(inputStream, outputStream);
//            return subtitleFile.getAbsolutePath();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            closeStreams(inputStream, outputStream);
//        }
//        return "";
//    }
//
//    private void copyFile(InputStream inputStream, OutputStream outputStream)
//            throws IOException {
//        final int BUFFER_SIZE = 1024;
//        byte[] buffer = new byte[BUFFER_SIZE];
//        int length = -1;
//        while ((length = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, length);
//        }
//    }
//
//    private void closeStreams(Closeable... closeables) {
//        if (closeables != null) {
//            for (Closeable stream : closeables) {
//                if (stream != null) {
//                    try {
//                        stream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
//        int index = -1;
//        for (int i = 0; i < trackInfo.length; i++) {
//            if (trackInfo[i].getTrackType() == mediaTrackType) {
//                return i;
//            }
//        }
//        return index;
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        } else {
            getSupportActionBar().show();
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPause() {
        mPosition = videoView.getCurrentPosition();
        videoView.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mPosition > 0) {
            videoView.seekTo(mPosition);
            mPosition = 0;
        }
        super.onResume();
        videoView.start();
    }
}
