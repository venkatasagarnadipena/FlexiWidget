package com.example.flexiwidget.widget

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.flexiwidget.R

abstract class WifiWidgetBase(
    private val isRound: Boolean,
    private val hasLabel: Boolean
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val isEnabled = wifiManager.isWifiEnabled
        
        provideContent {
            GlanceTheme {
                Content(isEnabled)
            }
        }
    }

    @Composable
    private fun Content(isEnabled: Boolean) {
        val backgroundProvider = if (isEnabled) {
            GlanceTheme.colors.primaryContainer
        } else {
            GlanceTheme.colors.surfaceVariant
        }
        
        val contentColorProvider = if (isEnabled) {
            GlanceTheme.colors.onPrimaryContainer
        } else {
            GlanceTheme.colors.onSurfaceVariant
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundProvider)
                .cornerRadius(if (isRound) 50.dp else 16.dp)
                .clickable(actionRunCallback<WifiToggleAction>()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_wifi),
                    contentDescription = "WiFi Toggle",
                    modifier = GlanceModifier.size(if (hasLabel) 28.dp else 36.dp),
                    colorFilter = ColorFilter.tint(contentColorProvider)
                )
                if (hasLabel) {
                    Spacer(modifier = GlanceModifier.height(0.dp))
                    Text(
                        text = "Wifi",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = contentColorProvider
                        )
                    )
                }
            }
        }
    }
}
