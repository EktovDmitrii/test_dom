package com.custom.rgs_android_dom.ui.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.MainActivity
import kotlin.random.Random

object MSDNotificationManager {

    const val ACTION_NOTIFICATION = "ACTION_NOTIFICATION"

    private const val CHANNEL_ID = "MSD_PUSHES_CHANNEL_ID"
    private const val CHANNEL_NAME = "MSD_PUSHES_CHANNEL"

    fun notify(
        context: Context,
        title: String = "",
        message: String,
        content: Bundle,
    ) {

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtras(content)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(context, (Math.random() * 100).toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .build()


        manager.notify(notificationId, notification)
    }
}