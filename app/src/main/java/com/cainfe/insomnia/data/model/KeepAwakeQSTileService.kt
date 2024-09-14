package com.cainfe.insomnia.data.model

import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cainfe.insomnia.R
import com.cainfe.insomnia.util.Constants

class KeepAwakeQSTileService: TileService() {

    data class StateModel(val enabled: Boolean, val label: String, val icon: Icon)

    companion object {
        private const val TAG = "KeepAwakeQSTileService"
    }

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d(TAG, "onTileAdded")
        updateTileState()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        updateTileState()
    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d(TAG, "onStartListening")
        updateTileState()
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d(TAG, "onStopListening")
    }

    override fun onClick() {
        super.onClick()
        if (qsTile.state == Tile.STATE_ACTIVE) {
            Log.d(TAG, "Stopping KeepAwakeService")
            startService(
                Intent(this, KeepAwakeService::class.java).setAction(
                    Constants.ACTION_STOP
                )
            )
        } else {
            Log.d(TAG, "Starting KeepAwakeService")
            startService(
                Intent(this, KeepAwakeService::class.java).setAction(
                    Constants.ACTION_START
                )
            )
        }
        updateTileState()
    }

    private fun updateTileState() {
        if (qsTile == null) return

        val state = getStateFromService()
        qsTile.label = state.label
        qsTile.contentDescription = qsTile.label
        qsTile.state = if (state.enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.icon = state.icon
        qsTile.updateTile()
        Log.d(TAG, "Tile updated")
    }

    private fun getStateFromService(): StateModel {
        val isRunning = KeepAwakeService.isRunning
        val label = if (isRunning) "Allow sleep" else "Keep awake"
        val icon = if (isRunning) Icon.createWithResource(this, R.drawable.keep_awake_icon) else Icon.createWithResource(this, R.drawable.keep_awake_icon)
        return StateModel(isRunning, label, icon)
    }
}