package com.faragostaresh.model;

public class EventList {
    int _id;
    String _title;
    String _subtitle;
    String _category;
    String _categoryMainTitle;
    String _summary;
    String _description;
    String _time_create;
    String _time_update;
    String _time_view;
    String _image;
    String _recommended;
    String _eventUrl;
    String _largeUrl;


    public EventList() {

    }

    public EventList(
            int id,
            String title,
            String subtitle,
            String category,
            String categoryMainTitle,
            String summary,
            String description,
            String time_create,
            String time_update,
            String time_view,
            String image,
            String recommended,
            String eventUrl,
            String largeUrl

    ) {
        this._id = id;
        this._title = title;
        this._subtitle = subtitle;
        this._category = category;
        this._categoryMainTitle = categoryMainTitle;
        this._summary = summary;
        this._description = description;
        this._time_create = time_create;
        this._time_update = time_update;
        this._time_view = time_view;
        this._recommended = recommended;
        this._largeUrl = largeUrl;
        this._image = image;
        this._eventUrl = eventUrl;

    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getTitle() {
        return this._title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getSubTitle() {
        return this._subtitle;
    }

    public void setSubTitle(String subtitle) {
        this._subtitle = subtitle;
    }

    public String getSummary() {
        return this._summary;
    }

    public void setSummary(String summary) {
        this._summary = summary;
    }

    public String getDescription() {
        return this._description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getLargeUrl() {
        return this._largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this._largeUrl = largeUrl;
    }

    public String getImage() {
        return this._image;
    }

    public void setImage(String image) {
        this._image = image;
    }

    public String getCategory() {
        return this._category;
    }

    public void setCategory(String category) {
        this._category = category;
    }

    public String getCategoryMainTitle() {
        return this._categoryMainTitle;
    }

    public void setCategoryMainTitle(String categoryMainTitle) {
        this._categoryMainTitle = categoryMainTitle;
    }

    public String getTimeCreate() {
        return this._time_create;
    }

    public void setTimeCreate(String time_create) {
        this._time_create = time_create;
    }

    public String getTimeUpdate() {
        return this._time_update;
    }

    public void setTimeUpdate(String time_update) {
        this._time_update = time_update;
    }

    public String getTimeView() {
        return this._time_view;
    }

    public void setTimeView(String time_view) {
        this._time_view = time_view;
    }

    public String getRecommended() {
        return this._recommended;
    }

    public void setRecommended(String recommended) {
        this._recommended = recommended;
    }

    public String geteventUrl() {
        return this._eventUrl;
    }

    public void seteventUrl(String eventUrl) {
        this._eventUrl = eventUrl;
    }
}
