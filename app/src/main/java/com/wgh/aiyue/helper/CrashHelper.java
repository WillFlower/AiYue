package com.wgh.aiyue.helper;

import android.content.Context;

import com.wgh.aiyue.util.MessageUtil;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by   : WGH.
 */
public class CrashHelper implements UncaughtExceptionHandler {

    private Context mContext;
    private static CrashHelper crashHelper;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHelper() {
    }

    public static CrashHelper getInstance() {
        if (crashHelper == null) {
            crashHelper = new CrashHelper();
        }
        return crashHelper;
    }

    public void init(Context context) {
        mContext = context;
        // to get the default UncaughtException of system
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // to set the CrashHandler as the default program handler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        MessageUtil.postAppCrashMsg(exception, mDefaultHandler, thread);
    }
}
