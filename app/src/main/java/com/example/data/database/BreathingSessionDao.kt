package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreathingSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: BreathingSessionEntity)

    @Query("SELECT * FROM breathing_sessions ORDER BY timestamp DESC")
    fun getAllSessionsFlow(): Flow<List<BreathingSessionEntity>>

    @Query("SELECT * FROM breathing_sessions WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getSessionsSinceFlow(sinceTimestamp: Long): Flow<List<BreathingSessionEntity>>

    @Query("SELECT COUNT(*) FROM breathing_sessions")
    fun getTotalSessionsCountFlow(): Flow<Int>

    @Query("SELECT SUM(durationSeconds) FROM breathing_sessions")
    fun getTotalDurationFlow(): Flow<Int?>
}
