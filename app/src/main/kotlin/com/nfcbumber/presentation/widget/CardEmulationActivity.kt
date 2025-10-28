package com.nfcbumber.presentation.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nfcbumber.data.nfc.NfcEmulationManager
import com.nfcbumber.data.security.SecureStorage
import com.nfcbumber.presentation.theme.WolleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fullscreen transparent activity that displays a single NFC card
 * for quick emulation from a widget.
 * Activates NFC emulation while the activity is visible.
 */
@AndroidEntryPoint
class CardEmulationActivity : ComponentActivity() {

    @Inject
    lateinit var emulationManager: NfcEmulationManager

    private var currentCardId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get card ID and widget ID from intent
        val cardId = intent?.getLongExtra("CARD_ID", -1L) ?: -1L
        val widgetId = intent?.getIntExtra("WIDGET_ID", -1) ?: -1

        if (cardId == -1L || widgetId == -1) {
            finish()
            return
        }

        currentCardId = cardId

        // Activate NFC emulation for this card
        emulationManager.activateEmulation(cardId)

        // Load card data from secure storage using widget ID
        val secureStorage = SecureStorage(this)
        val cardName = secureStorage.getString("widget_card_name_$widgetId", "Unknown Card") ?: "Unknown Card"
        val cardType = secureStorage.getString("widget_card_type_$widgetId", "NFC Card") ?: "NFC Card"
        val cardColor = secureStorage.getInt("widget_card_color_$widgetId", 0xFF2196F3.toInt())

        setContent {
            WolleTheme {
                CardEmulationScreen(
                    cardName = cardName,
                    cardType = cardType,
                    cardColor = cardColor,
                    onDismiss = { finish() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-activate emulation when returning to the activity
        if (currentCardId != -1L) {
            emulationManager.activateEmulation(currentCardId)
        }
    }

    override fun onPause() {
        super.onPause()
        // Deactivate emulation when activity goes to background
        emulationManager.deactivateEmulation()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure emulation is deactivated when activity is destroyed
        emulationManager.deactivateEmulation()
    }
}

@Composable
fun CardEmulationScreen(
    cardName: String,
    cardType: String,
    cardColor: Int,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        visible = true
    }

    val handleDismiss: () -> Unit = {
        visible = false
        coroutineScope.launch {
            delay(300)
            onDismiss()
        }
        Unit
    }

    // Animations
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (visible) 0.9f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "background_alpha"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    val cardAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 100),
        label = "card_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = backgroundAlpha))
            .clickable(
                onClick = handleDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Card Display
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1.6f)
                .scale(cardScale)
                .alpha(cardAlpha)
                .clickable(
                    onClick = { /* Prevent click propagation */ },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(cardColor)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                // Close button
                IconButton(
                    onClick = handleDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Card content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Card Name
                    Text(
                        text = cardName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card Type
                    Text(
                        text = cardType,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // NFC Status indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Animated NFC icon
                        NFCPulseIndicator()

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Ready to Tap",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // Bottom hint
                Text(
                    text = "Tap outside to dismiss",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun NFCPulseIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "nfc_pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulse ring
        Surface(
            modifier = Modifier
                .size(40.dp)
                .scale(scale)
                .alpha(alpha * 0.3f),
            shape = RoundedCornerShape(50),
            color = Color.White
        ) {}

        // Inner icon
        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(50),
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "📱",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

