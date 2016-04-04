package com.inspius.canyon.yo_video.fragment.account;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseLoginFragment;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.listener.AccountDataListener;
import com.inspius.canyon.yo_video.model.CustomerModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/15.
 */
public class RegisterFragment extends BaseLoginFragment {
    @Bind(R.id.edtUsername)
    EditText edtUsername;

    @Bind(R.id.edtEmail)
    EditText edtEmail;

    @Bind(R.id.edtPassword)
    EditText edtPassword;

    @Bind(R.id.edtPassConfirm)
    EditText edtPassConfirm;

    public static final String TAG = RegisterFragment.class.getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SplashScreen.
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }


    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    public void onInitView() {

    }

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle("");
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @OnClick(R.id.buttonSubmitRegister)
    void doSubmit() {
        String username = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String passConfirm = edtPassConfirm.getText().toString();

        if (checkField(username, email, password, passConfirm)) {
            mActivityInterface.showLoading(getString(R.string.msg_register_loading));
            mAccountDataManager.callNewAccount(email, password, passConfirm, new AccountDataListener() {
                @Override
                public void onError(String message) {
                    mActivityInterface.hideLoading();
                    DialogUtil.showMessageBox(getContext(), message);
                }

                @Override
                public void onSuccess(CustomerModel results) {
                    mActivityInterface.hideLoading();

                    Intent intent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            });
        }
    }

    boolean checkField(String username, String email, String pass, String pass_cf) {
        if (TextUtils.isEmpty(username)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_username));
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_email));

            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_false_email));
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_pass));
            return false;
        }

        if (pass.length() < 6 || pass.length() > 20) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_password_error_size));
            return false;
        }

        if (TextUtils.isEmpty(pass_cf)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_passforgot));
            return false;
        }
        if (!pass.equals(pass_cf)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_false_passconfirm));
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivityInterface.hideKeyBoard();
    }
}
