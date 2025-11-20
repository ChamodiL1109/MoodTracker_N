package com.example.moodtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "mood_entries")
data class MoodEntry(
    // The timestamp is used as the primary key as it's guaranteed to be unique
    @PrimaryKey(autoGenerate = false)
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String, // e.g., "Happy", "Sad"
    val timestampFormatted: String = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(timestamp))
)

// Provided list of moods with emojis for Challenge 1A (Visual Flair)
object Moods {
    val availableMoods = listOf(
        "Happy 😊",
        "Calm 🙂",
        "Neutral 😐",
        "Sad 😟",
        "Anxious 😬"
    )
}