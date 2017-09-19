package com.wgh.aiyue.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.util.ConstDefine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by   : WGH.
 */
public class SearchListAdapter extends BaseListAdapter {

    private OnSearchFinishListener onSearchFinishListener;

    public void setOnSearchFinishListener(OnSearchFinishListener listener) {
        this.onSearchFinishListener = listener;
    }

    public interface OnSearchFinishListener {
        void onSearchSuccess();
        void onSearchFail();
    }

    public SearchListAdapter(Context mContext, OnLoadPageFinishListener loadPageFinishListener) {
        super(mContext, loadPageFinishListener);
    }

    @Override
    protected void initContentData() {
        if (getContentDate(null)) {
            if (onSearchFinishListener != null) {
                onSearchFinishListener.onSearchSuccess();
            }
        } else {
            if (onSearchFinishListener != null) {
                onSearchFinishListener.onSearchFail();
            }
        }
    }

    public void updateData(String string) {
        if (getContentDate(string)) {
            notifyDataSetChanged();
            if (onSearchFinishListener != null) {
                onSearchFinishListener.onSearchSuccess();
            }
        } else {
            if (onSearchFinishListener != null) {
                onSearchFinishListener.onSearchFail();
            }
        }
    }

    private boolean getContentDate(String searchString) {
        HashMap<String, String> mapSearch;
        if (TextUtils.isEmpty(searchString)) {
            if (!TextUtils.isEmpty(ConstDefine.getSearch())) {
                mapSearch = DatabaseHelper.getInstance().getEligibleContent(ConstDefine.getSearch());
            } else {
                return false;
            }
        } else {
            mapSearch = DatabaseHelper.getInstance().getEligibleContent(searchString);
        }
        if (null == mapSearch) {
            return false;
        }
        Iterator iterator = mapSearch.entrySet().iterator();
        if (iterator.hasNext() && !TextUtils.isEmpty(searchString)) {
            ConstDefine.setLastSearch(searchString);
        }
        if (!mContentTitles.isEmpty()) {
            mContentTitles.clear();
        }
        if (!mContentUrls.isEmpty()) {
            mContentUrls.clear();
        }
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object contentTitle = entry.getKey();
            Object contentUrl = entry.getValue();
            mContentTitles.add(contentTitle.toString());
            mContentUrls.add(contentUrl.toString());
        }
        return true;
    }
}
