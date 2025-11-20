package com.example.moodtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Insert
    suspend fun insert(mood: MoodEntry)

    @Delete
    suspend fun delete(mood: MoodEntry)
}