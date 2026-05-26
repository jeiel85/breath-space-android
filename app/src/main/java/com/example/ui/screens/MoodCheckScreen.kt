package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CalmButton
import com.example.ui.components.MoodChip
import com.example.ui.components.SoftGradientBackground
import com.example.ui.theme.*

@Composable
fun MoodCheckScreen(
    onMoodSelected: (String) -> Unit,
    onSkip: () -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedMood by remember { mutableStateOf<String?>(null) }

    val moodOptions = listOf(
        BreathStrings.MOOD_OPTION_1,
        BreathStrings.MOOD_OPTION_2,
        BreathStrings.MOOD_OPTION_3,
        BreathStrings.MOOD_OPTION_4,
        BreathStrings.MOOD_OPTION_5
    )

    SoftGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiny back or skip area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.testTag("mood_skip_btn")
                ) {
                    Text(
                        text = BreathStrings.MOOD_SKIP,
                        color = MutedSlate,
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = BreathStrings.MOOD_CHECK_QUESTION,
                    color = WarmIvory,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("mood_question")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = BreathStrings.MOOD_CHECK_SUBTITLE,
                    color = MutedSlate,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .testTag("mood_subtitle")
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Mood Selection chips stacked beautifully
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    moodOptions.forEach { mood ->
                        MoodChip(
                            text = mood,
                            isSelected = selectedMood == mood,
                            onClick = {
                                selectedMood = mood
                                onMoodSelected(mood) // Directly proceed on select for friction-free UX!
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
