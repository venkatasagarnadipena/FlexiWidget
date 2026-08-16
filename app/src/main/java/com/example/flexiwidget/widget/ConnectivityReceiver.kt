package com.example.flexiwidget.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        // Use IO dispatcher for widget updates to avoid blocking main thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Parallel Update: Refresh every widget variant in the project
                
                // WiFi Suite
                WifiWidgetSquare().updateAll(context)
                WifiWidgetRound().updateAll(context)
                WifiWidgetSquareLabel().updateAll(context)
                WifiWidgetRoundLabel().updateAll(context)
                
                // Mobile Data Suite
                MobileDataWidgetSquare().updateAll(context)
                MobileDataWidgetRound().updateAll(context)
                MobileDataWidgetSquareLabel().updateAll(context)
                MobileDataWidgetRoundLabel().updateAll(context)
                
                // Airplane Mode Suite
                AirplaneModeWidgetSquare().updateAll(context)
                AirplaneModeWidgetRound().updateAll(context)
                AirplaneModeWidgetSquareLabel().updateAll(context)
                AirplaneModeWidgetRoundLabel().updateAll(context)

                // Volume Suite (The 1x2 and 2x1 pills)
                VolumeWidget1x2().updateAll(context)
                VolumeWidget2x1().updateAll(context)

            } finally {
                pendingResult.finish()
            }
        }
    }
}
