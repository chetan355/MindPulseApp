package com.chets.mindpulseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.chets.mindpulseapp.data.local.Graph
import com.chets.mindpulseapp.screens.MainView
import com.chets.mindpulseapp.ui.theme.MindPulseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindPulseAppTheme {
                val context = LocalContext.current
                Graph.provide(context)
                MainView()
            }
        }
    }
}
