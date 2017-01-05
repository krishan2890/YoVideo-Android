package com.inspius.yo_video.modules.module_video_detail_jw;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.inspius.yo_video.R;

/**
 * Created by Billy on 12/1/16.
 */

public class MExoDetailActivity extends MBaseVideoDetailActivity {
    @Override
    void initPlayer() {
        MExoPlayerFragment playerFragment = MExoPlayerFragment.newInstance(videoModel, isAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout(newConfig.orientation);
    }

    private void doLayout(int orientation) {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) frameContainer.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            linearContent.setVisibility(View.GONE);

            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.weight = 1;

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getSupportActionBar().show();
            linearContent.setVisibility(View.VISIBLE);
            playerParams.height = (int) getResources().getDimension(R.dimen.player_video_height);
            playerParams.weight = 0;

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}
