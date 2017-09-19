package com.wgh.aiyue.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.cache.DataCache;
import com.wgh.aiyue.helper.DataCacheHelper;
import com.wgh.aiyue.ui.view.ViewPagerFragment;
import com.wgh.aiyue.util.DLog;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    private ArrayList<Category> mCategoryList;
    private Context mContext;
    private Fragment mFragment;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mCategoryList = DataCacheHelper.getInstance().getPagerChildCategories();
    }

    public void onCategorysChange(String key) {
        if (key != null) {
            mCategoryList = DataCache.getInstance().getChildCategorys(key);
            notifyDataSetChanged();
        } else {
            DLog.e("onCategorysChange() Error!");
        }
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragment.newInstance(mContext, mCategoryList.get(position));
    }

    @Override
    public int getCount() {
        if (mCategoryList != null) {
            return mCategoryList.size();
        } else {
            return 0;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryList.get(position).getName();
    }

    public String getFragmentTag(int viewPagerId, int fragmentPosition) {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
