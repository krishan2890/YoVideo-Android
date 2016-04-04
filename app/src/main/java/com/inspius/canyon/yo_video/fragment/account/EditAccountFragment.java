package com.inspius.canyon.yo_video.fragment.account;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.inspius.canyon.yo_video.R;
import com.inspius.canyon.yo_video.base.BaseMainFragment;
import com.inspius.canyon.yo_video.helper.DialogUtil;
import com.inspius.canyon.yo_video.listener.AccountDataListener;
import com.inspius.canyon.yo_video.model.CustomerModel;
import com.sithagi.countrycodepicker.CountryPicker;
import com.sithagi.countrycodepicker.CountryPickerListener;

import butterknife.Bind;
import butterknife.OnClick;

public class EditAccountFragment extends BaseMainFragment {
    public static final String TAG = EditAccountFragment.class.getSimpleName();

    public static EditAccountFragment newInstance() {
        EditAccountFragment fragment = new EditAccountFragment();
        return fragment;
    }

    @Bind(R.id.edtFirstName)
    EditText edtFirstName;

    @Bind(R.id.edtLastName)
    EditText edtLastName;

    @Bind(R.id.edtEmail)
    EditText edtEmail;

    @Bind(R.id.edtPhone)
    EditText edtPhone;

    @Bind(R.id.edtAddress)
    EditText edtAddress;

    @Bind(R.id.edtCity)
    EditText edtCity;

    @Bind(R.id.edtCountry)
    TextView edtCountry;

    @Bind(R.id.edtZip)
    EditText edtZip;

    @Override
    public void onResume() {
        super.onResume();

        mActivityInterface.updateHeaderTitle(getString(R.string.edit_account_title));
        mActivityInterface.setVisibleHeaderMenu(false);
        mActivityInterface.setVisibleHeaderSearch(false);
    }

    @Override
    public boolean onBackPressed() {
        return mHostActivityInterface.popBackStack();
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_edit_account;
    }

    @Override
    public void onInitView() {
        CustomerModel customerInfo = mAccountDataManager.getCustomerModel();
        if (customerInfo == null)
            return;

        if (!TextUtils.isEmpty(customerInfo.email))
            edtEmail.setText(customerInfo.email);

        if (!TextUtils.isEmpty(customerInfo.firstName))
            edtFirstName.setText(customerInfo.firstName);

        if (!TextUtils.isEmpty(customerInfo.lastName))
            edtLastName.setText(customerInfo.lastName);


        if (!TextUtils.isEmpty(customerInfo.phone))
            edtPhone.setText(customerInfo.phone);

        if (!TextUtils.isEmpty(customerInfo.address))
            edtAddress.setText(customerInfo.address);

        if (!TextUtils.isEmpty(customerInfo.city))
            edtCity.setText(customerInfo.city);

        if (!TextUtils.isEmpty(customerInfo.country))
            edtCountry.setText(customerInfo.country);

        if (!TextUtils.isEmpty(customerInfo.zip))
            edtZip.setText(customerInfo.zip);
    }

    @OnClick(R.id.btnSubmit)
    void doSubmit() {
        CustomerModel customerUpdate = new CustomerModel();
        customerUpdate.firstName = edtFirstName.getText().toString();
        customerUpdate.lastName = edtLastName.getText().toString();
        customerUpdate.email = edtEmail.getText().toString();
        customerUpdate.phone = edtPhone.getText().toString();
        customerUpdate.city = edtCity.getText().toString();
        customerUpdate.country = edtCountry.getText().toString();
        customerUpdate.zip = edtZip.getText().toString();
        customerUpdate.address = edtAddress.getText().toString();

        if (!checkField(customerUpdate))
            return;

        mActivityInterface.showLoading(getString(R.string.msg_updating_loading));
        mAccountDataManager.callUpdateCustomer(customerUpdate, new AccountDataListener() {
            @Override
            public void onError(String message) {
                mActivityInterface.hideLoading();
                DialogUtil.showMessageBox(getContext(), message);
            }

            @Override
            public void onSuccess(CustomerModel results) {
                mActivityInterface.hideLoading();

                DialogUtil.showMessageBox(getContext(), getString(R.string.msg_update_acc_success), false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
            }
        });
    }

    boolean checkField(CustomerModel customerInfo) {
        if (TextUtils.isEmpty(customerInfo.firstName)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_first_name));
            return false;
        }

        if (TextUtils.isEmpty(customerInfo.lastName)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_last_name));
            return false;
        }

        if (TextUtils.isEmpty(customerInfo.email)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_email));

            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(customerInfo.email).matches()) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_false_email));

            return false;
        }

        if (TextUtils.isEmpty(customerInfo.phone)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_phone));
            return false;
        }
        if (TextUtils.isEmpty(customerInfo.address)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_address));
            return false;
        }

        if (TextUtils.isEmpty(customerInfo.city)) {
            mActivityInterface.showCroutonAlert(getString(R.string.msg_null_city));
            return false;
        }

        return true;
    }

    @OnClick(R.id.edtCountry)
    void doCountry() {
        final CountryPicker picker = CountryPicker.newInstance("SelectCountry");
        picker.setListener(new CountryPickerListener() {

            @Override
            public void onSelectCountry(String name, String code, String dialCode) {
                edtCountry.setText(name);
                picker.dismiss();
            }
        });

        picker.show(getChildFragmentManager(), "COUNTRY_CODE_PICKER");
    }
}
