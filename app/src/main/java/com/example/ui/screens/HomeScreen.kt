package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    completedTodayCount: Int,
    totalMinutes: Int,
    defaultSessionDurationSeconds: Int,
    onDurationSelected: (Int) -> Unit,
    onStartBreathing: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToInfo: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Minor breathing idle loop animation for the home orb to feel alive
    val infiniteTransition = rememberInfiniteTransition(label = "home_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 0.94f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Greeting matching local hour
    val greetingResId = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> R.string.greeting_morning
            in 12..17 -> R.string.greeting_day
            else -> R.string.greeting_night
        }
    }
    val greetingText = stringResource(id = greetingResId)

    SoftGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Elegant title/header options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = BreathStrings.APP_NAME,
                    color = WarmIvory,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.testTag("home_app_title")
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onNavigateToInfo,
                        modifier = Modifier.testTag("nav_info_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.medical_disclaimer_title),
                            tint = MutedSlate
                        )
                    }
                    IconButton(
                        onClick = onNavigateToReminders,
                        modifier = Modifier.testTag("nav_reminders_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.notif_settings_title),
                            tint = MutedSlate
                        )
                    }
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("nav_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_title),
                            tint = MutedSlate
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Atmospheric Header Message
                Text(
                    text = greetingText,
                    color = WarmIvory,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .testTag("home_greeting_text")
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Interactive Animated Resting Orb
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clickable(onClick = onStartBreathing),
                    contentAlignment = Alignment.Center
                ) {
                    BreathingOrb(
                        scale = pulseScale,
                        glowIntensity = pulseGlow,
                        phaseColor = SoftPurple
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Duration Selector
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.settings_meditation_time),
                        color = MutedSlate,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    SessionDurationSelector(
                        selectedDurationSeconds = defaultSessionDurationSeconds,
                        onDurationSelected = onDurationSelected
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Master Breathe Action Button
                CalmButton(
                    text = stringResource(R.string.onboarding_button_start),
                    onClick = onStartBreathing,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .testTag("home_start_btn")
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Beautiful Simple Stats Section
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = BreathStrings.STATS_HEADER,
                        color = MutedSlate,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Stat 1: Completed Today
                        QuietStatCard(
                            modifier = Modifier.weight(1f),
                            title = BreathStrings.STATS_COMPLETED_TODAY,
                            value = "$completedTodayCount ${BreathStrings.STATS_UNIT_SESSIONS}",
                            icon = {
                                Text("🧘", fontSize = 18.sp)
                            }
                        )

                        // Stat 2: Total relaxation minutes
                        val mins = totalMinutes / 60
                        val secs = totalMinutes % 60
                        val totalFormatted = if (mins > 0) {
                            String.format(BreathStrings.STATS_UNIT_MIN_SEC, mins, secs)
                        } else {
                            String.format(BreathStrings.STATS_UNIT_SEC, secs)
                        }
                        
                        QuietStatCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.stats_total_time),
                            value = totalFormatted,
                            icon = {
                                Text("⏱️", fontSize = 18.sp)
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
