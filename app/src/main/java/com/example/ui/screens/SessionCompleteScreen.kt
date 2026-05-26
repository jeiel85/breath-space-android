package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CalmButton
import com.example.ui.components.SoftGradientBackground
import com.example.ui.theme.*

@Composable
fun SessionCompleteScreen(
    durationSeconds: Int,
    onFinish: (String) -> Unit // returns the post-session mood to write into database
) {
    val defaultMood = stringResource(R.string.mood_after_option_1)
    var selectedMoodAfter by remember { mutableStateOf("") }
    if (selectedMoodAfter.isEmpty()) {
        selectedMoodAfter = defaultMood
    }

    val completionPhraseResId = remember {
        listOf(
            R.string.complete_title_1,
            R.string.complete_title_2,
            R.string.complete_title_3
        ).random()
    }
    val completionPhrase = stringResource(id = completionPhraseResId)

    // Gentle fade animation for the checkmark to expand
    val infiniteTransition = rememberInfiniteTransition(label = "accent_shimmer")
    val borderPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutBack),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border"
    )

    val moodAfterOptions = listOf(
        stringResource(R.string.mood_after_option_1),
        stringResource(R.string.mood_after_option_2),
        stringResource(R.string.mood_after_option_3),
        stringResource(R.string.mood_after_option_4)
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
            // Spacer
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // Success emblem with gentle outer pulse
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(SoftMint.copy(alpha = 0.1f))
                        .border(
                            (2.dp),
                            SoftMint.copy(alpha = 0.3f * borderPulse),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "심호흡 완료",
                        tint = SoftMint,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = completionPhrase,
                    color = WarmIvory,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .testTag("complete_title")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.complete_subtitle, durationSeconds),
                    color = MutedSlate,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("complete_subtitle")
                )
            }

            // Post-session lightweight Mood Check
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.mood_after_question),
                    color = SoftPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Row of compact clickable chips for light post-mood checklist
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moodAfterOptions.forEach { mood ->
                        val isSel = selectedMoodAfter == mood
                        val bgColor by animateColorAsState(
                            targetValue = if (isSel) SoftPurple.copy(alpha = 0.15f) else CardMidnight,
                            label = "btn"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isSel) SoftPurple else BorderMidnight,
                            label = "border"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSel) SoftPurple else WarmIvory,
                            label = "txt"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable { selectedMoodAfter = mood }
                                .testTag("mood_after_chip_$mood"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mood,
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Confirm & Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalmButton(
                    text = BreathStrings.BUTTON_GO_HOME,
                    onClick = { onFinish(selectedMoodAfter) },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .testTag("complete_home_btn")
                )
            }
        }
    }
}
