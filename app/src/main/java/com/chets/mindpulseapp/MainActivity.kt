package com.chets.mindpulseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chets.mindpulseapp.screens.AppNavigation
import com.chets.mindpulseapp.ui.theme.MindPulseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindPulseAppTheme {
                AppNavigation()
            }
        }
    }
}
