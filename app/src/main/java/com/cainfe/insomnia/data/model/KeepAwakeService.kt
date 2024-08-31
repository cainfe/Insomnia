package com.cainfe.insomnia.data.model

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.cainfe.insomnia.util.Constants

class KeepAwakeService : Service() {
    private val TAG = "KeepAwakeService"
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.WAKELOCK_TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == Constants.ACTION_START) {
            wakeLock.acquire()
            Log.v(TAG, "WakeLock acquired")
        } else if (intent?.action == Constants.ACTION_STOP) {
            wakeLock.release()
            Log.v(TAG, "WakeLock released")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock.release()
    }
}