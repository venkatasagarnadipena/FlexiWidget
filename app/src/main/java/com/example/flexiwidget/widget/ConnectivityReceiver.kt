package com.example.flexiwidget.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Update all widgets to reflect the new system state
                WifiWidgetSquare().updateAll(context)
                WifiWidgetRound().updateAll(context)
                WifiWidgetSquareLabel().updateAll(context)
                WifiWidgetRoundLabel().updateAll(context)
                
                MobileDataWidgetSquare().updateAll(context)
                MobileDataWidgetRound().updateAll(context)
                MobileDataWidgetSquareLabel().updateAll(context)
                MobileDataWidgetRoundLabel().updateAll(context)
                
                AirplaneModeWidgetSquare().updateAll(context)
                AirplaneModeWidgetRound().updateAll(context)
                AirplaneModeWidgetSquareLabel().updateAll(context)
                AirplaneModeWidgetRoundLabel().updateAll(context)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
