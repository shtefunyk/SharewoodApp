package com.robinhood.wsc.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.robinhood.wsc.R
import com.robinhood.wsc.data.Preferences

abstract class BaseAlarmReceiver : BroadcastReceiver() {

    private val NOTIFICATION_ID = "10010"

    abstract fun getStartActivity() : Class<*>
    abstract fun getPackageName() : String
    abstract fun getNotificationIcon() : Int

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = Preferences(context.getSharedPreferences(Preferences.PREFS_NAME, Context.MODE_PRIVATE))
        if(preferences.notificationCount <= 0) return

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(NOTIFICATION_ID, "Robin", importance)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)
        }
        val resultIntent = Intent(context, getStartActivity())
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_stat_info_outline)
            .setColor(ContextCompat.getColor(context, R.color.notification))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(getNotificationLayout(preferences.notificationText))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) { notify(717, notificationBuilder.build()) }
        preferences.setNotificationsCountRemains(preferences.notificationCount - 1)
    }

    private fun getNotificationLayout(text: String) : RemoteViews {
        val notificationLayout = RemoteViews(getPackageName(), R.layout.notification_collapsed)
        notificationLayout.setImageViewResource(R.id.collapsed_notification_icon, getNotificationIcon())
        notificationLayout.setCharSequence(R.id.collapsed_notification_title, "setText", text)
        return notificationLayout
    }
}