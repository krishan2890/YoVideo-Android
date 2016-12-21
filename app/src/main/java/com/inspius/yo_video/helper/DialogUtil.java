package com.inspius.yo_video.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import com.inspius.yo_video.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Billy on 11/23/15.
 */
public class DialogUtil {
    public static void showMessageBox(Context context, String message) {
        new SweetAlertDialog(context)
                .setTitleText(message)
                .show();
    }

    public static void showMessageBox(Context context, String message, boolean isBackPress, DialogInterface.OnClickListener listener) {
        if (message == null) {
            message = "";
        }

        if (listener == null) {
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            };
        }

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton(context.getString(R.string.cm_close).toUpperCase(), listener);
        b.setCancelable(isBackPress);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static void showMessageYesNo(Context context, String message, DialogInterface.OnClickListener listener) {
        if (message == null)
            message = "";

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton(context.getString(R.string.yes).toUpperCase(), listener);
        b.setNegativeButton(context.getString(R.string.no).toUpperCase(), null);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static void showErrorMessage(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public static void showMessageVip(Context context, String message, DialogInterface.OnClickListener listener) {
        if (message == null)
            message = "";

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton(context.getString(R.string.menu_deposit).toUpperCase(), listener);
        b.setNegativeButton(context.getString(R.string.cm_close).toUpperCase(), null);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static void showMessageLogin(Context context, String message, DialogInterface.OnClickListener listener) {
        if (message == null)
            message = "";

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setPositiveButton(context.getString(R.string.login).toUpperCase(), listener);
        b.setNegativeButton(context.getString(R.string.cm_close).toUpperCase(), null);
        AlertDialog d = b.show();

        ((TextView) d.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    public static ProgressDialog showLoading(Context mContext, String message) {
        try {
            ProgressDialog loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setCancelable(false);
            if (!message.isEmpty())
                loadingDialog.setMessage(message);

            loadingDialog.show();

            return loadingDialog;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void hideLoading(ProgressDialog loadingDialog) {
        try {
            if (loadingDialog != null && loadingDialog.isShowing())
                loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
