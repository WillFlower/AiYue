package com.wgh.aiyue.helper;

import android.content.Context;
import android.content.DialogInterface;

import com.wgh.aiyue.R;
import com.wgh.aiyue.ui.view.CustomDialog;

/**
 * Created by   : WGH.
 */
public class DialogHelper {

    private static DialogHelper dialogHelper;
    private static Context mContext;
    private static AlertListener mAlertListener;
    private static PayOfficalListener mOfficalListener;

    public DialogHelper(Context context) {
        this.mContext = context;
    }

    public interface AlertListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    public interface PayOfficalListener {
        void onPositiveButtonClick(DialogInterface dialog, String string);
        void onNegativeButtonClick(DialogInterface dialog, String string);
    }

    public static DialogHelper getInstance(Context context) {
        dialogHelper = new DialogHelper(context);
        return dialogHelper;
    }

    public static DialogHelper getInstance(Context context, AlertListener alertListener) {
        dialogHelper = new DialogHelper(context);
        mAlertListener = alertListener;
        return dialogHelper;
    }

    public static DialogHelper getInstance(Context context, PayOfficalListener officalListener) {
        dialogHelper = new DialogHelper(context);
        mOfficalListener = officalListener;
        return dialogHelper;
    }

    public void showAlertDialog(String message) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setTitle(R.string.dialog_title_alert);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlertListener.onPositiveButtonClick();
            }
        });

        builder.setNegativeButton(R.string.dialog_button_negative, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlertListener.onNegativeButtonClick();
            }
        });
        builder.create().show();
    }

    public void showOfficalDialog(String message) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setLayout(R.layout.dialog_payofficial_layout);
        builder.setMessage(message);
        builder.setTitle(R.string.dialog_title_update);
        builder.setPositiveButton(R.string.dialog_button_payoffical, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mOfficalListener.onPositiveButtonClick(dialog, builder.getInputText());
            }
        });

        builder.setNegativeButton(R.string.dialog_button_inputnum, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mOfficalListener.onNegativeButtonClick(dialog, builder.getInputText());
            }
        });
        builder.create().show();
    }
}
