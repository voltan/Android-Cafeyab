package com.faragostaresh.app;

/**
 * Created by Ravi Tamada on 28/09/16.
 * www.androidhive.info
 */

public class Config {

    // global topic to receive app wide push notifications
    public static final String FB_TOPIC_GLOBAL = "global";
    public static final String FB_TOPIC_CAFE = "cafe";
    public static final String FB_TOPIC_VIDEO = "video";
    public static final String FB_TOPIC_EVENT = "event";
    public static final String FB_TOPIC_NEWS = "news";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "cafeyab_firebase";

    // Set url
    public static final String URL_WEBSITE          = "https://www.cafeyab.com";
    public static final String URL_CAFE_LIST        = "https://www.cafeyab.com/guide/json/search?limit=10&page=";
    public static final String URL_CAFE_RECOMMENDED = "https://www.cafeyab.com/guide/json/search?limit=15&page=1&recommended=1";
    public static final String URL_CAFE_SINGLE      = "https://www.cafeyab.com/guide/json/itemSingle/id/";
    public static final String URL_CAFE_MAPS        = "https://www.cafeyab.com/guide/json/itemMap";
    public static final String URL_EVENT_LIST       = "https://www.cafeyab.com/event/json/search?limit=10&page=";
    public static final String URL_EVENT_SINGLE     = "https://www.cafeyab.com/event/json/eventSingle/id/";
    public static final String URL_VIDEO_LIST       = "https://www.cafeyab.com/video/json/search?limit=10&page=";
    public static final String URL_VIDEO_SINGLE     = "https://www.cafeyab.com/video/json/videoSingle/id/";
    public static final String URL_NEWS_LIST        = "https://www.cafeyab.com/news/json/search?limit=10&page=";
    public static final String URL_NEWS_SINGLE      = "https://www.cafeyab.com/news/json/storySingle/id/";

    public static final String URL_CHECK            = "https://www.cafeyab.com/usmartphone/check";
    public static final String URL_LOGIN            = "https://www.cafeyab.com/usmartphone/login";
    public static final String URL_LOGOUT           = "https://www.cafeyab.com/usmartphone/logout";
    public static final String URL_REGISTER         = "https://www.cafeyab.com/user/register";
    public static final String URL_IMAGE_MAIN       = "https://www.cafeyab.com/upload/app/android/cover.jpg";

}
