package com.example.flexiwidget.widget

import android.content.Context
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.datastore.preferences.core.Preferences
import com.example.flexiwidget.R

abstract class VolumeWidgetBase : GlanceAppWidget() {
    
    override val stateDefinition: GlanceStateDefinition<*> = VolumeState.definition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val savedPercentage = prefs[VolumeState.KEY_VOLUME_PERCENTAGE]
            
            val percentage = if (savedPercentage == null) {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                if (max > 0) (current * 100 / max) else 0
            } else {
                savedPercentage
            }

            GlanceTheme {
                Content(percentage)
            }
        }
    }

    @Composable
    abstract fun Content(percentage: Int)
}

class VolumeWidget1x2 : VolumeWidgetBase() {
    @Composable
    override fun Content(percentage: Int) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = GlanceModifier
                    .fillMaxHeight()
                    .width(44.dp)
                    .background(GlanceTheme.colors.onSurfaceVariant)
                    .cornerRadius(22.dp)
            ) {
                // TRUE PERCENTAGE FILL (Vertical)
                // We use weights to divide the space into exactly 100 parts
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    val emptyWeight = (100 - percentage).toFloat().coerceAtLeast(0.01f)
                    val filledWeight = percentage.toFloat().coerceAtLeast(0.01f)
                    
                    // Empty space (top)
                    Box(modifier = GlanceModifier.fillMaxWidth().defaultWeight().fillMaxHeight()) {
                        // This Spacer-like box takes up the 'empty' portion
                        // Since Glance defaultWeight is 1, we use a loop to create segments
                        // mapping to 1% accuracy.
                        Column(modifier = GlanceModifier.fillMaxSize()) {
                            for(i in 0 until (100-percentage)) {
                                Box(modifier = GlanceModifier.fillMaxWidth().defaultWeight()) {}
                            }
                            for(i in 0 until percentage) {
                                Box(modifier = GlanceModifier.fillMaxWidth().defaultWeight().background(GlanceTheme.colors.primary)) {}
                            }
                        }
                    }
                }

                // Controls Overlay
                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_add),
                        contentDescription = "Up",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to 1))),
                        colorFilter = ColorFilter.tint(if (percentage > 85) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Vol",
                            style = TextStyle(fontSize = 9.sp, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$percentage%",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Image(
                        provider = ImageProvider(R.drawable.ic_remove),
                        contentDescription = "Down",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to -1))),
                        colorFilter = ColorFilter.tint(if (percentage > 15) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                }
            }
        }
    }
}

class VolumeWidget2x1 : VolumeWidgetBase() {
    @Composable
    override fun Content(percentage: Int) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(GlanceTheme.colors.onSurfaceVariant)
                    .cornerRadius(22.dp)
            ) {
                // TRUE PERCENTAGE FILL (Horizontal)
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    for(i in 0 until percentage) {
                        Box(modifier = GlanceModifier.fillMaxHeight().defaultWeight().background(GlanceTheme.colors.primary)) {}
                    }
                    for(i in 0 until (100-percentage)) {
                        Box(modifier = GlanceModifier.fillMaxHeight().defaultWeight()) {}
                    }
                }

                Row(
                    modifier = GlanceModifier.fillMaxSize().padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_remove),
                        contentDescription = "Down",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to -1))),
                        colorFilter = ColorFilter.tint(if (percentage > 15) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Volume",
                            style = TextStyle(fontSize = 10.sp, color = if (percentage > 50) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$percentage%",
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (percentage > 50) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Image(
                        provider = ImageProvider(R.drawable.ic_add),
                        contentDescription = "Up",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to 1))),
                        colorFilter = ColorFilter.tint(if (percentage > 85) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                }
            }
        }
    }
}

class VolumeWidget1x4 : VolumeWidgetBase() {
    @Composable
    override fun Content(percentage: Int) {
        Box(
            modifier = GlanceModifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = GlanceModifier
                    .fillMaxHeight()
                    .width(44.dp)
                    .background(GlanceTheme.colors.onSurfaceVariant)
                    .cornerRadius(22.dp)
            ) {
                // TRUE PERCENTAGE FILL (Vertical 1x4)
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    for(i in 0 until (100-percentage)) {
                        Box(modifier = GlanceModifier.fillMaxWidth().defaultWeight()) {}
                    }
                    for(i in 0 until percentage) {
                        Box(modifier = GlanceModifier.fillMaxWidth().defaultWeight().background(GlanceTheme.colors.primary)) {}
                    }
                }

                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_add),
                        contentDescription = "Up",
                        modifier = GlanceModifier.size(24.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to 1))),
                        colorFilter = ColorFilter.tint(if (percentage > 85) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Vol",
                            style = TextStyle(fontSize = 10.sp, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$percentage%",
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Image(
                        provider = ImageProvider(R.drawable.ic_remove),
                        contentDescription = "Down",
                        modifier = GlanceModifier.size(24.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to -1))),
                        colorFilter = ColorFilter.tint(if (percentage > 15) GlanceTheme.colors.onPrimary else GlanceTheme.colors.surface)
                    )
                }
            }
        }
    }
}

class VolumeWidget1x2Receiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VolumeWidget1x2()
}

class VolumeWidget2x1Receiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VolumeWidget2x1()
}

class VolumeWidget1x4Receiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VolumeWidget1x4()
}
