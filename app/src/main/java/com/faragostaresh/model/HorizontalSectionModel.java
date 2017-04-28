package com.faragostaresh.model;

import java.util.ArrayList;

public class HorizontalSectionModel {

    private String headerTitle;
    private ArrayList<HorizontalSingleItemModel> allItemsInSection;

    public HorizontalSectionModel() {

    }
    public HorizontalSectionModel(String headerTitle, ArrayList<HorizontalSingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<HorizontalSingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<HorizontalSingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
