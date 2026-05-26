package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breathing_sessions")
data class BreathingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val moodBefore: String,
    val moodAfter: String,
    val durationSeconds: Int
)
