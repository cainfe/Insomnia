package com.cainfe.insomnia.data.model

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.cainfe.insomnia.util.Constants

class KeepAwakeQSTileService: TileService() {
    companion object {
        private const val TAG = "KeepAwakeQSTileService"
    }

    override fun onClick() {
        super.onClick()
        if (qsTile.state == Tile.STATE_ACTIVE) {
            Log.v(TAG, "Stopping KeepAwakeService")
            startService(
                Intent(this, KeepAwakeService::class.java).setAction(
                Constants.ACTION_STOP))
            qsTile.state = Tile.STATE_INACTIVE
        } else {
            Log.v(TAG, "Starting KeepAwakeService")
            startService(
                Intent(this, KeepAwakeService::class.java).setAction(
                Constants.ACTION_START))
            qsTile.state = Tile.STATE_ACTIVE
        }
        qsTile.updateTile()
    }
}