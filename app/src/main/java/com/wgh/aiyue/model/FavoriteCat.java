package com.wgh.aiyue.model;

import com.wgh.aiyue.greendao.ListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
@Entity
public class FavoriteCat {
    @Id(autoincrement = true)
    private Long id;
    private String categoryKey;
    @Convert(columnType = String.class, converter = ListConverter.class)
    private ArrayList<String> favoriteUrls = new ArrayList<>();

    @Generated(hash = 2133203663)
    public FavoriteCat(Long id, String categoryKey, ArrayList<String> favoriteUrls) {
        this.id = id;
        this.categoryKey = categoryKey;
        this.favoriteUrls = favoriteUrls;
    }

    @Generated(hash = 937828944)
    public FavoriteCat() {
    }

    public FavoriteCat(String categoryKey, ArrayList<String> favoriteUrls) {
        this.categoryKey = categoryKey;
        this.favoriteUrls = favoriteUrls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public ArrayList<String> getFavoriteUrls() {
        return favoriteUrls;
    }

    public void setFavoriteUrls(ArrayList<String> favoriteUrls) {
        this.favoriteUrls = favoriteUrls;
    }
}
