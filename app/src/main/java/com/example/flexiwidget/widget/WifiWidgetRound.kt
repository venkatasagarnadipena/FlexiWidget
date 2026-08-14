package com.example.flexiwidget.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WifiWidgetRound : WifiWidgetBase(isRound = true, hasLabel = false)

class WifiWidgetRoundReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WifiWidgetRound()
}
