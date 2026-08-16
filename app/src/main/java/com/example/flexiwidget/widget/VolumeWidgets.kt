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
                    .background(GlanceTheme.colors.surfaceVariant)
                    .cornerRadius(22.dp)
            ) {
                // PERCENT-PERFECT PROPORTIONAL FILL (Vertical)
                // We use weights 0-100 to map every single percentage point to physical pixels
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    val filledWeight = percentage.toFloat().coerceAtLeast(0.01f)
                    val emptyWeight = (100f - percentage).coerceAtLeast(0.01f)
                    
                    // Empty space at the top
                    Spacer(modifier = GlanceModifier.defaultWeight().fillMaxHeight())
                    // Resetting weights using a clever trick: nested weights or splitting segments
                    // Since Glance only supports 1:1 weights with defaultWeight(), 
                    // we use a large enough number of segments to simulate a smooth bar.
                    for (i in 0 until 100) {
                        val isFilled = (99 - i) < percentage
                        Box(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .defaultWeight()
                                .background(if (isFilled) GlanceTheme.colors.primary else GlanceTheme.colors.surfaceVariant)
                        ) {}
                    }
                }

                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_add),
                        contentDescription = "Up",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to 1))),
                        colorFilter = ColorFilter.tint(if (percentage > 85) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Vol",
                            style = TextStyle(fontSize = 9.sp, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$percentage%",
                            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (percentage > 45) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Image(
                        provider = ImageProvider(R.drawable.ic_remove),
                        contentDescription = "Down",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to -1))),
                        colorFilter = ColorFilter.tint(if (percentage > 15) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
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
                    .background(GlanceTheme.colors.surfaceVariant)
                    .cornerRadius(22.dp)
            ) {
                // PERCENT-PERFECT PROPORTIONAL FILL (Horizontal)
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    for (i in 0 until 100) {
                        val isFilled = i < percentage
                        Box(
                            modifier = GlanceModifier
                                .fillMaxHeight()
                                .defaultWeight()
                                .background(if (isFilled) GlanceTheme.colors.primary else GlanceTheme.colors.surfaceVariant)
                        ) {}
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
                        colorFilter = ColorFilter.tint(if (percentage > 15) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Volume",
                            style = TextStyle(fontSize = 10.sp, color = if (percentage > 50) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$percentage%",
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (percentage > 50) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
                        )
                    }
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Image(
                        provider = ImageProvider(R.drawable.ic_add),
                        contentDescription = "Up",
                        modifier = GlanceModifier.size(20.dp).clickable(actionRunCallback<VolumeAdjustAction>(actionParametersOf(VolumeAdjustAction.KEY_DIRECTION to 1))),
                        colorFilter = ColorFilter.tint(if (percentage > 85) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant)
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
