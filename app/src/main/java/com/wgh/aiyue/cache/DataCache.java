package com.wgh.aiyue.cache;

import com.wgh.aiyue.MyApplication;
import com.wgh.aiyue.greendao.CategoryDao;
import com.wgh.aiyue.helper.DatabaseHelper;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.util.ConstDefine;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by   : WGH.
 */
public class DataCache {

    private static String FirstKey = ConstDefine.SpaceString;
    private static DataCache mDataCache;
    private static LinkedHashMap<String, Category> mCategoryCache = new LinkedHashMap<>();

    private static CategoryDao categoryDao;

    public static DataCache getInstance() {
        if (mDataCache == null) {
            mDataCache = new DataCache();
        }
        if (categoryDao == null) {
            categoryDao = MyApplication.getDaoSession().getCategoryDao();
        }
        return mDataCache;
    }

    public static String getFirstKey() {
        return FirstKey;
    }

    public static void setFirstKey(String firstKey) {
        FirstKey = firstKey;
    }

    public void putCategory(Category category) {
        mCategoryCache.put(category.getSelfKey(), category);
    }

    public Category getCategory(String key) {
        if (mCategoryCache.get(key) == null) {
            syncCategoryFromDB(key);
        }
        return mCategoryCache.get(key);
    }

    public ArrayList<Category> getChildCategorys(String key) {
        if (key == null || mCategoryCache.size() == 0) {
            return null;
        }
        if (mCategoryCache.get(key) == null) {
            syncCategoryFromDB(key);
        }
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<String> childKeys = mCategoryCache.get(key).getNextKeys();
        for (String childKey : childKeys) {
            if (mCategoryCache.get(childKey) == null) {
                syncCategoryFromDB(childKey);
            }
            categories.add(mCategoryCache.get(childKey));
        }
        return categories;
    }

    private void syncCategoryFromDB(String key) {
        Category category = DatabaseHelper.getInstance().getCategotyFromDB(key);
        if (category != null) {
            putCategory(category);
        } else {
            syncCategoryFromNet(key);
        }
    }

    public void syncCategoryFromNet(String key) {
        // get data from net...
    }
}
