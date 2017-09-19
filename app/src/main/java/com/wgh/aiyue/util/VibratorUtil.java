package com.wgh.aiyue.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.wgh.aiyue.MyApplication;

public class VibratorUtil {

    /**
     * long milliseconds ：Length of vibration(ms)
     */
    public static void Vibrate(long millSecond) {
        Vibrator vibrator = (Vibrator) MyApplication.context().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(millSecond);
    }

    public static void Vibrate(Context context, long millSecond) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(millSecond);
    }

    /**
     * long[] pattern ：Custom shock mode. long at rest, long at shock, long at rest, long at shock...
     * boolean isRepeat ：Repeated vibration.
     */
    public static void Vibrate(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
