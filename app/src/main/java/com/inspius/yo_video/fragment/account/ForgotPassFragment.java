package com.inspius.yo_video.fragment.account;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.inspius.yo_video.R;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.base.BaseLoginFragment;
import com.inspius.yo_video.helper.DialogUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/15.
 */
public class ForgotPassFragment extends BaseLoginFragment {
    @Bind(R.id.edtEmail)
    EditText edtEmail;

    public static final String TAG = ForgotPassFragment.class.getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SplashScreen.
     */
    public static ForgotPassFragment newInstance() {
        ForgotPassFragment fragment = new ForgotPassFragment();
        return fragment;
    }


    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.login_forgot_pass));
    }


    @OnClick(R.id.btnSubmit)
    void doSubmit() {
        String email = edtEmail.getText().toString();

        if (checkField(email)) {
            mActivityInterface.showLoading(getString(R.string.msg_forgot_pass_loading));
            RPC.requestForgotPassword(email, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    mActivityInterface.hideLoading();
                    DialogUtil.showMessageBox(getContext(), message);
                }

                @Override
                public void onSuccess(Object results) {
                    mActivityInterface.hideLoading();
                    DialogUtil.showMessageBox(getContext(), getString(R.string.msg_forgot_pass_check_mail), false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
                }
            });
        }
    }

    boolean checkField(String email) {
        if (TextUtils.isEmpty(email)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_email));

            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_false_email));
            return false;
        }
        return true;
    }
}
