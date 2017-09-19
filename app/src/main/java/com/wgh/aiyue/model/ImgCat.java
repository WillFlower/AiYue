package com.wgh.aiyue.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by   : WGH.
 */
@Entity
public class ImgCat {
    @Id(autoincrement = true)
    private Long id;
    private String contentUrl;
    private String imgUrl;

    @Generated(hash = 896637884)
    public ImgCat() {
    }

    public ImgCat(String contentUrl, String imgUrl) {
        this.contentUrl = contentUrl;
        this.imgUrl = imgUrl;
    }

    @Generated(hash = 1692887716)
    public ImgCat(Long id, String contentUrl, String imgUrl) {
        this.id = id;
        this.contentUrl = contentUrl;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
