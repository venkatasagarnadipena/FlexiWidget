package com.example.flexiwidget.widget

import android.content.Context
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
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.flexiwidget.R

abstract class LockWidgetBase(
    private val isRound: Boolean,
    private val hasLabel: Boolean
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.primaryContainer)
                .cornerRadius(if (isRound) 50.dp else 16.dp)
                .clickable(actionRunCallback<LockScreenAction>()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_lock),
                    contentDescription = "Lock Screen",
                    modifier = GlanceModifier.size(if (hasLabel) 28.dp else 36.dp),
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onPrimaryContainer)
                )
                if (hasLabel) {
                    Spacer(modifier = GlanceModifier.height(0.dp))
                    Text(
                        text = "Lock",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}

class LockWidgetSquare : LockWidgetBase(isRound = false, hasLabel = false)
class LockWidgetSquareReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidgetSquare()
}

class LockWidgetRound : LockWidgetBase(isRound = true, hasLabel = false)
class LockWidgetRoundReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidgetRound()
}

class LockWidgetSquareLabel : LockWidgetBase(isRound = false, hasLabel = true)
class LockWidgetSquareLabelReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidgetSquareLabel()
}

class LockWidgetRoundLabel : LockWidgetBase(isRound = true, hasLabel = true)
class LockWidgetRoundLabelReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidgetRoundLabel()
}
