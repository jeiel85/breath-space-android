package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CalmButton
import com.example.ui.components.GlassCard
import com.example.ui.components.SoftGradientBackground
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingsScreen(
    currentEnabled: Boolean,
    currentHour: Int,
    currentMinute: Int,
    currentFrequency: Int,
    onSaveSettings: (Boolean, Int, Int, Int) -> Unit,
    onSendTestNotification: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var enabled by remember { mutableStateOf(currentEnabled) }
    var hour by remember { mutableIntStateOf(currentHour) }
    var minute by remember { mutableIntStateOf(currentMinute) }
    var frequency by remember { mutableIntStateOf(currentFrequency) }

    SoftGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onSaveSettings(enabled, hour, minute, frequency)
                        onBack()
                    },
                    modifier = Modifier.testTag("reminder_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.button_exit),
                        tint = WarmIvory
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = BreathStrings.NOTIF_SETTINGS_TITLE,
                    color = WarmIvory,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Toggle Enable Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = BreathStrings.NOTIF_ENABLE,
                                color = WarmIvory,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.notif_enable_desc),
                                color = MutedSlate,
                                fontSize = 12.sp
                            )
                        }

                        Switch(
                            checked = enabled,
                            onCheckedChange = { enabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DeepMidnight,
                                checkedTrackColor = SoftPurple,
                                uncheckedThumbColor = MutedSlate,
                                uncheckedTrackColor = BorderMidnight
                            ),
                            modifier = Modifier.testTag("reminder_enable_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Custom tactile Time Selector (Slide/Increment dials) when enabled
                AnimatedVisibility(
                    visible = enabled,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = BreathStrings.NOTIF_TIME_SELECT,
                                color = SoftPurple,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Formatted Time output
                            val amPmText = if (hour < 12) {
                                if (java.util.Locale.getDefault().language == "ko") "오전" else "AM"
                            } else {
                                if (java.util.Locale.getDefault().language == "ko") "오후" else "PM"
                            }
                            val formattedHour = if (hour % 12 == 0) 12 else hour % 12
                            val timeFormattedString = if (java.util.Locale.getDefault().language == "ko") {
                                String.format("%s %02d시 %02d분", amPmText, formattedHour, minute)
                            } else {
                                String.format("%02d:%02d %s", formattedHour, minute, amPmText)
                            }
                            Text(
                                text = timeFormattedString,
                                color = WarmIvory,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraLight,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Tactile steppers for Hour and Minute
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Hour Setter
                                Column(modifier = Modifier.weight(1f)) {
                                    val hrLabel = if (java.util.Locale.getDefault().language == "ko") "시간 (시)" else "Hour"
                                    Text(hrLabel, color = MutedSlate, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CardMidnight)
                                            .border(1.dp, BorderMidnight, RoundedCornerShape(12.dp))
                                            .padding(4.dp)
                                    ) {
                                        IconButton(onClick = { hour = if (hour > 0) hour - 1 else 23 }) {
                                            Text("-", color = SoftPurple, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Text(text = String.format("%02d", hour), color = WarmIvory, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                        IconButton(onClick = { hour = (hour + 1) % 24 }) {
                                            Text("+", color = SoftPurple, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                // Minute Setter
                                Column(modifier = Modifier.weight(1f)) {
                                    val minLabel = if (java.util.Locale.getDefault().language == "ko") "분 (분)" else "Minute"
                                    Text(minLabel, color = MutedSlate, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CardMidnight)
                                            .border(1.dp, BorderMidnight, RoundedCornerShape(12.dp))
                                            .padding(4.dp)
                                    ) {
                                        IconButton(onClick = { minute = if (minute > 0) minute - 5 else 55 }) {
                                            Text("-", color = SoftPurple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Text(text = String.format("%02d", minute), color = WarmIvory, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                        IconButton(onClick = { minute = (minute + 5) % 60 }) {
                                            Text("+", color = SoftPurple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Daily Frequency Select
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = BreathStrings.NOTIF_FREQUENCY_SELECT,
                                color = SoftPurple,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val options = listOf(
                                    1 to BreathStrings.NOTIF_FREQ_1,
                                    2 to BreathStrings.NOTIF_FREQ_2,
                                    3 to BreathStrings.NOTIF_FREQ_3
                                )

                                options.forEach { (freq, label) ->
                                    val isSel = frequency == freq
                                    val bgColor by animateColorAsState(
                                        targetValue = if (isSel) SoftPurple.copy(alpha = 0.15f) else CardMidnight,
                                        label = "bg"
                                    )
                                    val borderColor by animateColorAsState(
                                        targetValue = if (isSel) SoftPurple else BorderMidnight,
                                        label = "border"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(bgColor)
                                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                            .clickable { frequency = freq }
                                            .testTag("freq_$freq"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label.replace("하루 ", ""),
                                            color = if (isSel) SoftPurple else WarmIvory,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Test Alert card
                CalmButton(
                    text = BreathStrings.BUTTON_NOTIF_TEST,
                    onClick = {
                        onSendTestNotification()
                        Toast.makeText(context, context.getString(R.string.test_notif_sent), Toast.LENGTH_SHORT).show()
                    },
                    containerColor = CardMidnight,
                    contentColor = SoftPurple,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderMidnight, RoundedCornerShape(18.dp))
                        .testTag("reminder_test_btn")
                )

                Spacer(modifier = Modifier.weight(1f))

                // Elegant Bottom CTA to save
                CalmButton(
                    text = stringResource(R.string.button_save_settings),
                    onClick = {
                        onSaveSettings(enabled, hour, minute, frequency)
                        Toast.makeText(context, context.getString(R.string.settings_saved_success), Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .testTag("reminder_save_btn")
                )
            }
        }
    }
}
