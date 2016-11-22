package com.inspius.yo_video.fragment.account;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.EditText;

import com.inspius.yo_video.R;
import com.inspius.yo_video.api.APIResponseListener;
import com.inspius.yo_video.api.RPC;
import com.inspius.yo_video.base.BaseMainFragment;
import com.inspius.yo_video.helper.DialogUtil;
import com.inspius.yo_video.model.CustomerModel;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangePassFragment extends BaseMainFragment {
    public static final String TAG = ChangePassFragment.class.getSimpleName();

    public static ChangePassFragment newInstance() {
        ChangePassFragment fragment = new ChangePassFragment();
        return fragment;
    }

    @Bind(R.id.edtCurrentPass)
    EditText edtCurrentPass;

    @Bind(R.id.edtNewPass)
    EditText edtNewPass;

    @Bind(R.id.edtConfirmPass)
    EditText edtConfirmPass;

    private CustomerModel customerModel;
    @Override
    public int getLayout() {
        return R.layout.fragment_change_pass;
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

        mActivityInterface.updateHeaderTitle(getString(R.string.change_password));
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivityInterface.hideKeyBoard();
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @OnClick(R.id.buttonSubmit)
    void doSubmit() {
        String currentPass = edtCurrentPass.getText().toString();
        final String newPass = edtNewPass.getText().toString();
        String confirmPass = edtConfirmPass.getText().toString();
        if (checkField(currentPass, newPass, confirmPass)) {
            mActivityInterface.showLoading(getString(R.string.msg_change_pass_loading));

            RPC.requestChangePass(mAccountDataManager.getCustomerModel().id, currentPass, newPass, new APIResponseListener() {
                @Override
                public void onError(String message) {
                    mActivityInterface.hideLoading();
                    DialogUtil.showMessageBox(getContext(), message);
                }

                @Override
                public void onSuccess(Object results) {
                    mActivityInterface.hideLoading();
                    DialogUtil.showMessageBox(getContext(), getString(R.string.msg_change_pass_success), false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
                }
            });
        }
}

    boolean checkField(String currentPass, String newPass, String confirmPass) {
        if (TextUtils.isEmpty(currentPass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_pass));
            return false;
        }

        if (TextUtils.isEmpty(newPass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_pass));
            return false;
        }

        if (newPass.length() < 6 || newPass.length() > 20) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_password_error_size));
            return false;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_passforgot));
            return false;
        }
        if (!newPass.equals(confirmPass)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_false_passconfirm));
            return false;
        }
        return true;
    }
}
