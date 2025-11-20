package com.example.moodtracker.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moodtracker.data.MoodEntry
import com.example.moodtracker.viewmodel.MoodsUiState

// --- Tier 1A: Helper function to get a mood-specific color ---
@Composable
private fun getMoodColor(mood: String): Color {
    return when {
        mood.startsWith("Happy") -> Color(0xFF66BB6A) // Green
        mood.startsWith("Calm") -> Color(0xFF42A5F5) // Blue
        mood.startsWith("Neutral") -> Color(0xFFFFA726) // Orange
        mood.startsWith("Sad") -> Color(0xFF7986CB) // Indigo
        mood.startsWith("Anxious") -> Color(0xFFFF7043) // Red-Orange
        else -> MaterialTheme.colorScheme.primary
    }
}

// --- Composable for a single Mood Selector Item (Tier 1A) ---
@Composable
fun MoodSelectorItem(
    mood: String,
    onMoodSelected: (String) -> Unit
) {
    val (name, emoji) = mood.split(" ")
    val moodColor = getMoodColor(mood)

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = moodColor.copy(alpha = 0.9f)),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(100.dp) // Fixed width for clean layout
            .clickable { onMoodSelected(mood) }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- Composable for a single Mood History Item (Tier 2B: Deletion) ---
@Composable
fun MoodHistoryItem(
    entry: MoodEntry,
    onMoodDeleted: (MoodEntry) -> Unit
) {
    val (moodName, moodEmoji) = entry.mood.split(" ")
    val moodColor = getMoodColor(entry.mood)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Mood and Timestamp details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = moodEmoji,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = moodName,
                        fontWeight = FontWeight.Bold,
                        color = moodColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = entry.timestampFormatted,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Deletion Icon (Tier 2B)
            IconButton(onClick = { onMoodDeleted(entry) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Mood Entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// --- Main Screen Composable (Core Requirement) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrackerScreen(
    uiState: MoodsUiState,
    onMoodSelected: (String) -> Unit,
    onMoodDeleted: (MoodEntry) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Phoenix: Mood Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Section 1: Mood Selector (Row)
            Text(
                text = "How are you feeling right now?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                uiState.availableMoods.forEach { mood ->
                    MoodSelectorItem(mood = mood, onMoodSelected = onMoodSelected)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Section 2: Mood History (LazyColumn)
            Text(
                text = "Mood History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            // Tier 1B: Empty State UI
            AnimatedVisibility(
                visible = uiState.moodHistory.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EmptyStateView()
            }

            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp)
            ) {
                items(uiState.moodHistory, key = { it.timestamp }) { entry ->
                    MoodHistoryItem(entry = entry, onMoodDeleted = onMoodDeleted)
                }
            }
        }
    }
}

// --- Tier 1B: Empty State Composable ---
@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //  - Could be triggered here if an icon/image was desired
        Text(
            text = "🧘‍♀️", // Simple creative element
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "No moods logged yet. How are you feeling today?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}