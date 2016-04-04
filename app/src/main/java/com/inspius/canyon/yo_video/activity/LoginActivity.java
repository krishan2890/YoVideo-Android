package com.inspius.canyon.yo_video.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.inspius.coreapp.CoreAppActivity;
import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.app.AppConstant;
import com.inspius.canyon.yo_video.base.BaseLoginActivityInterface;
import com.inspius.canyon.yo_video.fragment.account.LoginFragment;
import com.inspius.canyon.yo_video.fragment.account.RegisterFragment;
import com.sromku.simple.fb.SimpleFacebook;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends CoreAppActivity implements BaseLoginActivityInterface {
    @Bind(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    private SimpleFacebook mSimpleFacebook;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstant.KEY_BUNDLE_OPEN_REGISTER)) {
            addFragment(RegisterFragment.newInstance(), true);
        } else
            addFragment(LoginFragment.newInstance(), true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSimpleFacebook != null)
            mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mHostActivityImplement.getSelectedFragment() == null || !mHostActivityImplement.getSelectedFragment().onBackPressed()) {
            finish();

            setResult(RESULT_CANCELED, null);
        }
    }

    @Override
    public void updateHeaderTitle(String name) {
        if (name == null)
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name);
    }

    @Override
    public void showCroutonAlert(String croutonText) {
        Style croutonStyle = Style.ALERT;
        showCroutonAlert(croutonStyle, croutonText, 0);
    }

    private void showCroutonAlert(Style croutonStyle, final String croutonText, int idAlternateViewGroup) {
        Crouton.cancelAllCroutons();

        final Crouton crouton;

        if (idAlternateViewGroup > 0) {
            crouton = Crouton.makeText(this, croutonText, croutonStyle);
        } else {
            crouton = Crouton.makeText(this, croutonText, croutonStyle, idAlternateViewGroup);
        }
        crouton.show();
    }

    @Override
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

    @Override
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

    @Override
    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
