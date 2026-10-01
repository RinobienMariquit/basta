package com.oneguystudios.basta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oneguystudios.basta.view.*
import com.oneguystudios.basta.view.theme.BastaTheme
import com.oneguystudios.basta.viewmodel.PlannerViewModel
import com.oneguystudios.basta.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BastaTheme {
                val plannerViewModel: PlannerViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()

                val dailyLog by plannerViewModel.dailyLog.collectAsState()
                val userProfile by profileViewModel.userProfile.collectAsState()

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Overview) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BastaBottomBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { screen -> currentScreen = screen }
                        )
                    }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            is Screen.Overview -> OverviewScreen(
                                dailyLog = dailyLog,
                                userProfile = userProfile
                            )
                            is Screen.Planning -> PlanningScreen(
                                dailyLog = dailyLog,
                                onAddItem = { item -> plannerViewModel.addMealItem(item) },
                                onRemoveItem = { itemId -> plannerViewModel.removeMealItem(itemId) }
                            )
                            is Screen.Profile -> ProfileScreen(
                                userProfile = userProfile,
                                onUpdateProfile = { sex, age, height, weight, calorieGoal, allergies, preferences ->
                                    profileViewModel.updateProfile(sex, age, height, weight, calorieGoal, allergies, preferences)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
