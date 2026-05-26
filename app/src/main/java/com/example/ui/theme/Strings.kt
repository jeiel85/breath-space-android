package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.R

object BreathStrings {
    val APP_NAME: String
        @Composable get() = stringResource(R.string.app_name)
        
    val APP_DESCRIPTION: String
        @Composable get() = stringResource(R.string.app_description)

    val ONBOARDING_TITLES: List<String>
        @Composable get() = listOf(
            stringResource(R.string.onboarding_title_0),
            stringResource(R.string.onboarding_title_1),
            stringResource(R.string.onboarding_title_2)
        )

    val ONBOARDING_DESCS: List<String>
        @Composable get() = listOf(
            stringResource(R.string.onboarding_desc_0).replace("\\n", "\n"),
            stringResource(R.string.onboarding_desc_1).replace("\\n", "\n"),
            stringResource(R.string.onboarding_desc_2).replace("\\n", "\n")
        )

    val ALLOW_NOTIFICATIONS_PROMPT: String
        @Composable get() = stringResource(R.string.allow_notifications_prompt)

    val ONBOARDING_BUTTON_NEXT: String
        @Composable get() = stringResource(R.string.onboarding_button_next)

    val ONBOARDING_BUTTON_START: String
        @Composable get() = stringResource(R.string.onboarding_button_start)

    val ONBOARDING_SKIP: String
        @Composable get() = stringResource(R.string.onboarding_skip)

    val GREETING_MORNING: String
        @Composable get() = stringResource(R.string.greeting_morning)

    val GREETING_DAY: String
        @Composable get() = stringResource(R.string.greeting_day)

    val GREETING_NIGHT: String
        @Composable get() = stringResource(R.string.greeting_night)

    val STATS_HEADER: String
        @Composable get() = stringResource(R.string.stats_header)

    val STATS_COMPLETED_TODAY: String
        @Composable get() = stringResource(R.string.stats_completed_today)

    val STATS_TOTAL_TIME: String
        @Composable get() = stringResource(R.string.stats_total_time)

    val STATS_UNIT_SESSIONS: String
        @Composable get() = stringResource(R.string.stats_unit_sessions)

    val STATS_UNIT_MIN_SEC: String
        @Composable get() = stringResource(R.string.stats_unit_min_sec)

    val STATS_UNIT_SEC: String
        @Composable get() = stringResource(R.string.stats_unit_sec)

    val NOT_STARTED_YET: String
        @Composable get() = stringResource(R.string.not_started_yet)

    val MOOD_CHECK_QUESTION: String
        @Composable get() = stringResource(R.string.mood_check_question)

    val MOOD_CHECK_SUBTITLE: String
        @Composable get() = stringResource(R.string.mood_check_subtitle)

    val MOOD_OPTION_1: String
        @Composable get() = stringResource(R.string.mood_option_1)

    val MOOD_OPTION_2: String
        @Composable get() = stringResource(R.string.mood_option_2)

    val MOOD_OPTION_3: String
        @Composable get() = stringResource(R.string.mood_option_3)

    val MOOD_OPTION_4: String
        @Composable get() = stringResource(R.string.mood_option_4)

    val MOOD_OPTION_5: String
        @Composable get() = stringResource(R.string.mood_option_5)

    val MOOD_SKIP: String
        @Composable get() = stringResource(R.string.mood_skip)

    val PHASE_INHALE: String
        @Composable get() = stringResource(R.string.phase_inhale)

    val PHASE_HOLD: String
        @Composable get() = stringResource(R.string.phase_hold)

    val PHASE_EXHALE: String
        @Composable get() = stringResource(R.string.phase_exhale)

    val PHASE_COMFORT: String
        @Composable get() = stringResource(R.string.phase_comfort)

    val BUTTON_PAUSE: String
        @Composable get() = stringResource(R.string.button_pause)

    val BUTTON_RESUME: String
        @Composable get() = stringResource(R.string.button_resume)

    val BUTTON_EXIT: String
        @Composable get() = stringResource(R.string.button_exit)

    val COMPLETE_TITLE_1: String
        @Composable get() = stringResource(R.string.complete_title_1)

    val COMPLETE_TITLE_2: String
        @Composable get() = stringResource(R.string.complete_title_2)

    val COMPLETE_TITLE_3: String
        @Composable get() = stringResource(R.string.complete_title_3)

    val BUTTON_RETRY: String
        @Composable get() = stringResource(R.string.button_retry)

    val BUTTON_GO_HOME: String
        @Composable get() = stringResource(R.string.button_go_home)

    val SETTINGS_TITLE: String
        @Composable get() = stringResource(R.string.settings_title)

    val SETTINGS_MEDITATION_TIME: String
        @Composable get() = stringResource(R.string.settings_meditation_time)

    val COMP_DURATION_1: String
        @Composable get() = stringResource(R.string.comp_duration_1)

    val COMP_DURATION_3: String
        @Composable get() = stringResource(R.string.comp_duration_3)

    val COMP_DURATION_5: String
        @Composable get() = stringResource(R.string.comp_duration_5)

    val NOTIF_SETTINGS_TITLE: String
        @Composable get() = stringResource(R.string.notif_settings_title)

    val NOTIF_ENABLE: String
        @Composable get() = stringResource(R.string.notif_enable)

    val NOTIF_TIME_SELECT: String
        @Composable get() = stringResource(R.string.notif_time_select)

    val NOTIF_FREQUENCY_SELECT: String
        @Composable get() = stringResource(R.string.notif_frequency_select)

    val NOTIF_FREQ_1: String
        @Composable get() = stringResource(R.string.notif_freq_1)

    val NOTIF_FREQ_2: String
        @Composable get() = stringResource(R.string.notif_freq_2)

    val NOTIF_FREQ_3: String
        @Composable get() = stringResource(R.string.notif_freq_3)

    val BUTTON_NOTIF_TEST: String
        @Composable get() = stringResource(R.string.button_notif_test)

    val TEST_NOTIF_SENT: String
        @Composable get() = stringResource(R.string.test_notif_sent)

    val MEDICAL_DISCLAIMER_TITLE: String
        @Composable get() = stringResource(R.string.medical_disclaimer_title)

    val MEDICAL_DISCLAIMER_TEXT: String
        @Composable get() = stringResource(R.string.medical_disclaimer_text)
}
