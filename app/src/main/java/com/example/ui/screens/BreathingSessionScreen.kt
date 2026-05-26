package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BreathingOrb
import com.example.ui.components.BreathingPhaseText
import com.example.ui.components.CalmButton
import com.example.ui.components.SoftGradientBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BreathingSessionScreen(
    sessionDurationSeconds: Int,
    onSessionCompleted: () -> Unit,
    onBackToHome: () -> Unit
) {
    var isPaused by remember { mutableStateOf(false) }
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    var remainingMillis by remember { mutableLongStateOf(sessionDurationSeconds * 1000L) }

    // Smooth ticker update
    LaunchedEffect(key1 = isPaused) {
        if (!isPaused) {
            val period = 16L
            while (remainingMillis > 0) {
                delay(period)
                elapsedMillis += period
                remainingMillis = maxOf(0L, (sessionDurationSeconds * 1000L) - elapsedMillis)
            }
            onSessionCompleted()
        }
    }

    val cycleDuration = 12000L // 12 seconds total per breath cycle
    val currentCycleTime = elapsedMillis % cycleDuration

    // 1. Determine active phase characteristics
    val (phaseText, phaseColor, targetScale) = when {
        currentCycleTime < 4000L -> {
            // Inhale: 4 seconds (scale 0.6 -> 1.0)
            val fraction = currentCycleTime.toFloat() / 4000L
            val scale = 0.6f + (0.4f * fraction)
            Triple(BreathStrings.PHASE_INHALE, SoftMint, scale)
        }
        currentCycleTime < 6000L -> {
            // Hold: 2 seconds (scale stays 1.0)
            Triple(BreathStrings.PHASE_HOLD, SoftPurple, 1.0f)
        }
        else -> {
            // Exhale: 6 seconds (scale 1.0 -> 0.6)
            val fractionExhale = (currentCycleTime - 6000L).toFloat() / 6000L
            val scale = 1.0f - (0.4f * fractionExhale)
            Triple(BreathStrings.PHASE_EXHALE, WarmPink, scale)
        }
    }

    // Cinematic scaling dampener
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow),
        label = "orbital_scale"
    )

    // Gentle pulse glow
    val animatedGlow by animateFloatAsState(
        targetValue = if (currentCycleTime in 4000L..6000L) 1.0f else 0.75f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "orbital_glow"
    )

    SoftGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Simple quiet top bar with direct escape button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onBackToHome,
                    modifier = Modifier.testTag("session_exit_icon")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "심호흡 그만하기",
                        tint = MutedSlate
                    )
                }
            }

            // Quiet unobtrusive Timer count
            val remainingSeconds = (remainingMillis + 999) / 1000
            val minutesLeft = remainingSeconds / 60
            val secondsLeft = remainingSeconds % 60
            val timeString = String.format("%d:%02d", minutesLeft, secondsLeft)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = timeString,
                    color = MutedSlate.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 32.sp
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // High fidelity animated Breathing Orb
                BreathingOrb(
                    scale = animatedScale,
                    glowIntensity = animatedGlow,
                    phaseColor = phaseColor,
                    modifier = Modifier.testTag("breathing_orb")
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Active instructions with beautiful transitional colors
                BreathingPhaseText(
                    phaseText = phaseText,
                    comfortText = if (currentCycleTime >= 6000L) stringResource(R.string.phase_comfort_exhale) else BreathStrings.PHASE_COMFORT,
                    phaseColor = phaseColor
                )
            }

            // Quiet interactive control buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalmButton(
                    text = if (isPaused) stringResource(R.string.button_resume) else stringResource(R.string.button_pause),
                    onClick = { isPaused = !isPaused },
                    containerColor = if (isPaused) SoftPurple else CardMidnight,
                    contentColor = if (isPaused) DeepMidnight else WarmIvory,
                    modifier = Modifier.testTag("session_pause_resume_btn")
                )
                
                Spacer(modifier = Modifier.width(16.dp))

                CalmButton(
                    text = stringResource(R.string.button_exit),
                    onClick = onBackToHome,
                    containerColor = Color.Transparent,
                    contentColor = MutedSlate,
                    modifier = Modifier.testTag("session_exit_btn")
                )
            }
        }
    }
}
