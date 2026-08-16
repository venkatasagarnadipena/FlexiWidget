package com.example.flexiwidget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.flexiwidget.widget.*

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        }

        setContent {
            val context = LocalContext.current

            FlexiWidgetTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }

                    Box(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
                        when (currentScreen) {
                            "home" -> HomeScreen(onDailyUtilitiesClick = { currentScreen = "gallery" })
                            "gallery" -> WidgetGridScreen(
                                context = context,
                                onBack = { currentScreen = "home" }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onDailyUtilitiesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        HeaderTitle(text = "FlexiWidget")
        
        Spacer(modifier = Modifier.height(32.dp))

        // Dashboard Item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20), Color(0xFF000000))
                    )
                )
                .clickable { onDailyUtilitiesClick() }
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Daily Utilities",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun WidgetGridScreen(context: Context, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Logo and Title Row that acts as a back button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBack() }
        ) {
            HeaderTitle(text = "Daily Utilities")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "1X1",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // WiFi Row
            item { MiniWidgetPreview("", R.drawable.ic_wifi, false) { pinWidget(context, WifiWidgetSquareReceiver::class.java) } }
            item { MiniWidgetPreview("", R.drawable.ic_wifi, true) { pinWidget(context, WifiWidgetRoundReceiver::class.java) } }
            item { MiniWidgetPreview("Wifi", R.drawable.ic_wifi, false) { pinWidget(context, WifiWidgetSquareLabelReceiver::class.java) } }
            item { MiniWidgetPreview("Wifi", R.drawable.ic_wifi, true) { pinWidget(context, WifiWidgetRoundLabelReceiver::class.java) } }

            // Spacing
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Mobile Data Row
            item { MiniWidgetPreview("", R.drawable.ic_mobile_data, false) { pinWidget(context, MobileDataWidgetSquareReceiver::class.java) } }
            item { MiniWidgetPreview("", R.drawable.ic_mobile_data, true) { pinWidget(context, MobileDataWidgetRoundReceiver::class.java) } }
            item { MiniWidgetPreview("Data", R.drawable.ic_mobile_data, false) { pinWidget(context, MobileDataWidgetSquareLabelReceiver::class.java) } }
            item { MiniWidgetPreview("Data", R.drawable.ic_mobile_data, true) { pinWidget(context, MobileDataWidgetRoundLabelReceiver::class.java) } }

            // Spacing
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Airplane Mode Row
            item { MiniWidgetPreview("", R.drawable.ic_airplane_mode, false) { pinWidget(context, AirplaneModeWidgetSquareReceiver::class.java) } }
            item { MiniWidgetPreview("", R.drawable.ic_airplane_mode, true) { pinWidget(context, AirplaneModeWidgetRoundReceiver::class.java) } }
            item { MiniWidgetPreview("Flight", R.drawable.ic_airplane_mode, false) { pinWidget(context, AirplaneModeWidgetSquareLabelReceiver::class.java) } }
            item { MiniWidgetPreview("Flight", R.drawable.ic_airplane_mode, true) { pinWidget(context, AirplaneModeWidgetRoundLabelReceiver::class.java) } }

            // Spacing
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Lock Row
            item { MiniWidgetPreview("", R.drawable.ic_lock, false) { pinWidget(context, LockWidgetSquareReceiver::class.java) } }
            item { MiniWidgetPreview("", R.drawable.ic_lock, true) { pinWidget(context, LockWidgetRoundReceiver::class.java) } }
            item { MiniWidgetPreview("Lock", R.drawable.ic_lock, false) { pinWidget(context, LockWidgetSquareLabelReceiver::class.java) } }
            item { MiniWidgetPreview("Lock", R.drawable.ic_lock, true) { pinWidget(context, LockWidgetRoundLabelReceiver::class.java) } }

            // Volume Section Header
            item(span = { GridItemSpan(4) }) {
                Text(
                    text = "Volume Control",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }

            // Volume Row
            item(span = { GridItemSpan(2) }) {
                VolumeWidgetPreview("1x2", isVertical = true) {
                    pinWidget(context, VolumeWidget1x2Receiver::class.java)
                }
            }
            item(span = { GridItemSpan(2) }) {
                VolumeWidgetPreview("2x1", isVertical = false) {
                    pinWidget(context, VolumeWidget2x1Receiver::class.java)
                }
            }
        }
    }
}

@Composable
fun VolumeWidgetPreview(name: String, isVertical: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isVertical) Modifier.size(width = 64.dp, height = 120.dp)
                    else Modifier.size(width = 120.dp, height = 64.dp)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Background Fill (simulated 75%)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isVertical) Modifier.fillMaxHeight(0.75f).align(Alignment.BottomCenter)
                        else Modifier.fillMaxWidth(0.75f).align(Alignment.CenterStart)
                    )
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )

            if (isVertical) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize().padding(vertical = 12.dp)
                ) {
                    Icon(painterResource(R.drawable.ic_add), null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Volume", fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("75%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Icon(painterResource(R.drawable.ic_remove), null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)
                ) {
                    Icon(painterResource(R.drawable.ic_remove), null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Volume", fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("75%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Icon(painterResource(R.drawable.ic_add), null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(20.dp))
                }
            }
        }
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun pinWidget(context: Context, receiverClass: Class<*>) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myProvider = ComponentName(context, receiverClass)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        appWidgetManager.requestPinAppWidget(myProvider, null, null)
    }
}

@Composable
fun HeaderTitle(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = buildAnnotatedString {
                append(text)
                withStyle(SpanStyle(color = Color(0xFF4CAF50))) {
                    append(".")
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun MiniWidgetPreview(name: String, iconRes: Int, isRound: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(if (isRound) CircleShape else RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
                if (name.isNotEmpty()) {
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FlexiWidgetTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    
    val colorScheme = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) darkColorScheme(primary = Color(0xFF33691E)) else lightColorScheme(primary = Color(0xFF33691E))
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
