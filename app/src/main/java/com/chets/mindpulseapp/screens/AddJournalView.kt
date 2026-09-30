package com.chets.mindpulseapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chets.mindpulseapp.data.MoodObjects
import com.chets.mindpulseapp.data.moodList
import com.chets.mindpulseapp.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalView(
    navController: NavController,
    journalViewModel: JournalViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    val context = LocalContext.current
    val isEditing = journalViewModel?.isEditing ?: false
    val currentMood = journalViewModel?.selectedMood

    val selectedMoodObject = remember(currentMood) {
        mutableStateOf(moodList.firstOrNull { it.mood.equals(currentMood, ignoreCase = true) })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Edit Journal Entry" else "Journal Entry",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        journalViewModel?.resetState()
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Arrow Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardColors(
                    contentColor = MaterialTheme.colorScheme.surfaceContainer,
                    disabledContentColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "What's in your mind?",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    OutlinedTextField(
                        value = journalViewModel?.journalContent ?: "",
                        onValueChange = {
                            journalViewModel?.onJournalContentChange(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        placeholder = { Text(text = "Enter your thoughts!") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "How are you feeling today?",
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                moodList.forEach { item ->
                    MoodBox(
                        mood = item.mood,
                        emoji = item.emoji,
                        tint = item.tint,
                        isSelected = selectedMoodObject.value == item,
                        onClick = {
                            val newMood = if (selectedMoodObject.value == item) null else item
                            selectedMoodObject.value = newMood
                            journalViewModel?.onMoodChange(newMood?.mood)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val content = journalViewModel?.journalContent?.trim().orEmpty()
                        if (content.isEmpty()) {
                            Toast.makeText(context, "Please enter your thoughts", Toast.LENGTH_SHORT).show()
                        } else {
                            journalViewModel?.saveJournalEntry(
                                content = content,
                                mood = selectedMoodObject.value?.mood
                            )
                            Toast.makeText(
                                context,
                                if (isEditing) "Journal Entry Updated" else "Journal Entry Created",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isEditing) "Update Entry" else "Save Entry",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (isEditing) {
                            journalViewModel?.deleteCurrentEntry()
                            Toast.makeText(context, "Journal Entry Deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            journalViewModel?.resetState()
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isEditing) "Delete Entry" else "Discard Entry",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddJournalPreview() {
    AddJournalView(rememberNavController())
}
