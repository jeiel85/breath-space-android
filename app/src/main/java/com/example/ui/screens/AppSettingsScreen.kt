package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun AppSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

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
                    onClick = onBack,
                    modifier = Modifier.testTag("app_info_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.button_exit),
                        tint = WarmIvory
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.info_title),
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // App Description card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Breath Space",
                        color = SoftPurple,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("info_app_title")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.info_app_story),
                        color = WarmIvory,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        modifier = Modifier.testTag("info_app_description")
                    )
                }

                // MANDATORY Medical Disclaimer card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = borderPulseColor_or_mutedSlate(true)
                ) {
                    Text(
                        text = BreathStrings.MEDICAL_DISCLAIMER_TITLE,
                        color = SoftMint,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("disclaimer_title")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = BreathStrings.MEDICAL_DISCLAIMER_TEXT,
                        color = WarmIvory,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.testTag("disclaimer_text")
                    )
                }

                // Settings reset option
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.info_app_manage),
                        color = WarmIvory,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.info_app_privacy),
                        color = MutedSlate,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Version Stamp
                Text(
                    text = "VERSION 1.0.0 (COMPOSE CLOUD)",
                    color = MutedSlate.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun borderPulseColor_or_mutedSlate(isAccent: Boolean): Color {
    return if (isAccent) SoftMint.copy(alpha = 0.3f) else BorderMidnight
}
