package com.example.flexiwidget.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WifiWidgetRoundLabel : WifiWidgetBase(isRound = true, hasLabel = true)

class WifiWidgetRoundLabelReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WifiWidgetRoundLabel()
}
