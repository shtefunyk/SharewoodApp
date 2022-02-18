package com.robinhood.wsc.data;

import android.content.SharedPreferences;

public class Preferences {

    private SharedPreferences preferences;

    public static final String PREFS_NAME = "robin_prefs";
    public static final String PREFS_FIRST_LAUNCH = "PREFS_FIRST_LAUNCH";
    public static final String PREFS_URL = "PREFS_URL";
    public static final String PREFS_SAVE_LAST_URL = "PREFS_SAVE_LAST_URL";

    public static final String PREFS_NOTIFICATIONS_START_MINS = "PREFS_NOTIFICATION_START";
    public static final String PREFS_NOTIFICATIONS_INTERVAL_MINS = "PREFS_NOTIFICATION_INTERVAL";
    public static final String PREFS_NOTIFICATIONS_COUNT = "PREFS_NOTIFICATION_COUNT";
    public static final String PREFS_NOTIFICATIONS_TEXT = "PREFS_NOTIFICATION_TEXT";

    public Preferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveUrl(String url) {
        preferences.edit().putString(PREFS_URL, url).apply();
    }

    public String getUrl() {
        return preferences.getString(PREFS_URL, null);
    }

    public void setSaveLastUrl(boolean saveLastUrl) {
        preferences.edit().putBoolean(PREFS_SAVE_LAST_URL, saveLastUrl).apply();
    }

    public boolean getSaveLastUrl() {
        return preferences.getBoolean(PREFS_SAVE_LAST_URL, false);
    }

    public void saveNotification(String text, Integer start, Integer interval, Integer count) {
        preferences.edit().putString(PREFS_NOTIFICATIONS_TEXT, text).apply();
        preferences.edit().putInt(PREFS_NOTIFICATIONS_START_MINS, start).apply();
        preferences.edit().putInt(PREFS_NOTIFICATIONS_INTERVAL_MINS, interval).apply();
        preferences.edit().putInt(PREFS_NOTIFICATIONS_COUNT, count).apply();
    }

    public Integer getNotificationCount() {
        return preferences.getInt(PREFS_NOTIFICATIONS_COUNT, 0);
    }

    public void setNotificationsCountRemains(Integer count) {
        preferences.edit().putInt(PREFS_NOTIFICATIONS_COUNT, count).apply();
    }

    public String getNotificationText() {
        return preferences.getString(PREFS_NOTIFICATIONS_TEXT, null);
    }

    public Long getNotificationStartMins() {
        return preferences.getLong(PREFS_NOTIFICATIONS_START_MINS, 0);
    }

    public Long getNotificationIntervalMins() {
        return preferences.getLong(PREFS_NOTIFICATIONS_INTERVAL_MINS, 0);
    }
}
