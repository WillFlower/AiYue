package com.wgh.aiyue.model;

import com.wgh.aiyue.greendao.ListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = 7247714666080613254L;

    @Id(autoincrement = true)
    private Long id;
    private int grade;
    private String name;
    private String selfKey;

    @Convert(columnType = String.class, converter = ListConverter.class)
    private ArrayList<String> nextNames = new ArrayList<>();
    @Convert(columnType = String.class, converter = ListConverter.class)
    private ArrayList<String> contentUrls = new ArrayList<>();
    @Convert(columnType = String.class, converter = ListConverter.class)
    private ArrayList<String> contentTitles = new ArrayList<>();
    @Convert(columnType = String.class, converter = ListConverter.class)
    private ArrayList<String> nextKeys = new ArrayList<>();

    public Category() {
    }

    @Generated(hash = 182005897)
    public Category(Long id, int grade, String name, String selfKey, ArrayList<String> nextNames, ArrayList<String> contentUrls, ArrayList<String> contentTitles, ArrayList<String> nextKeys) {
        this.id = id;
        this.grade = grade;
        this.name = name;
        this.selfKey = selfKey;
        this.nextNames = nextNames;
        this.contentUrls = contentUrls;
        this.contentTitles = contentTitles;
        this.nextKeys = nextKeys;
    }

    public Category(int grade, String name) {
        this.grade = grade;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelfKey() {
        return selfKey;
    }

    public void setSelfKey(String selfKey) {
        this.selfKey = selfKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public ArrayList<String> getNextNames() {
        return nextNames;
    }

    public void setNextNames(ArrayList<String> nextNames) {
        this.nextNames = nextNames;
    }

    public ArrayList<String> getContentUrls() {
        return contentUrls;
    }

    public void setContentUrls(ArrayList<String> contentUrls) {
        this.contentUrls = contentUrls;
    }

    public void setContentUrlList(ArrayList<String> contentUrls) {
        this.contentUrls = contentUrls;
    }

    public String getContentUrl(int index) {
        return this.contentUrls.get(index);
    }

    public ArrayList<String> getContentTitles() {
        return contentTitles;
    }

    public void setContentTitles(ArrayList<String> contentTitles) {
        this.contentTitles = contentTitles;
    }

    public String getContentTitle(int index) {
        return this.contentTitles.get(index);
    }

    public ArrayList<String> getNextKeys() {
        if (nextKeys.size() == 0) {
            for (String nextName : nextNames) {
                nextKeys.add(name + nextName);
            }
        }
        return nextKeys;
    }

    public void setNextKeys(ArrayList<String> nextKeys) {
        this.nextKeys = nextKeys;
    }

    public void addNextName(String name) {
        nextNames.add(name);
    }

    public void addContentUrl(String url) {
        contentUrls.add(url);
    }

    public void addContentTitle(String title) {
        contentTitles.add(title);
    }

    public String getNextKey(int index) {
        if (nextKeys.size() == 0) {
            for (String nextName : nextNames) {
                nextKeys.add(name + nextName);
            }
        }
        if (nextKeys.size() > index) {
            return nextKeys.get(index);
        } else {
            return null;
        }
    }

    public String getKey() {
        return selfKey;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", grade=" + grade +
                ", name='" + name + '\'' +
                ", selfKey='" + selfKey + '\'' +
                ", nextNames=" + nextNames +
                ", contentUrls=" + contentUrls.toString() +
                ", contentTitles=" + contentTitles.toString() +
                ", nextKeys=" + nextKeys.toString() +
                '}';
    }
}
