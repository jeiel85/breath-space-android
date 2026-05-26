package com.example.data.repository

import com.example.data.database.BreathingSessionDao
import com.example.data.database.BreathingSessionEntity
import com.example.data.database.UserPreferencesDao
import com.example.data.database.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class BreathRepository(
    private val userPreferencesDao: UserPreferencesDao,
    private val breathingSessionDao: BreathingSessionDao
) {
    // Preferences Flow, guaranteeing a non-null object
    val userPreferencesFlow: Flow<UserPreferencesEntity> = userPreferencesDao.getUserPreferencesFlow()
        .map { it ?: UserPreferencesEntity() }

    suspend fun getUserPreferences(): UserPreferencesEntity {
        return userPreferencesDao.getUserPreferences() ?: UserPreferencesEntity()
    }

    suspend fun saveUserPreferences(preferences: UserPreferencesEntity) {
        userPreferencesDao.saveUserPreferences(preferences)
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        val prefs = getUserPreferences()
        saveUserPreferences(prefs.copy(isOnboardingCompleted = completed))
    }

    suspend fun setNotificationSettings(enabled: Boolean, hour: Int, minute: Int, frequency: Int) {
        val prefs = getUserPreferences()
        saveUserPreferences(
            prefs.copy(
                isNotificationEnabled = enabled,
                notificationHour = hour,
                notificationMinute = minute,
                notificationFrequency = frequency
            )
        )
    }

    suspend fun setDefaultSessionDuration(seconds: Int) {
        val prefs = getUserPreferences()
        saveUserPreferences(prefs.copy(defaultSessionDuration = seconds))
    }

    // Sessions flow
    val allSessionsFlow: Flow<List<BreathingSessionEntity>> = breathingSessionDao.getAllSessionsFlow()

    // Filter today's sessions reactively
    val todaySessionsFlow: Flow<List<BreathingSessionEntity>> = allSessionsFlow.map { sessions ->
        val todayStart = getStartOfToday()
        sessions.filter { it.timestamp >= todayStart }
    }

    val totalCompletedSessionsCountFlow: Flow<Int> = breathingSessionDao.getTotalSessionsCountFlow()

    val totalBreathingDurationFlow: Flow<Int> = breathingSessionDao.getTotalDurationFlow()
        .map { it ?: 0 }

    suspend fun insertSession(session: BreathingSessionEntity) {
        breathingSessionDao.insertSession(session)
    }

    private fun getStartOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
