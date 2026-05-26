package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.BreathingSessionEntity
import com.example.data.database.UserPreferencesEntity
import com.example.data.repository.BreathRepository
import com.example.receiver.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BreathViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BreathRepository(
        database.userPreferencesDao(),
        database.breathingSessionDao()
    )

    val userPreferences: StateFlow<UserPreferencesEntity> = repository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesEntity()
        )

    val todaySessions: StateFlow<List<BreathingSessionEntity>> = repository.todaySessionsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalSessionsCount: StateFlow<Int> = repository.totalCompletedSessionsCountFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val totalBreathingDuration: StateFlow<Int> = repository.totalBreathingDurationFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Bridge states for current session
    var sessionMoodBefore: String = "모르겠어요"
    var sessionDurationSeconds: Int = 60

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.setOnboardingCompleted(true)
        }
    }

    fun saveNotificationSettings(enabled: Boolean, hour: Int, minute: Int, frequency: Int) {
        viewModelScope.launch {
            repository.setNotificationSettings(enabled, hour, minute, frequency)
            ReminderScheduler.scheduleAlarms(getApplication(), enabled, hour, minute, frequency)
        }
    }

    fun setSessionDuration(seconds: Int) {
        viewModelScope.launch {
            repository.setDefaultSessionDuration(seconds)
        }
    }

    fun completeBreathingSession(moodAfter: String) {
        viewModelScope.launch {
            val session = BreathingSessionEntity(
                moodBefore = sessionMoodBefore,
                moodAfter = moodAfter,
                durationSeconds = sessionDurationSeconds
            )
            repository.insertSession(session)
        }
    }

    fun testNotification() {
        ReminderScheduler.triggerTestNotification(getApplication())
    }
}
