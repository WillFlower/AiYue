package com.wgh.aiyue.util;

import com.wgh.aiyue.message.CrashMessage;
import com.wgh.aiyue.message.KeyMessage;
import com.wgh.aiyue.message.PagerCategoryMessage;
import com.wgh.aiyue.message.PayMessage;
import com.wgh.aiyue.message.RightCategoryMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by   : WGH.
 */
public class MessageUtil {
    public static void postRightCategoryChangeMsg(String categoryKey) {
        RightCategoryMessage message = new RightCategoryMessage();
        message.what = ConstDefine.DrawerCategoryChange;
        message.drawerKey = categoryKey;
        EventBus.getDefault().post(message);
    }

    public static void postPagerCategoryChangeMsg(String categoryKey) {
        PagerCategoryMessage message = new PagerCategoryMessage();
        message.what = ConstDefine.PagerCategoryChange;
        message.key = categoryKey;
        EventBus.getDefault().post(message);
    }

    public static void postAppCrashMsg(Throwable exception, Thread.UncaughtExceptionHandler exceptionHandler, Thread thread) {
        CrashMessage crashMessage = new CrashMessage();
        crashMessage.what = ConstDefine.AppCrash;
        crashMessage.throwable = exception;
        crashMessage.exceptionHandler = exceptionHandler;
        crashMessage.thread = thread;
        EventBus.getDefault().post(crashMessage);
    }

    public static void postPayMsg(Map<String, String> result) {
        PayMessage payMessage = new PayMessage();
        payMessage.what = ConstDefine.PayOffical;
        payMessage.object = result;
        EventBus.getDefault().post(payMessage);
    }

    public static void postKeyMsg(String userKey) {
        KeyMessage keyMessage = new KeyMessage();
        keyMessage.what = ConstDefine.UserKey;
        keyMessage.userKey = userKey;
        EventBus.getDefault().post(keyMessage);
    }
}
