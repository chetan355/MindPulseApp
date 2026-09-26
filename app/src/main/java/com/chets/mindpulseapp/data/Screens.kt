package com.chets.mindpulseapp.data

import androidx.annotation.DrawableRes
import com.chets.mindpulseapp.R

sealed class Screens(val title : String, val route : String)
{
    object AddJournal : Screens("Add Journal", "add_journal")

    sealed class BottomScreen(val bTitle:String, val bRoute:String, @DrawableRes val icon: Int) : Screens(bTitle,bRoute)
    {
        object Home : BottomScreen(
            "Home",
            "home",
            R.drawable.baseline_home_filled_24
        )
        object Insights : BottomScreen(
            "Insights",
            "insights",
            R.drawable.outline_analytics_24
        )
        object Habits : BottomScreen(
            "Habits",
            "habits",
            R.drawable.outline_target_check_24
        )
    }
}

val screensInBottom = listOf(
    Screens.BottomScreen.Home,
    Screens.BottomScreen.Insights,
    Screens.BottomScreen.Habits
)
