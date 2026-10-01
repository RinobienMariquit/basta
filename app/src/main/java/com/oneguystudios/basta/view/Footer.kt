package com.oneguystudios.basta.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Overview : Screen("overview", "Overview", Icons.Default.Home)
    object Planning : Screen("planning", "Planning", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun BastaBottomBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val screens = listOf(
        Screen.Overview,
        Screen.Planning,
        Screen.Profile
    )

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) }
            )
        }
    }
}
