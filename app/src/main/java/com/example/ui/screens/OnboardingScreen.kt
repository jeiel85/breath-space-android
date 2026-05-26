package com.example.ui.screens

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CalmButton
import com.example.ui.components.GlassCard
import com.example.ui.components.SoftGradientBackground
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingFinished: () -> Unit,
    onRequestNotifications: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    // Register simple request notification launcher for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onRequestNotifications(isGranted)
        if (isGranted) {
            val toastMsg = if (java.util.Locale.getDefault().language == "ko") {
                "알림 설정이 완료되었습니다."
            } else {
                "Notification settings completed."
            }
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        }
        onOnboardingFinished()
    }

    SoftGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onOnboardingFinished,
                    modifier = Modifier.testTag("onboarding_skip_btn")
                ) {
                    Text(
                        text = BreathStrings.ONBOARDING_SKIP,
                        color = MutedSlate,
                        fontSize = 14.sp
                    )
                }
            }

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Accent ring / visual representation during onboarding
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(SoftPurple.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = SoftPurple,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = BreathStrings.ONBOARDING_TITLES[page],
                        color = WarmIvory,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        modifier = Modifier
                            .testTag("onboarding_title_$page")
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = BreathStrings.ONBOARDING_DESCS[page],
                        color = MutedSlate,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .testTag("onboarding_desc_$page")
                            .fillMaxWidth()
                    )
                    
                    if (page == 2) {
                        Spacer(modifier = Modifier.height(32.dp))
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = CardMidnight.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = BreathStrings.ALLOW_NOTIFICATIONS_PROMPT,
                                color = SoftPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Bottom actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicators Pips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    repeat(3) { idx ->
                        val isSelected = pagerState.currentPage == idx
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 18.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) SoftPurple else BorderMidnight)
                        )
                    }
                }

                // Next / Finish Button
                val isLastPage = pagerState.currentPage == 2
                CalmButton(
                    text = if (isLastPage) BreathStrings.ONBOARDING_BUTTON_START else BreathStrings.ONBOARDING_BUTTON_NEXT,
                    onClick = {
                        if (isLastPage) {
                            // On Android 13+, request POST_NOTIFICATIONS
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                onRequestNotifications(true)
                                onOnboardingFinished()
                            }
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .testTag("onboarding_action_btn")
                )
            }
        }
    }
}
