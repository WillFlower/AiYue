package com.wgh.aiyue;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wgh.aiyue.greendao.DaoMaster;
import com.wgh.aiyue.greendao.DaoSession;
import com.wgh.aiyue.helper.CrashHelper;
import com.wgh.aiyue.service.AiService;
import com.wgh.aiyue.util.ConstDefine;

/**
 * Created by   : WGH.
 */
public class MyApplication extends Application{

    private static MyApplication mMyApplication;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;

        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AiService.class.getName().equals(service.service.getClassName())) {
                isServiceRunning = true;
                break;
            }
        }

        if (!isServiceRunning) {
            try {
                Intent intent = new Intent(this, AiService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CrashHelper handler = CrashHelper.getInstance();
        handler.init(getApplicationContext());
    }

    public static MyApplication context() {
        return mMyApplication;
    }

    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(mMyApplication, ConstDefine.DBNAME, null);
            daoMaster = new DaoMaster(helper.getReadableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
