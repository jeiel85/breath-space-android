package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val isOnboardingCompleted: Boolean = false,
    val isNotificationEnabled: Boolean = false,
    val notificationHour: Int = 20, // default 8pm
    val notificationMinute: Int = 0,
    val notificationFrequency: Int = 1, // default once per day
    val defaultSessionDuration: Int = 60 // default 1 minute (60s)
)
