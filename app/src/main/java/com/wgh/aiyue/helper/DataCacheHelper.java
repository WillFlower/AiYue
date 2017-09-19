package com.wgh.aiyue.helper;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wgh.aiyue.MyApplication;
import com.wgh.aiyue.R;
import com.wgh.aiyue.cache.DataCache;
import com.wgh.aiyue.model.Category;
import com.wgh.aiyue.model.FavoriteContent;
import com.wgh.aiyue.ui.MainActivity;
import com.wgh.aiyue.util.ConstDefine;
import com.wgh.aiyue.util.UiUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static com.wgh.aiyue.MyApplication.getGson;
import static com.wgh.aiyue.util.ConstDefine.getRightDrawerCategoryKey;

/**
 * Created by   : WGH.
 */
public class DataCacheHelper {

    private static double itemScale = -1;
    private static DataCacheHelper dataCacheHelper;

    public static DataCacheHelper getInstance() {
        if (dataCacheHelper == null) {
            dataCacheHelper = new DataCacheHelper();
        }
        return dataCacheHelper;
    }

    public Category getCategory(String key) {
        return DataCache.getInstance().getCategory(key);
    }

    public void setCategory(Category category) {
        DataCache.getInstance().putCategory(category);
    }

    public Category getLeftCategory() {
        if (TextUtils.isEmpty(DataCache.getFirstKey())) {
            DataCache.setFirstKey(ConstDefine.getCategoryFirstKey());
        }
        return DataCache.getInstance().getCategory(DataCache.getFirstKey());
    }

    public Category getRightCategory() {
        Category category;
        if (TextUtils.isEmpty(getRightDrawerCategoryKey())) {
            category = DataCache.getInstance().getCategory(getLeftCategory().getNextKey(0));
        } else {
            category = DataCache.getInstance().getCategory(getRightDrawerCategoryKey());
        }
        return category;
    }

    public String getRightCategoryKey() {
        return getRightCategory().getKey();
    }

    public ArrayList<Category> getRightChildCategories() {
        ArrayList<Category> categories;
        if (TextUtils.isEmpty(getRightDrawerCategoryKey())) {
            categories = DataCache.getInstance().getChildCategorys(getLeftCategory().getNextKey(0));
        } else {
            categories = DataCache.getInstance().getChildCategorys(getRightDrawerCategoryKey());
        }
        return categories;
    }

    public ArrayList<Category> getPagerChildCategories() {
        ArrayList<Category> categories;
        if (TextUtils.isEmpty(ConstDefine.getViewPagerKey())) {
            categories = DataCache.getInstance().getChildCategorys(getRightCategory().getNextKey(0));
        } else {
            categories = DataCache.getInstance().getChildCategorys(ConstDefine.getViewPagerKey());
        }
        return categories;
    }

    public String getDefaultPagerCategoryKey(Category category) {
        return category.getNextKey(0);
    }

    public String getCategoryKey(Category category) {
        return category.getSelfKey();
    }

    public void saveRightChildCategoryList(ArrayList<Category> dragViewCategoryList, ArrayList<Category> belowViewCategoryList) {
        Category[] dragCategories = dragViewCategoryList.toArray(new Category[0]);
        Category[] belowCategories = belowViewCategoryList.toArray(new Category[0]);
        String dragCategoriesJson = getGson().toJson(dragCategories);
        String belowCategoriesJson = getGson().toJson(belowCategories);
        ConstDefine.setRightChildCategoryList(getRightCategoryKey(), dragCategoriesJson, belowCategoriesJson);
    }

    public ArrayList<Category> getRightDragCategoryList() {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        String rightDragCategoryListJson = ConstDefine.getRightDragCategoryList(getRightCategoryKey());
        Category[] rightDragCategories = getGson().fromJson(rightDragCategoryListJson, Category[].class);
        if (rightDragCategories != null && rightDragCategories.length > 0) {
            categoryArrayList.addAll(Arrays.asList(rightDragCategories));
        } else {
            categoryArrayList.addAll(getRightChildCategories());
        }
        return categoryArrayList;
    }

