package com.example.flexiwidget.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WifiWidgetSquareLabel : WifiWidgetBase(isRound = false, hasLabel = true)

class WifiWidgetSquareLabelReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WifiWidgetSquareLabel()
}
