package com.chets.mindpulseapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Habits(onAddHabitClick : ()-> Unit)
{
    val backgroundColor = MaterialTheme.colorScheme.background

    Surface(modifier = Modifier.fillMaxSize(),
        color = backgroundColor,
        contentColor = MaterialTheme.colorScheme.onBackground)
    {
        Column(modifier = Modifier.fillMaxSize())
        {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Text(text = "Your Habits",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(2.dp)
                        )
                    Text(text = "Small steps, steady progress",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(2.dp))
                }
                IconButton(onClick = {onAddHabitClick()}, modifier = Modifier.padding(2.dp)) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add habit")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitsPreview(){
    Habits(onAddHabitClick = {})
}