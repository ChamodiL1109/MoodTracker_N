package com.example.moodtracker

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodtracker.data.MoodDatabase
import com.example.moodtracker.ui.theme.MoodTrackerScreen
import com.example.moodtracker.ui.theme.MoodTrackerTheme
import com.example.moodtracker.viewmodel.MoodViewModel
import com.example.moodtracker.viewmodel.MoodViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and DAO
        val database = MoodDatabase.getDatabase(applicationContext)
        val moodDao = database.moodDao()

        setContent {
            MoodTrackerTheme {
                // Initialize the ViewModel using the Factory
                val viewModel: MoodViewModel = viewModel(
                    factory = MoodViewModelFactory(moodDao)
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Collect the state from the ViewModel
                    val uiState by viewModel.uiState.collectAsState()

                    // The main screen Composable
                    MoodTrackerScreen(
                        uiState = uiState,
                        onMoodSelected = viewModel::addMoodEntry,
                        onMoodDeleted = viewModel::deleteMoodEntry
                    )
                }
            }
        }
    }
}