package com.chets.mindpulseapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddHabitView()
{
    Scaffold (modifier = Modifier.fillMaxSize(),
        {TopAppBar(title =
            { Text("New Habit") })
        }
    )
    {it->
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Habit Name",
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddHabitViewPreview()
{
    AddHabitView()
}