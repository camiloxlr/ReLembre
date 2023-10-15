package com.example.relembre.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.relembre.ui.MainActivity
import com.example.relembre.R

private const val CHANNEL_ID = "medicine"
private const val NOTIFICATION_ID = 456

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("aquiContext: "+context)
        if (context != null) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                Intent("SHOW_NOTIFICATION_ACTION"),
                PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("ReLembre")
                .setContentText("Hora do remedinho")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

}
