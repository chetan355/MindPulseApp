package com.chets.mindpulseapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chets.mindpulseapp.data.Screens

class MainViewModel : ViewModel()
{
    private val _currentScreen = mutableStateOf<Screens>(Screens.BottomScreen.Home)

    val currentScreen : MutableState<Screens> get() = _currentScreen
}