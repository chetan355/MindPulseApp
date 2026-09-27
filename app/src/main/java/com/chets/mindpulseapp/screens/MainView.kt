package com.chets.mindpulseapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chets.mindpulseapp.R
import com.chets.mindpulseapp.data.Screens
import com.chets.mindpulseapp.data.screensInBottom
import com.chets.mindpulseapp.viewmodel.JournalViewModel
import com.chets.mindpulseapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    val viewModel: MainViewModel = viewModel()
    val journalViewModel : JournalViewModel = viewModel()
    val currentScreen = remember { viewModel.currentScreen.value }
    val title = remember { mutableStateOf(currentScreen.title) }

    val scaffoldState = rememberScaffoldState()

    val controller = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navContainerColor = MaterialTheme.colorScheme.primaryContainer
    val navContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    val navUnselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    when{
        (currentRoute == Screens.BottomScreen.Home.route) -> title.value = Screens.BottomScreen.Home.title
        (currentRoute == Screens.BottomScreen.Insights.route) -> title.value = Screens.BottomScreen.Insights.title
        (currentRoute == Screens.BottomScreen.Habits.route) -> title.value = Screens.BottomScreen.Habits.title
    }

    val isBottomScreen = screensInBottom.any { it.bRoute == currentRoute } || currentRoute == null

    val topBar: @Composable () -> Unit = {
        if (isBottomScreen) {
            TopAppBar(
                title = { Text(title.value) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mindpulse_logo),
                        contentDescription = "MindPulseLogo",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                }
            )
        }
    }

    val bottomBar: @Composable () -> Unit = {
        if (isBottomScreen) {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = navContainerColor,
                contentColor = navContentColor,
                windowInsets = WindowInsets.navigationBars
            ) {
                screensInBottom.forEach { item ->
                    val isSelected = (currentRoute == item.bRoute)
                    val tint = if (isSelected) navContentColor else navUnselectedColor
                    BottomNavigationItem(
                        selected = isSelected,
                        onClick = {
                            title.value = item.bTitle
                            controller.navigate(item.bRoute)
                        },
                        icon = {
                            Icon(
                                tint = tint,
                                painter = painterResource(item.icon),
                                contentDescription = item.bTitle
                            )
                        },
                        label = { Text(item.bTitle, color = tint) },
                        selectedContentColor = navContentColor,
                        unselectedContentColor = navUnselectedColor
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar,
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Navigation(navController = controller, viewModel = viewModel,journalViewModel = journalViewModel, pd = paddingValues)
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, journalViewModel: JournalViewModel, pd: PaddingValues) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screens.BottomScreen.Home.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(Screens.BottomScreen.Home.bRoute) {
            Home(
                onAddJournalClick = {
                    navController.navigate(Screens.AddJournal.route)
                },
                journalViewModel = journalViewModel
            )
        }
        composable(Screens.BottomScreen.Insights.bRoute) {
            Insights()
        }
        composable(Screens.BottomScreen.Habits.bRoute) {
            Habits()
        }
        composable(Screens.AddJournal.route) {
            AddJournalView(navController,journalViewModel)
        }
    }
}
