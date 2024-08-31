package com.cainfe.insomnia

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class InsomniaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        Log.v("InsomniaApplication", "Creating notification channel")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.keep_awake_channel_name)
            val descriptionText = getString(R.string.keep_awake_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = getString(R.string.keep_awake_channel_id)
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
            Log.v("InsomniaApplication", "Notification channel created")
        } else {
            Log.v("InsomniaApplication", "Notification channel not created")
        }
    }
}