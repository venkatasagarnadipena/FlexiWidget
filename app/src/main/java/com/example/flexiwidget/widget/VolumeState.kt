package com.example.flexiwidget.widget

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

object VolumeState {
    val KEY_VOLUME_PERCENTAGE = intPreferencesKey("volume_percentage")
    
    val definition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
}
