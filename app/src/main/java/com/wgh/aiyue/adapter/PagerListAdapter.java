package com.wgh.aiyue.adapter;

import android.content.Context;

import com.wgh.aiyue.model.Category;

/**
 * Created by   : WGH.
 */
public class PagerListAdapter extends BaseListAdapter {
    public PagerListAdapter(Context mContext, Category mCategory, OnLoadPageFinishListener mLoadPageFinishListener) {
        super(mContext, mCategory, mLoadPageFinishListener);
    }
}
