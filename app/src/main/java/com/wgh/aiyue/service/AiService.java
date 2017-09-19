package com.wgh.aiyue.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.wgh.aiyue.R;
import com.wgh.aiyue.helper.EmailHelper;
import com.wgh.aiyue.message.CrashMessage;
import com.wgh.aiyue.message.KeyMessage;
import com.wgh.aiyue.message.PayMessage;
import com.wgh.aiyue.model.PayResult;
import com.wgh.aiyue.ui.MainActivity;
import com.wgh.aiyue.util.DLog;
import com.wgh.aiyue.util.DateUtil;
import com.wgh.aiyue.util.MessageUtil;
import com.wgh.aiyue.util.NetWorkUtil;
import com.wgh.aiyue.util.PhoneUtil;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by   : WGH.
 */
public class AiService extends Service {

    private HashMap<String, String> deviceInfos = new HashMap<>();
    private String mFilePath;
    private Thread mThread;
    private Throwable mException;
    private Thread.UncaughtExceptionHandler mExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), ConstDefine.LogFileName);
                if (file.exists() && NetWorkUtil.isNetWork()) {
                    String emailSubject = "Phone manufacturers : " + Build.MANUFACTURER + ",  Phone model : " + Build.MODEL;
                    String[] toAddress = {ConstDefine.getMailNumTo()};
                    EmailHelper.sendComplex(toAddress, MainActivity.activityMain.getResources()
                            .getString(R.string.mail_title_crashlog), emailSubject, mFilePath);
                    deleteFile();
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CrashMessage message) {
        switch (message.what) {
            case ConstDefine.AppCrash:
                mThread = message.thread;
                mException = message.throwable;
                mExceptionHandler = message.exceptionHandler;
                handleException(mException);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KeyMessage message) {
        switch (message.what) {
            case ConstDefine.UserKey:
                final String orderInfo = message.userKey;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(MainActivity.activityMain);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        MessageUtil.postPayMsg(result);
                    }
                }).start();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PayMessage message) {
        switch (message.what) {
            case ConstDefine.PayOffical:
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) message.object);
                String resultInfo = payResult.getResult();  // Synchronously returns the information that needs validation
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    ToastUtil.ToastLong(R.string.payoffical_number_success);
                    ConstDefine.isOfficalVersion(true);
                } else {
                    ToastUtil.ToastShort(R.string.payoffical_number_error);
                    ConstDefine.isOfficalVersion(false);
                }
                break;
        }
    }

    private boolean handleException(Throwable exception) {
        if (exception == null) {
            return false;
        }
        final StackTraceElement[] stack = exception.getStackTrace();
        final String mEmailSubject = "Phone manufacturers : " + Build.MANUFACTURER + ",  Phone model : " + Build.MODEL;
        final String header = getLogHeader();
        final String message = exception.getMessage();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File file = new File(Environment.getExternalStorageDirectory(), ConstDefine.LogFileName);
                    mFilePath = file.getPath();
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        fileOutputStream.write((DateUtil.getCurrentTimeInString() + "\n").getBytes());
                        fileOutputStream.write((mEmailSubject + "\n").getBytes());
                        fileOutputStream.write((header + "\n").getBytes());
                        fileOutputStream.write((message + "\n").getBytes());
                        for (StackTraceElement stackTraceElement : stack) {
                            fileOutputStream.write(stackTraceElement.toString().getBytes());
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        if (NetWorkUtil.isNetWork()) {
                            String[] toAddress = {ConstDefine.getMailNumTo()};
                            EmailHelper.sendComplex(toAddress, MainActivity.activityMain.getResources()
                                    .getString(R.string.mail_title_crashlog), mEmailSubject, mFilePath);
                            deleteFile();
                        }
                        handleExceptionBySystem();
                    } catch (Exception e) {
                        DLog.e(e.toString());
                    }
                }
                Looper.loop();
            }
        }.start();
        return true;
    }

    private void handleExceptionBySystem() {
        mExceptionHandler.uncaughtException(mThread, mException);
    }

    private String getLogHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : PhoneUtil.getInstance().getDeviceInfo().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key + ":" + value + "\n");
        }
        return stringBuilder.toString();
    }

    public void deleteFile() {
        File file = new File(mFilePath);
        if (file.isFile()) {
            file.delete();
        }
    }
}
