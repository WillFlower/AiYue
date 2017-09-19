package com.wgh.aiyue.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.ui.view.FavoritePagerFragment;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
public class FavoritePagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private ArrayList<Category> mCategoryList;

    public FavoritePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mCategoryList = DatabaseHelper.getInstance().getAllFavoriteCategory();
    }

    @Override
    public Fragment getItem(int position) {
        return FavoritePagerFragment.newInstance(mContext, mCategoryList.get(position));
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
}
