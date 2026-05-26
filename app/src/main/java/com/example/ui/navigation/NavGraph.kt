package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.*
import com.example.ui.viewmodel.BreathViewModel

object NavRoutes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val MOOD_CHECK = "mood_check"
    const val BREATHING_SESSION = "breathing_session"
    const val SESSION_COMPLETE = "session_complete"
    const val REMINDERS = "reminders"
    const val APP_INFO = "app_info"
}

@Composable
fun BreathNavGraph(
    navController: NavHostController,
    viewModel: BreathViewModel = viewModel(),
    startDestination: String
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val todaySessionsList by viewModel.todaySessions.collectAsState()
    val totalCount by viewModel.totalSessionsCount.collectAsState()
    val totalDurationSec by viewModel.totalBreathingDuration.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {
        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(
                onOnboardingFinished = {
                    viewModel.completeOnboarding()
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                    }
                },
                onRequestNotifications = { granted ->
                    if (granted) {
                        // set default evening reminder enabled
                        viewModel.saveNotificationSettings(
                            enabled = true,
                            hour = 20,
                            minute = 0,
                            frequency = 1
                        )
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                completedTodayCount = todaySessionsList.size,
                totalMinutes = totalDurationSec,
                defaultSessionDurationSeconds = prefs.defaultSessionDuration,
                onDurationSelected = { seconds ->
                    viewModel.setSessionDuration(seconds)
                },
                onStartBreathing = {
                    // Start from default selected dur
                    viewModel.sessionDurationSeconds = prefs.defaultSessionDuration
                    navController.navigate(NavRoutes.MOOD_CHECK)
                },
                onNavigateToSettings = {
                    // Set duration option is directly on HomeScreen for convenient use, fallback uses reminders
                    navController.navigate(NavRoutes.REMINDERS)
                },
                onNavigateToReminders = {
                    navController.navigate(NavRoutes.REMINDERS)
                },
                onNavigateToInfo = {
                    navController.navigate(NavRoutes.APP_INFO)
                }
            )
        }

        composable(NavRoutes.MOOD_CHECK) {
            MoodCheckScreen(
                onMoodSelected = { mood ->
                    viewModel.sessionMoodBefore = mood
                    navController.navigate(NavRoutes.BREATHING_SESSION) {
                        popUpTo(NavRoutes.MOOD_CHECK) { inclusive = true }
                    }
                },
                onSkip = {
                    viewModel.sessionMoodBefore = "모르겠어요"
                    navController.navigate(NavRoutes.BREATHING_SESSION) {
                        popUpTo(NavRoutes.MOOD_CHECK) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.BREATHING_SESSION) {
            BreathingSessionScreen(
                sessionDurationSeconds = viewModel.sessionDurationSeconds,
                onSessionCompleted = {
                    navController.navigate(NavRoutes.SESSION_COMPLETE) {
                        popUpTo(NavRoutes.BREATHING_SESSION) { inclusive = true }
                    }
                },
                onBackToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.BREATHING_SESSION) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.SESSION_COMPLETE) {
            SessionCompleteScreen(
                durationSeconds = viewModel.sessionDurationSeconds,
                onFinish = { moodAfter ->
                    viewModel.completeBreathingSession(moodAfter)
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SESSION_COMPLETE) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.REMINDERS) {
            ReminderSettingsScreen(
                currentEnabled = prefs.isNotificationEnabled,
                currentHour = prefs.notificationHour,
                currentMinute = prefs.notificationMinute,
                currentFrequency = prefs.notificationFrequency,
                onSaveSettings = { enabled, hr, min, freq ->
                    viewModel.saveNotificationSettings(enabled, hr, min, freq)
                },
                onSendTestNotification = {
                    viewModel.testNotification()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.APP_INFO) {
            AppSettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
