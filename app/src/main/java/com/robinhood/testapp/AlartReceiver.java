package com.robinhood.testapp;

import android.widget.RemoteViews;
import androidx.annotation.NonNull;

import com.robinhood.wsc.notifications.BaseAlarmReceiver;

public class AlartReceiver extends BaseAlarmReceiver {

    @Override
    public int getSmallIconRes() {
        return R.drawable.ic_stat_info_outline;
    }

    @Override
    public int getSmallIconColor() {
        return R.color.orange;
    }

    @NonNull
    @Override
    public Class<?> getActivityOpen() {
        return MainActivity.class;
    }

    @NonNull
    @Override
    public RemoteViews getNotificationLayout(String text) {
        RemoteViews notificationLayout = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_collapsed);
        notificationLayout.setCharSequence(R.id.collapsed_notification_title, "setText", text);
        return notificationLayout;
    }
}
