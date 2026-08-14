package com.example.flexiwidget.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WifiWidgetSquare : WifiWidgetBase(isRound = false, hasLabel = false)

class WifiWidgetSquareReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WifiWidgetSquare()
}
