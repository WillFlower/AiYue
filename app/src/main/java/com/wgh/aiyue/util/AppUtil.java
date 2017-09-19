package com.wgh.aiyue.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.wgh.aiyue.MyApplication;

import java.io.File;

/**
 * Created by   : WGH.
 */

public class AppUtil {

    private static AppUtil appUtil;

    public static AppUtil getInstance() {
        if (appUtil == null) {
            appUtil = new AppUtil();
        }
        return appUtil;
    }

    private AppUtil() {
//        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Since you did not start Activity in the Activity environment, set the following label.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(MyApplication.context(), "com.wgh.willflowaicollection.fileprovider", file);
            // Add this sentence to indicate that the application is temporarily authorized to the file represented by the Uri.
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        MyApplication.context().startActivity(intent);
    }
}