    public ArrayList<Category> getRightBelowCategoryList() {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        String rightBelowCategoryListJson = ConstDefine.getRightBelowCategoryList(getRightCategoryKey());
        Category[] rightBelowCategories = getGson().fromJson(rightBelowCategoryListJson, Category[].class);
        if (rightBelowCategories != null && rightBelowCategories.length > 0) {
            categoryArrayList.addAll(Arrays.asList(rightBelowCategories));
        }
        return categoryArrayList;
    }


    public void setFavoriteContents(Category category, ArrayList<Integer> indexList) {
        String favoriteKey = category.getKey();
        ArrayList<FavoriteContent> favoriteContentList = new ArrayList<>();
        for (Integer index : indexList) {
            FavoriteContent favorite = new FavoriteContent();
            favorite.setCategoryKey(favoriteKey);
            favorite.setIndex(index);
            favoriteContentList.add(favorite);
        }
        FavoriteContent[] favoriteContents = favoriteContentList.toArray(new FavoriteContent[0]);
        String favoriteContentJson = getGson().toJson(favoriteContents);
        ConstDefine.setFavoriteContents(category.getKey(), favoriteContentJson);
        if (indexList.size() <= 0) {
            removeFavoriteKey(favoriteKey);
        } else {
            addFavoriteKey(favoriteKey);
        }
    }

    public ArrayList<FavoriteContent> getFavoriteContents(Category category) {
        ArrayList<FavoriteContent> favoriteContentList = new ArrayList<>();
        String favoriteContentJson = ConstDefine.getFavoriteContents(category.getKey());
        FavoriteContent[] favoriteContents = MyApplication.getGson().fromJson(favoriteContentJson, FavoriteContent[].class);
        if (favoriteContents != null && favoriteContents.length > 0) {
            favoriteContentList.addAll(Arrays.asList(favoriteContents));
        }
        return favoriteContentList;
    }

    public ArrayList<String> getFavoriteUrls(Category category) {
        ArrayList<FavoriteContent> favoriteContentList = getFavoriteContents(category);
        ArrayList<String> favoriteUrlList = new ArrayList<>();
        for (FavoriteContent favoriteContent : favoriteContentList) {
            favoriteUrlList.add(category.getContentUrl(favoriteContent.getIndex()));
        }
        return favoriteUrlList;
    }

    public ArrayList<String> getFavoriteTitles(Category category) {
        ArrayList<FavoriteContent> favoriteContentList = getFavoriteContents(category);
        ArrayList<String> favoriteTitleList = new ArrayList<>();
        for (FavoriteContent favoriteContent : favoriteContentList) {
            favoriteTitleList.add(category.getContentTitle(favoriteContent.getIndex()));
        }
        return favoriteTitleList;
    }

    public ArrayList<String> getFavoriteTitles(Category category, ArrayList<String> favoriteUrls) {
        if (category == null) {
            return null;
        }
        ArrayList<String> favoriteTitles = new ArrayList<>();
        ArrayList<String> contentUrls = category.getContentUrls();
        for (String contentUrl : contentUrls) {
            if (favoriteUrls.contains(contentUrl)) {
                favoriteTitles.add(category.getContentTitle(contentUrls.indexOf(contentUrl)));
            }
        }
        return favoriteTitles;
    }

    public ArrayList<Integer> getFavoriteIndex(Category category) {
        ArrayList<FavoriteContent> favoriteContentList = getFavoriteContents(category);
        ArrayList<Integer> favoriteIndexList = new ArrayList<>();
        for (FavoriteContent favoriteContent : favoriteContentList) {
            favoriteIndexList.add(favoriteContent.getIndex());
        }
        return favoriteIndexList;
    }

