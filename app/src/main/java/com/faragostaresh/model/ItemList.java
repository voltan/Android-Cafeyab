package com.faragostaresh.model;

import android.graphics.Bitmap;

public class ItemList {

    private String title, thumblUrl, itemtime, itemid, largUrl, city_area, item_type;
    private Bitmap image;

    public ItemList() {
    }

    public ItemList(String name, String tumbnailUrl, String itemtimeadded, String id, String largUrl, String city_area, String itemtype) {
        this.title = name;
        this.thumblUrl = tumbnailUrl;
        this.itemtime = itemtimeadded;
        this.itemid = id;
        this.largUrl = largUrl;
        this.city_area = city_area;
        this.item_type = itemtype;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }


    public String getItemtime() {
        return itemtime;
    }

    public void setItemtime(String itime) {
        this.itemtime = itime;
    }

    public String getThumbnailUrl() {
        return thumblUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumblUrl = thumbnailUrl;
    }

    public String getItemId() {
        return itemid;
    }

    public void setItemID(String itemid) {
        this.itemid = itemid;
    }

    public String getLargeUrl() {
        return this.largUrl;
    }

    public void setLargeUrl(String largUrl) {
        this.largUrl = largUrl;
    }

    public String getCityArea() {
        return this.city_area;
    }

    public void setCityArea(String cityArea) {
        this.city_area = cityArea;
    }

    public String getItemType() {
        return this.item_type;
    }

    public void setItemType(String itemtype) {
        this.item_type = itemtype;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

