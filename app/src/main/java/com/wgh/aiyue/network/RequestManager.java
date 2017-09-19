package com.wgh.aiyue.network;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wgh.aiyue.BuildConfig;
import com.wgh.aiyue.MyApplication;
import com.wgh.aiyue.util.DLog;

public class RequestManager {

    private static boolean isRequset = true;
    private static final int OVER_TIME = 10000;
    private static final int TIMES_OF_RETRY = 1;
    private static RequestManager requestManager;
    private static RequestQueue mRequestQueue;
    private static com.bumptech.glide.RequestManager mGlideManager;

    private RequestManager() {
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
            mRequestQueue = Volley.newRequestQueue(MyApplication.context());
            mGlideManager = Glide.with(MyApplication.context());
        }
        return requestManager;
    }

    public void addRequest(Request<?> request, Object tag) {
        if (!isRequset) {
            return;
        }
        if (tag != null) {
            request.setTag(tag);
        }
        request.setRetryPolicy(new DefaultRetryPolicy(OVER_TIME, TIMES_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

        if (BuildConfig.DEBUG) {
            DLog.i(request.getUrl());
        }
    }

    public com.bumptech.glide.RequestManager getGlideManager() {
        return mGlideManager;
    }

    public void setGlidePause() {
        if (!mGlideManager.isPaused()) {
            mGlideManager.pauseRequests();
        }
    }

    public void setGlideResume() {
        if (mGlideManager.isPaused()) {
            mGlideManager.resumeRequests();
        }
    }

    public void setRequsetEnable(boolean flag) {
        isRequset = flag;
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}