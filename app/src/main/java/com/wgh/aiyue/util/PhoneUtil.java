package com.wgh.aiyue.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.wgh.aiyue.MyApplication;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by   : WGH.
 */
public class PhoneUtil {

    private static PhoneUtil phoneUtil;

    public static PhoneUtil getInstance() {
        if (phoneUtil == null) {
            phoneUtil = new PhoneUtil();
        }
        return phoneUtil;
    }

    public HashMap<String, String> getDeviceInfo() {
        HashMap<String, String> deviceInfos = new HashMap<>();
        try {
            PackageManager packageManager = MyApplication.context().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MyApplication.context().getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                deviceInfos.put("appName", AppUtil.getInstance().getAppName(MyApplication.context()));
                deviceInfos.put("versionName", AppUtil.getInstance().getVersionName(MyApplication.context()));
                deviceInfos.put("versionCode", String.valueOf(AppUtil.getInstance().getVersionCode(MyApplication.context())));
            }
        } catch (Exception e) {
            DLog.e("an error occured when collect package info.\n" + e.toString());
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                DLog.e("an error occured when collect crash info.\n" + e.toString());
                e.printStackTrace();
            }
        }
        return deviceInfos;
    }
}
