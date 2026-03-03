package com.aodapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {

            Intent.ACTION_SCREEN_OFF -> {
                AODService.showOverlay(context)
            }

            Intent.ACTION_SCREEN_ON -> {
                AODService.hideOverlay()
            }
        }
    }
}
