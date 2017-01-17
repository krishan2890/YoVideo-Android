package com.inspius.yo_video.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.inspius.coreapp.CoreAppActivity;
import com.inspius.yo_video.R;
import com.inspius.yo_video.app.AppConstant;
import com.inspius.yo_video.fragment.CommentFragment;
import com.inspius.yo_video.model.VideoModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends CoreAppActivity {
    private ProgressDialog loadingDialog;
    private VideoModel videoModel;

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        setupActionBar();

        mHostActivityImplement.addFragment(CommentFragment.newInstance(videoModel), true);
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showLoading(String message) {
        if (isFinishing())
            return;

        if (loadingDialog != null && loadingDialog.isShowing())
            return;

        try {
            if (loadingDialog == null) {
                loadingDialog = new ProgressDialog(this);
                loadingDialog.setCancelable(false);
            }
            if (!message.isEmpty())
                loadingDialog.setMessage(message);

            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoading() {
        if (isFinishing())
            return;

        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @Override
    public void handleBackPressInThisActivity() {
        finish();
    }
}
