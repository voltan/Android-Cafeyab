package com.faragostaresh.model;

public class HorizontalSingleItemModel {


    private String itemTitle;
    private String itemId;
    private String itemImage;
    private String itemType;


    public HorizontalSingleItemModel() {
    }

    public HorizontalSingleItemModel(String itemTitle, String itemImage, String itemType, String itemId) {
        this.itemTitle = itemTitle;
        this.itemImage = itemImage;
        this.itemType = itemType;
        this.itemId = itemId;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String type) {
        this.itemType = type;
    }


}
