package com.example.flexiwidget.widget

import android.content.Context
import android.media.AudioManager
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll

class VolumeAdjustAction : ActionCallback {
    companion object {
        val KEY_DIRECTION = ActionParameters.Key<Int>("adjust_direction")
        private const val VOLUME_STEP_PERCENT = 7 // 1/15th of 100% roughly matches Android steps
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val direction = parameters[KEY_DIRECTION] ?: 0
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        
        // 1. Perform Hardware Volume Action
        val adjustType = if (direction > 0) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            adjustType,
            AudioManager.FLAG_SHOW_UI
        )
        
        // 2. Direct Widget State Update (No system volume check)
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentLevel = prefs[VolumeState.KEY_VOLUME_PERCENTAGE] ?: 50 // Default to 50 if empty
            val newLevel = if (direction > 0) {
                (currentLevel + VOLUME_STEP_PERCENT).coerceAtMost(100)
            } else {
                (currentLevel - VOLUME_STEP_PERCENT).coerceAtLeast(0)
            }
            prefs[VolumeState.KEY_VOLUME_PERCENTAGE] = newLevel
        }
        
        // 3. Trigger Instant Refresh
        VolumeWidget1x2().updateAll(context)
        VolumeWidget2x1().updateAll(context)
    }
}