    public void addFavoriteKey(String favoriteKey) {
        ArrayList<String> favoriteKeyList = getAllFavoriteKey();
        if (!favoriteKeyList.contains(favoriteKey)) {
            favoriteKeyList.add(favoriteKey);
            String favoriteKeyJson = getGson().toJson(favoriteKeyList);
            ConstDefine.setAllFavoriteKey(favoriteKeyJson);
        }
    }

    public void removeFavoriteKey(String favoriteKey) {
        ArrayList<String> favoriteKeyList = getAllFavoriteKey();
        if (favoriteKeyList.contains(favoriteKey)) {
            favoriteKeyList.remove(favoriteKey);
            String favoriteKeyJson = getGson().toJson(favoriteKeyList);
            ConstDefine.setAllFavoriteKey(favoriteKeyJson);
        }
    }

    public ArrayList<String> getAllFavoriteKey() {
        ArrayList<String> favoriteKeyList = new ArrayList<>();
        String favoriteKeyJson = ConstDefine.getAllFavoriteKey();
        String[] favoriteKey = MyApplication.getGson().fromJson(favoriteKeyJson, String[].class);
        if (favoriteKey != null && favoriteKey.length > 0) {
            favoriteKeyList.addAll(Arrays.asList(favoriteKey));
        }
        return favoriteKeyList;
    }

    public ArrayList<Category> getAllFavoriteCategory() {
        ArrayList<String> favoriteKeyList = getAllFavoriteKey();
        ArrayList<Category> favoriteCategoryList = new ArrayList<>();
        for (String favoriteKey : favoriteKeyList) {
            favoriteCategoryList.add(DataCacheHelper.getInstance().getCategory(favoriteKey));
        }
        return favoriteCategoryList;
    }

    public int getThemeIconId() {
        int iconId = 0;
        switch (ConstDefine.getAppTheme()) {
            case WillFLow:
                iconId = R.drawable.head_love;
                break;
            case Blue:
                iconId =  R.drawable.head_blue;
                break;
            case Green:
                iconId =  R.drawable.head_green;
                break;
            case Red:
                iconId =  R.drawable.head_red;
                break;
            case Purple:
                iconId =  R.drawable.head_purple;
                break;
            case Orange:
                iconId =  R.drawable.head_orange;
                break;
            case Pink:
                iconId =  R.drawable.head_pink;
                break;
            case Grey:
                iconId =  R.drawable.head_grey;
                break;
            case Black:
                iconId =  R.drawable.head_black;
                break;
            default:
                iconId = R.drawable.head_love;
        }
        return iconId;
    }

    public void setCardParams(CardView cardView, int column) {
        int cardRadius = 0;
        int cardElevation = 0;
        int cardMargin = 0;
        switch (column) {
            case 0:
            case 1:
            case 2:
                cardRadius = 33;
                cardElevation = 5;
                cardMargin = 15;
                break;
            case 3:
                cardRadius = 25;
                cardElevation = 3;
                cardMargin = 10;
                break;
            case 4:
            case 5:
            case 6:
                cardRadius = 20;
                cardElevation = 2;
                cardMargin = 7;
                break;
        }
        cardView.setRadius(cardRadius);
        cardView.setCardElevation(cardElevation);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);
        cardView.setLayoutParams(layoutParams);
    }

    public void setItemHight(ImageView imageView, TextView textView, int imgHeight) {
        ViewGroup.LayoutParams paramsImg = imageView.getLayoutParams();
        ViewGroup.LayoutParams paramsText = textView.getLayoutParams();
        paramsImg.height = imgHeight;
        paramsText.height = imgHeight / 6;
        imageView.setLayoutParams(paramsImg);
        textView.setLayoutParams(paramsText);
        if (itemScale < 0) {
            itemScale = UiUtil.getInstance(MainActivity.activityMain).getDensityDpi() / 68;
        }
        textView.setTextSize((float) (imgHeight / 4.6 / itemScale));
    }
}
