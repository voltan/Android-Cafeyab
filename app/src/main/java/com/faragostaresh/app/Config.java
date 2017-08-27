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


    public static final String TOKEN = "lovquDGv5iyOc8AHdMZ";

    // Set url
    public static final String URL_APP_URL          = "https://play.google.com/store/apps/details?id=com.faragostaresh.cafeyab";
    public static final String URL_FARAGOSTARESH    = "https://www.faragostaresh.com";
    public static final String URL_WEBSITE          = "https://www.cafeyab.com";

    public static final String URL_REGISTER         = URL_WEBSITE + "/user/register";
    public static final String URL_IMAGE_MAIN       = URL_WEBSITE + "/upload/app/android/cover.jpg";
    public static final String URL_APP_LOGO         = URL_WEBSITE + "/upload/app/android/logo.png";

    public static final String URL_CAFE_LIST        = URL_WEBSITE + "/apis/guide/itemList?token=" + TOKEN + "&platform=android&limit=10&page=";
    public static final String URL_CAFE_SINGLE      = URL_WEBSITE + "/apis/guide/itemSingle?token=" + TOKEN + "&platform=android&id=";
    public static final String URL_CAFE_MAPS        = URL_WEBSITE + "/apis/guide/map?token=" + TOKEN + "&platform=android";

    public static final String URL_EVENT_LIST       = URL_WEBSITE + "/event/json/search?limit=10&page=";
    public static final String URL_EVENT_SINGLE     = URL_WEBSITE + "/event/json/eventSingle/id/";

    public static final String URL_VIDEO_LIST       = URL_WEBSITE + "/video/json/search?limit=10&page=";
    public static final String URL_VIDEO_SINGLE     = URL_WEBSITE + "/video/json/videoSingle/id/";

    public static final String URL_NEWS_LIST        = URL_WEBSITE + "/news/json/search?limit=10&page=";
    public static final String URL_NEWS_SINGLE      = URL_WEBSITE + "/news/json/storySingle/id/";

    public static final String URL_PROFILE          = URL_WEBSITE + "/apis/user/profile?token=" + TOKEN + "&platform=android";
    public static final String URL_CHECK            = URL_WEBSITE + "/apis/user/check?token=" + TOKEN + "&platform=android";
    public static final String URL_LOGIN            = URL_WEBSITE + "/apis/user/login?token=" + TOKEN + "&platform=android";
    public static final String URL_LOGOUT           = URL_WEBSITE + "/apis/user/logout?token=" + TOKEN + "&platform=android";
}
