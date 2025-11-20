package com.example.moodtracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.MoodDao
import com.example.moodtracker.data.MoodEntry
import com.example.moodtracker.data.Moods
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Interface for the UI state
data class MoodsUiState(
    val moodHistory: List<MoodEntry> = emptyList(),
    val availableMoods: List<String> = Moods.availableMoods
)

class MoodViewModel(private val moodDao: MoodDao) : ViewModel() {

    // 1. State Flow for Mood History (Core Requirement)
    val uiState: StateFlow<MoodsUiState> =
        moodDao.getAllMoods().map { moodList ->
            MoodsUiState(moodHistory = moodList)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MoodsUiState()
        )

    /**
     * Adds a new mood entry to the database.
     * @param mood The string representing the selected mood.
     */
    fun addMoodEntry(mood: String) {
        val newEntry = MoodEntry(mood = mood)
        viewModelScope.launch {
            moodDao.insert(newEntry)
        }
    }

    /**
     * Deletes a mood entry from the database. (Tier 2 Requirement)
     * @param entry The MoodEntry object to delete.
     */
    fun deleteMoodEntry(entry: MoodEntry) {
        viewModelScope.launch {
            moodDao.delete(entry)
        }
    }
}