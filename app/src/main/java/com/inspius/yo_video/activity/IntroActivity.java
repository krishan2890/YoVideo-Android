package com.inspius.yo_video.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.inspius.yo_video.R;
import com.inspius.yo_video.fragment.IntroSlideFragment;

/**
 * Created by Billy on 11/15/16.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        addSlide(IntroSlideFragment.newInstance(R.layout.screen_app_intro_1));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_app_intro_2));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_app_intro_3));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_app_intro_4));

        setColorSkipButton(Color.parseColor("#222222"));
        setColorDoneText(Color.parseColor("#222222"));
        setNextArrowColor(Color.parseColor("#222222"));
        selectedIndicatorColor = Color.parseColor("#77B5EF");
        unselectedIndicatorColor = Color.parseColor("#EEEEEE");
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        loadMainActivity();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        pager.setCurrentItem(slidesNumber - 1);
    }

    public void getStarted(View v) {
        loadMainActivity();
    }

    void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
