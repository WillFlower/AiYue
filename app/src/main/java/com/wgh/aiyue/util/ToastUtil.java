package com.wgh.aiyue.util;

import android.content.Context;
import android.widget.Toast;

import com.wgh.aiyue.MyApplication;

/**
 * Created by   : WGH.
 */
public class ToastUtil {

    public static void ToastShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void ToastShort(String string) {
        ToastShort(MyApplication.context(), string);
    }

    public static void ToastShort(int stringId) {
        ToastShort(MyApplication.context(), MyApplication.context().getString(stringId));
    }

    public static void ToastLong(int stringId) {
        ToastShort(MyApplication.context(), MyApplication.context().getString(stringId));
    }
}
