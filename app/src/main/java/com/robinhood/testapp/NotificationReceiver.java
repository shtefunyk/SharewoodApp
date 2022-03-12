package com.robinhood.testapp;

import androidx.annotation.NonNull;
import com.robinhood.wsc.notifications.BaseAlarmReceiver;

public class NotificationReceiver extends BaseAlarmReceiver {

    @NonNull
    @Override
    public Class<?> getStartActivity() {
        return RobinHoodStartActivity.class;
    }

    @NonNull
    @Override
    public String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    public int getNotificationIcon() {
        return R.mipmap.ic_launcher_round;
    }
}
