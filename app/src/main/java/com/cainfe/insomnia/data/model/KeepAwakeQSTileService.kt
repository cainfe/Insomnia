package com.cainfe.insomnia.data.model

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class KeepAwakeQSTileService: TileService() {
    override fun onClick() {
        super.onClick()
        if (qsTile.state == Tile.STATE_ACTIVE) {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
        } else {
            qsTile.state = Tile.STATE_ACTIVE
            qsTile.updateTile()
        }
    }
}