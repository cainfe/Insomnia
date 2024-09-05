package com.cainfe.insomnia.data.model

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cainfe.insomnia.R
import com.cainfe.insomnia.util.Constants

class KeepAwakeService : Service() {
    private lateinit var wakeLock: PowerManager.WakeLock
    private val notificationId = 1

    companion object {
        private const val TAG = "KeepAwakeService"
    }

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, Constants.WAKELOCK_TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == Constants.ACTION_START) {
            acquireWakeLock()
            showNotification()
        } else if (intent?.action == Constants.ACTION_STOP) {
            releaseWakeLock()
            cancelNotification()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) {
            releaseWakeLock()
            cancelNotification()
        }
    }

    private fun acquireWakeLock() {
        if (!wakeLock.isHeld) {
            wakeLock.acquire()
            Log.v(TAG, "WakeLock acquired")
        }
    }

    private fun releaseWakeLock() {
        if (wakeLock.isHeld) {
            wakeLock.release()
            Log.v(TAG, "WakeLock released")
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.keep_awake_channel_id)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.keep_awake_icon)
            .setContentTitle(getString(R.string.keep_awake_notification_title))
            .setContentText(getString(R.string.keep_awake_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)

        val intent = Intent(this, KeepAwakeService::class.java).setAction(Constants.ACTION_STOP)
        val pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.addAction(R.drawable.ic_stop, getString(R.string.keep_awake_notification_action_stop), pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }

    private fun cancelNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}