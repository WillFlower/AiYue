package com.wgh.aiyue.helper;

import android.text.TextUtils;

import com.wgh.aiyue.MyApplication;
import com.wgh.aiyue.greendao.CategoryDao;
import com.wgh.aiyue.greendao.FavoriteCatDao;
import com.wgh.aiyue.greendao.ImgCatDao;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.model.FavoriteCat;
import com.wgh.aiyue.model.ImgCat;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by   : WGH.
 */
public class DatabaseHelper {

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getInstance() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;
    }

    public Category getCategotyFromDB(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder().where(CategoryDao.Properties.SelfKey.eq(key));
        if (queryBuilder.list().size() > 0) {
            return queryBuilder.list().get(0);
        } else {
            return null;
        }
    }

    public void insertOrUpdateCategory(Category categoryNew) {
        if (categoryNew == null) {
            return;
        }
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        Category categoryOld = getCategotyFromDB(categoryNew.getKey());
        if (categoryOld != null) {
            ArrayList<String> contentUrlOld = categoryOld.getContentUrls();
            ArrayList<String> contentUrlNew = categoryNew.getContentUrls();
            if (!contentUrlOld.toString().equals(contentUrlNew.toString())) {
                categoryNew.setId(categoryOld.getId());
                categoryDao.update(categoryNew);
                return;
            }
            ArrayList<String> nextNameOld = categoryOld.getNextNames();
            ArrayList<String> nextNameNew = categoryNew.getNextNames();
            if (!nextNameOld.toString().equals(nextNameNew.toString())) {
                categoryNew.setId(categoryOld.getId());
                categoryDao.update(categoryNew);
            }
        } else {
            categoryDao.insert(categoryNew);
        }
    }

    public ArrayList<Category> getEligibleCategory(String contentString) {
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder().where(CategoryDao.Properties.ContentTitles.like("%" + contentString + "%"));
        if (queryBuilder.list().size() > 0) {
            return new ArrayList<>(queryBuilder.list());
        } else {
            return null;
        }
    }

    public HashMap<String, String> getEligibleContent(String contentString) {
        HashMap<String, String> eligibleContents = new HashMap<>();
        ArrayList<Category> categories = getEligibleCategory(contentString);
        if (categories == null) {
            return null;
        }
        for (Category category : categories) {
            ArrayList<String> contentTitles = category.getContentTitles();
            ArrayList<String> contentUrls = category.getContentUrls();
            for (int i = 0; i < contentTitles.size(); i++) {
                if (contentTitles.get(i).toLowerCase().contains(contentString.toLowerCase())) {
                    eligibleContents.put(contentTitles.get(i), contentUrls.get(i));
                }
            }
        }
        return eligibleContents;
    }

    public void deleteAllCategory() {
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        categoryDao.deleteAll();
    }

    public void showCategory(Category category) {
        if (category == null) {
            return;
        }
        Category categotyFromDB = DatabaseHelper.getInstance().getCategotyFromDB(category.getKey());
    }

    public void deleteCategoryFromDB(Category category) {
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        Category categoryOld = getCategotyFromDB(category.getKey());
        if (categoryOld != null) {
            categoryDao.delete(categoryOld);
        }
    }

    public void updateCategoryToDB(Category categoryNew) {
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        Category categoryOld = getCategotyFromDB(categoryNew.getKey());
        categoryNew.setId(categoryOld.getId());
        categoryDao.update(categoryNew);
    }

    public List<Category> getCategoryByPage(int offset) {
        CategoryDao categoryDao = MyApplication.getDaoSession().getCategoryDao();
        // Query data on page offset, 20 per page
        return categoryDao.queryBuilder().offset(offset * 20).limit(20).list();
    }


    public ImgCat getImgCatFromDB(String contentUrl) {
        if (TextUtils.isEmpty(contentUrl)) {
            return null;
        }
        ImgCatDao imgCatDao = MyApplication.getDaoSession().getImgCatDao();
        QueryBuilder<ImgCat> queryBuilder = imgCatDao.queryBuilder().where(ImgCatDao.Properties.ContentUrl.eq(contentUrl));
        if (queryBuilder.list().size() > 0) {
            return queryBuilder.list().get(0);
        } else {
            return null;
        }
    }

    public void insetOrUpdateImgCat(ImgCat imgCatNew) {
        if (imgCatNew == null) {
            return;
        }
        ImgCatDao imgCatDao = MyApplication.getDaoSession().getImgCatDao();
        ImgCat imgCatOld = getImgCatFromDB(imgCatNew.getContentUrl());
        if (imgCatOld != null) {
            imgCatNew.setId(imgCatOld.getId());
            imgCatDao.update(imgCatNew);
        } else {
            imgCatDao.insert(imgCatNew);
        }
    }

    public void deleteImgCatFromDB(String contentUrl) {
        ImgCatDao imgCatDao = MyApplication.getDaoSession().getImgCatDao();
        ImgCat imgCatOld = getImgCatFromDB(contentUrl);
        if (imgCatOld != null) {
            imgCatDao.delete(imgCatOld);
        }
    }


    public ArrayList<String> getFavoriteUrlFromDB(Category category) {
        ArrayList<String> favoriteUrls = new ArrayList<>();
        if (getFavoriteCatFromDB(category) != null) {
            favoriteUrls = getFavoriteCatFromDB(category).getFavoriteUrls();
        }
        return favoriteUrls;
    }

    public FavoriteCat getFavoriteCatFromDB(Category category) {
        if (category == null) {
            return null;
        }
        FavoriteCatDao favoriteCatDao = MyApplication.getDaoSession().getFavoriteCatDao();
        QueryBuilder<FavoriteCat> queryBuilder = favoriteCatDao.queryBuilder().where(FavoriteCatDao.Properties.CategoryKey.eq(category.getKey()));
        if (queryBuilder.list().size() > 0) {
            return queryBuilder.list().get(0);
        } else {
            return null;
        }
    }

    public void insertOrUpdateFavorite(Category category, ArrayList<String> favoriteUrlsNew) {
        if (category == null || favoriteUrlsNew == null) {
            return;
        }
        FavoriteCatDao favoriteCatDao = MyApplication.getDaoSession().getFavoriteCatDao();
        FavoriteCat favoriteCatNew = new FavoriteCat(category.getKey(), favoriteUrlsNew);
        FavoriteCat favoriteCatOld = getFavoriteCatFromDB(category);
        if (favoriteCatOld != null) {
            favoriteCatNew.setId(favoriteCatOld.getId());
            if (favoriteUrlsNew.size() <= 0) {
                favoriteCatDao.delete(favoriteCatNew);
            } else {
                favoriteCatDao.update(favoriteCatNew);
            }
        } else {
            favoriteCatDao.insert(favoriteCatNew);
        }
    }

    public ArrayList<Category> getAllFavoriteCategory() {
        ArrayList<FavoriteCat> favoriteCats = getAllFavoriteCat();
        ArrayList<Category> categories = new ArrayList<>();
        if (favoriteCats != null) {
            for (FavoriteCat favoriteCat : favoriteCats) {
                categories.add(DataCacheHelper.getInstance().getCategory(favoriteCat.getCategoryKey()));
            }
        }
        return categories;
    }

    public ArrayList<FavoriteCat> getAllFavoriteCat() {
        FavoriteCatDao favoriteCatDao = MyApplication.getDaoSession().getFavoriteCatDao();
        return new ArrayList<>(favoriteCatDao.loadAll());
    }
}
